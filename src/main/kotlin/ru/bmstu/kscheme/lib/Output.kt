package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.io.OutputPort
import ru.bmstu.kscheme.lang.type.Void

object Output {
    val primitives = arrayOf(Display, Write, Newline, Flush)

    object Display: Primitive(
        name = "display",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 2,
        comment = "human-readable form (display \"hello\")",
        documentation = "(display \"hello\") => hello"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            val out: OutputPort = getOutputPort(this, environment, arg2)
            if (out.isOpen) {
                out.display(arg1)
                return Void.VALUE
            } else {
                throw Exception("Closed output port")
            }
        }
    }

    object Write: Primitive(
        name = "write",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 2,
        comment = "machine-readable form (write \"hello\")",
        documentation = "(write \"hello\") => \"hello\""
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            val out = getOutputPort(this, environment, arg2)
            if (out.isOpen) {
                out.write(arg1)
                return Void.VALUE
            } else {
                throw Exception("Closed output port")
            }
        }
    }

    object Newline: Primitive(
        name = "newline",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 0,
        maxArgs = 1,
        comment = "Writes end of line to current or specified port",
        documentation = "(newline)"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            val out = getOutputPort(this, environment, arg2)
            if (out.isOpen) {
                out.newline()
                return Void.VALUE
            } else {
                throw Exception("Closed output port")
            }
        }
    }

    object Flush: Primitive(
        name = "flush",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 0,
        maxArgs = 1,
        comment = "Flushes current or specified port",
        documentation = "(flush)"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            val out = getOutputPort(this, environment, arg2)
            if (out.isOpen) {
                out.flush()
                return Void.VALUE
            } else {
                throw Exception("Closed output port")
            }
        }
    }

    private fun getOutputPort(primitive: Primitive, env: Environment, entity: Entity): OutputPort {
        return when (entity) {
            is OutputPort -> {
                entity
            }
            Void.VALUE -> {
                env.out!!
            }
            else -> {
                throw Exception("$entity Not an output port")
            }
        }
    }
}
