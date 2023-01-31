package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.action.ProcedureCallAction
import ru.bmstu.kscheme.lang.base.List
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.base.Procedure
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.KSBoolean

object ControlFeatures {

    val primitives = arrayOf(
        CallCC, ProcedureQ, ApplyProc
    )

    object CallCC : Primitive (
        name = "call-with-current-continuation",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "",
        documentation = ""
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            if (arg1 is Procedure) {
                val argList = ArgumentList()
                argList.set(0, Continuation(cont))
                cont.begin(ProcedureCallAction(argList, environment))
                return arg1
            } else {
                throw Exception("$this: should be a procedure $arg1")
            }
        }
    }

    object ProcedureQ : Primitive (
        name = "procedure?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "True if arg is procedure, else false",
        documentation = "(procedure? car) => #t"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is Procedure)
        }
    }

    object ApplyProc : Primitive (
        name = "apply",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 2,
        maxArgs = 2,
        comment = "Calls procedure with arguments (apply + 1 2)",
        documentation = null
    ) {
        override fun apply2(proc: Entity, args: Entity, environment: Environment, cont: Continuation): Entity {
            if (proc !is Procedure) {
                throw Exception("$proc not a procedure")
            }
            if (args is List) {
                cont.begin(ProcedureCallAction(ArgumentList(args), environment))
                return proc
            } else {
                throw Exception("$args not a list")
            }

        }
    }
}
