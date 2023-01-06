package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.Symbol
import ru.bmstu.kscheme.lang.type.Void
import kotlin.text.StringBuilder

object Interaction {

    val primitives: Array<Primitive> = arrayOf(Help)

    object Help : Primitive(
        name = "help",
        definitionEnv = Environment.Kind.INTERACTION_ENV,
        keyword = Primitive.KEYWORD,
        minArgs = 0,
        maxArgs = 1,
        comment = "tbd",
        documentation = "tbd"
    ) {
        override fun apply0(environment: Environment, cont: Continuation): Entity {
            return apply1(Void.VALUE, environment, cont)
        }

        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            val outPort = environment.out ?: throw Exception("$this: Output port null")
            if (arg1 != Void.VALUE) {
                if (arg1 !is Symbol) {
                    throw Exception("$this: Invalid argument $arg1")
                }
                val pName = arg1.toString()
                val doc = Interpreter.getHelpDocumentation(pName)
                doc?.let { documentation ->
                    outPort.print(documentation)
                } ?: run {
                    outPort.print("No documentation available for $pName")
                }
                outPort.newline()
                return Void.VALUE
            }
            val chars = Array(HELP_COLUMN_WIDTH){ ' ' }
            val spc = StringBuilder()
            spc.append(chars)

            val nameSet = Interpreter.getHelpNames()
            for (name in nameSet) {
                val pName = StringBuilder(name)
                val doc = Interpreter.getHelpComment(pName.toString())
                if (pName.length < HELP_COLUMN_WIDTH) {
                    pName.append(chars.toString().subSequence(0, HELP_COLUMN_WIDTH - pName.length))
                }
                outPort.print("$pName $doc")
                outPort.newline()
            }
            outPort.newline()
            return Void.VALUE
        }
    }

    private const val HELP_COLUMN_WIDTH = 20
}