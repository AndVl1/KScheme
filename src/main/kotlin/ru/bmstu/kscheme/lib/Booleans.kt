package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.type.KSBoolean

object Booleans {
    val primitives = arrayOf(
        KSBooleanQ,
        KSBooleanNot
    )

    object KSBooleanNot : Primitive(
        name = "not",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "True if arg is false, else true",
        documentation = "(not #f) => #t"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 == KSBoolean.FALSE)
        }
    }

    object KSBooleanQ : Primitive(
        name = "boolean?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "True if arg is boolean, else false",
        documentation = "(boolean? #f) => #t"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is KSBoolean)
        }
    }
}

