package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.io.InputPort
import ru.bmstu.kscheme.lang.io.OutputPort
import ru.bmstu.kscheme.lang.io.Port
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.KSBoolean

object Ports {
    val primitives = arrayOf(PortQ, InpPortQ, OutPortQ, TextPortQ, BinPortQ, CurInputPort, CurOutputPort)

    object PortQ : Primitive(
        name = "port?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "True if arg is port, else false",
        documentation = "(port? (current-input-port)) => #t"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is Port)
        }
    }

    object InpPortQ : Primitive(
        name = "input-port?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "True if arg is input port, else false",
        documentation = "(input-port? (current-input-port)) => #t"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is InputPort)
        }
    }

    object OutPortQ : Primitive(
        name = "output-port?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "True if arg is output port, else false",
        documentation = "(output-port? (current-input-port)) => #f"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is OutputPort)
        }
    }

    object TextPortQ : Primitive(
        name = "textual-port?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "True if arg is textual port, else false",
        documentation = "(textual-port? (current-input-port)) => #t"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean((arg1 as? OutputPort)?.kind == Port.Kind.TEXT)
        }
    }

    object BinPortQ : Primitive(
        name = "binary-port?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "True if arg is binary port, else false",
        documentation = "(binary-port? (current-input-port)) => #f"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean((arg1 as? OutputPort)?.kind == Port.Kind.BINARY)
        }
    }

    object CurInputPort : Primitive(
        name = "current-input-port",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 0,
        maxArgs = 0,
        comment = "Returns current input port",
        documentation = null
    ) {
        override fun apply0(environment: Environment, cont: Continuation): Entity {
            return environment.`in` ?: throw Exception("Input port null")
        }
    }

    object CurOutputPort : Primitive(
        name = "current-output-port",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 0,
        maxArgs = 0,
        comment = "Returns current output port",
        documentation = null
    ) {
        override fun apply0(environment: Environment, cont: Continuation): Entity {
            return environment.out ?: throw Exception("Output port null")
        }
    }
}