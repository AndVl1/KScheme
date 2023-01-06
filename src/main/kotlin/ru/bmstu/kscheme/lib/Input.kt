package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.io.InputPort
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.KSBoolean
import ru.bmstu.kscheme.lang.type.Void

object Input {

    val primitives = arrayOf(EofQ, Read)

    object EofQ : Primitive(
        name = "eof-object?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "tbd",
        documentation = "tbd"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is Eof)
        }
    }

    object Read : Primitive(
        name = "read",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 0,
        maxArgs = 1,
        comment = "tbd",
        documentation = "tbd"
    ) {
        override fun apply0(environment: Environment, cont: Continuation): Entity {
            return apply1(Void.VALUE, environment, cont)
        }

        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            val inPort = if (arg1 is InputPort) {
                arg1
            } else {
                environment.`in`
            }
            if (inPort?.isOpen == true) {
                return inPort.read()
            } else {
                throw Exception("$this: InputPort closed $inPort")
            }
        }
    }
}