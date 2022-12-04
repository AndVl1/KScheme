package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.type.Number
import ru.bmstu.kscheme.lang.action.ExpressionAction
import ru.bmstu.kscheme.lang.action.ExpressionInEnvAction
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.KSBoolean
import ru.bmstu.kscheme.lang.type.Void
import java.math.BigDecimal

object Eval {

    val primitives = arrayOf(
        Eval, NullEnv, ReportEnv, InteractionEnv, CurrentEnv, InEnv, MakeEnv, EnvironmentQ
    )

    object Eval : Primitive(
        name = "eval",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 2,
        comment = "tbd",
        documentation = "tbd"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return apply2(arg1, Void.VALUE, environment, cont)
        }

        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            val evalEnv = arg2 as? Environment ?: environment
            val arg = arg1.analyze(environment).optimize(evalEnv)
            cont.begin(ExpressionAction(arg, evalEnv, null))
            return Void.VALUE
        }
    }

    object NullEnv : Primitive(
        name = "null-environment",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "tbd",
        documentation = "tbd"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            val version = arg1 as? Number ?: throw Exception("$this: Not a number $arg1")
            if (version.value == BigDecimal(5)) {
                return Interpreter.getNullEnv(environment) ?:throw Exception("$this: Env is null $environment")
            } else {
                throw Exception("$this: Version $version not supported")
            }
        }
    }

    object ReportEnv : Primitive(
        name = "scheme-report-environment",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "tbd",
        documentation = "tbd"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            val version = arg1 as? Number ?: throw Exception("$this: Not a number $arg1")
            if (version.value == BigDecimal(5)) {
                return Interpreter.getReportEnv(environment) ?:throw Exception("$this: Env is null $environment")
            } else {
                throw Exception("$this: Version $version not supported")
            }
        }
    }

    object InteractionEnv : Primitive(
        name = "interaction-environment",
        definitionEnv = Environment.Kind.INTERACTION_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 0,
        maxArgs = 0,
        comment = "tbd",
        documentation = "tbd"
    ) {
        override fun apply0(environment: Environment, cont: Continuation): Entity {
            return Interpreter.getInteractionEnv(environment) ?: throw Exception("env is null")
        }
    }

    object CurrentEnv : Primitive(
        name = "current-environment",
        definitionEnv = Environment.Kind.INTERACTION_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 0,
        maxArgs = 0,
        comment = "tbd",
        documentation = "tbd"
    ) {
        override fun apply0(environment: Environment, cont: Continuation): Entity {
            return environment
        }
    }

    object InEnv : Primitive(
        name = "in-environment",
        definitionEnv = Environment.Kind.INTERACTION_ENV,
        keyword = Primitive.KEYWORD,
        minArgs = 2,
        maxArgs = 2,
        comment = "tbd",
        documentation = "tbd"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            cont.begin(ExpressionAction(arg1, environment))
                .andThen(ExpressionInEnvAction(arg2, environment))
            return Void.VALUE
        }
    }

    object MakeEnv : Primitive(
        name = "make-environment",
        definitionEnv = Environment.Kind.INTERACTION_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 0,
        maxArgs = 0,
        comment = "tbd",
        documentation = "tbd"
    ) {
        override fun apply0(environment: Environment, cont: Continuation): Entity {
            return Environment(environment)
        }
    }

    object EnvironmentQ : Primitive(
        name = "environment?",
        definitionEnv = Environment.Kind.INTERACTION_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "tbd",
        documentation = "tbd"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is Environment)
        }
    }
}
