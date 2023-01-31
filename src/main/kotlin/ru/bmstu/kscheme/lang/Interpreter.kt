package ru.bmstu.kscheme.lang

import org.jetbrains.annotations.TestOnly
import ru.bmstu.kscheme.lang.action.ExpressionAction
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.io.InputPort
import ru.bmstu.kscheme.lang.io.OutputPort
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.procedure.PrimitiveProcedure
import ru.bmstu.kscheme.lang.type.Symbol
import ru.bmstu.kscheme.lang.type.Void
import ru.bmstu.kscheme.lib.*
import ru.bmstu.kscheme.util.Logger
import java.io.*
import java.lang.System
import java.util.TreeSet
import kotlin.text.StringBuilder

class Interpreter() {

    var traceEnabled: Boolean = false
        private set
    var bootstrapped: Boolean = false
        private set
    private val continuation: Continuation = Continuation()
    private val nullEnv = SystemEnvironment(Environment.Kind.NULL_ENV)
    private val reportEnv = SystemEnvironment(nullEnv, Environment.Kind.REPORT_ENV)
    private val interactionEnv = SystemEnvironment(reportEnv, Environment.Kind.INTERACTION_ENV)
    var cin: InputPort? = null
        private set
    var cout: OutputPort? = null
        private set
    private var accum: Entity = Void.VALUE

    var sessionEnv: Environment? = null
        private set

    init {
        initEnvironments()
        bindIOPorts()
        setSessionEnv(interactionEnv, Environment.newEnvironment(cin!!, cout!!))
    }

    fun bootstrap() {
        if (!bootstrapped) {
            val bootstrap = InputPort(
                BufferedReader(
                    InputStreamReader(
                        javaClass.getResourceAsStream("/bootstrap.scm") ?: throw Exception("file null")
                    )
                )
            )
            load(bootstrap, reportEnv)
        }
    }

    fun load(reader: InputPort, env: Environment) {
        var obj: Entity = reader.read()
        var value: Entity
        while (obj != Eof.VALUE) {
            Logger.log(Logger.Level.DEBUG, "load: read object $obj")
            value = eval(obj, env)
            Logger.log(Logger.Level.DEBUG, "load: result is $value")
            obj = reader.read()
        }
    }

    fun clearContinuation() {
        continuation.clear()
    }

    fun eval(expr: Entity, env: Environment): Entity {
        val newExpr = expr.analyze(env).optimize(env)
        continuation.begin(ExpressionAction(newExpr, env, null))
        execute()
        return accum
    }

    private fun execute() {
        var currentAction = continuation.head
        var tmp: Entity?
        while (currentAction != null) {
            tmp = currentAction.invoke(accum, continuation)
            if (tmp != null) {
                accum = tmp
            }
            currentAction = continuation.head
        }
    }

    private fun bindIOPorts() {
        cin = InputPort(BufferedReader(InputStreamReader(System.`in`)))
        val isConsole = System.console() != null
        cout = OutputPort(System.out, isConsole)

        nullEnv.`in` = cin
        nullEnv.out = cout
        reportEnv.`in` = cin
        reportEnv.out = cout
        interactionEnv.`in` = cin
        interactionEnv.out = cout
    }

    private fun importPrimitives(primitives: Array<Primitive>) {
        var instEnv: Environment
        for (primitive in primitives) {
            instEnv = when (primitive.definitionEnv) {
                Environment.Kind.NULL_ENV -> nullEnv
                Environment.Kind.REPORT_ENV -> reportEnv
                Environment.Kind.INTERACTION_ENV -> interactionEnv
            }
            installPrimitive(instEnv, primitive)
        }
    }

    private fun installPrimitive(env: Environment, primitive: Primitive) {
        val name = Symbol.makeSymbol(primitive.name)
        val proc = if (primitive.keyword) SyntaxProcedure(primitive) else PrimitiveProcedure(primitive)
        env.define(name, proc)
        if (primitive.keyword) {
            kwSet.add(name)
        }
        helpComment[primitive.name] = primitive.comment
        helpDoc[primitive.name] = primitive.documentation
    }

