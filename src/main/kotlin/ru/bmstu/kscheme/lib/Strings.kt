package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.KSBoolean
import ru.bmstu.kscheme.lang.type.MutableString

object Strings {

    val primitives: Array<Primitive> = arrayOf(StringQ)

    object StringQ : Primitive(
        name = "string?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "Returns true if arg is string, else false",
        documentation = "(string? \"hello\") => #t",
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is MutableString)
        }
    }
}