    private fun initEnvironments() {
        nullEnv.define(INTP_SYMBOL, KtAnyObject(this))
        try {
            importPrimitives(Booleans.primitives)
            importPrimitives(Numbers.primitives)
            importPrimitives(Characters.primitives)
            importPrimitives(ControlFeatures.primitives)
            importPrimitives(Equivalence.primitives)
            importPrimitives(Eval.primitives)
            importPrimitives(Input.primitives)
            importPrimitives(Interaction.primitives)
            importPrimitives(Numbers.primitives)
            importPrimitives(Output.primitives)
            importPrimitives(PairsAndLists.primitives)
            importPrimitives(Ports.primitives)
            importPrimitives(Strings.primitives)
            importPrimitives(Symbols.primitives)
            importPrimitives(Syntax.primitives)
            importPrimitives(SystemInterface.primitives)

            reportEnv.define(Symbol.CALL_CC, reportEnv.lookup(Symbol.CALL_WITH_CURRENT_CONTINUATION))
        } catch (e: Exception) {
            Logger.log(Logger.Level.ERROR, "Error during env initialization: ${e.message}")
        }
    }

    @TestOnly
    fun loadForTest(reader: InputPort, env: Environment): String {
        var lastRes = ""
        var obj: Entity = reader.read()
        var value: Entity
        while (obj != Eof.VALUE) {
            Logger.log(Logger.Level.DEBUG, "load: read object $obj")
            value = eval(obj, env)
            Logger.log(Logger.Level.DEBUG, "load: result is $value")
            lastRes = value.toString()
            obj = reader.read()
        }
        return lastRes
    }

    @TestOnly
    fun loadForOutputTest(reader: InputPort, env: Environment): String {
        val baos = ByteArrayOutputStream()
        val out = PrintStream(baos)
        val outputPort = OutputPort(PrintWriter(out), false)
        var obj: Entity = reader.read()
        var value: Entity
        cout = outputPort
        env.out = outputPort
        while (obj != Eof.VALUE) {
            Logger.log(Logger.Level.DEBUG, "load: read object $obj")
            value = eval(obj, env)
            Logger.log(Logger.Level.DEBUG, "load: result is $value")
            obj = reader.read()
        }
        return baos.toString()
    }

    companion object {
        val INTP_SYMBOL = Symbol.makeUninternedSymbol("__intp__")
        private val kwSet = HashSet<Symbol>()
        private val helpComment = HashMap<String, String>()
        private val helpDoc = HashMap<String, String>()


        fun addForEval(expr: Entity, env: Environment, cont: Continuation) {
            val newExpr = expr.analyze(env).optimize(env)
            cont.begin(ExpressionAction(newExpr, env, null))
        }

        fun setSessionEnv(currentEnv: Environment, newSessionEnv: Environment) {
            val interpreter = currentEnv.interpreter
            interpreter?.sessionEnv = newSessionEnv
            interpreter?.sessionEnv?.parent = interpreter?.interactionEnv
        }

        fun newInterpreter(): Interpreter {
            val interpreter = Interpreter()
            Logger.log(Logger.Level.DEBUG, "Created $interpreter")
            interpreter.bootstrap()
            return interpreter
        }

        fun isKeyword(s: Symbol): Boolean = kwSet.contains(s)

        fun getInteractionEnv(environment: Environment): Entity? {
            return environment.interpreter?.interactionEnv
        }

        fun getNullEnv(environment: Environment): Entity? {
            return environment.interpreter?.nullEnv
        }

        fun getReportEnv(environment: Environment): Entity? {
            return environment.interpreter?.reportEnv
        }

        fun getHelpDocumentation(pName: String): String? {
            return helpDoc[pName]
        }

        fun getHelpNames(): Set<String> {
            return TreeSet(helpDoc.keys)
        }

        fun getHelpComment(name: String): String {
            return helpDoc[name] ?: "No doc yet"
        }
    }
}
