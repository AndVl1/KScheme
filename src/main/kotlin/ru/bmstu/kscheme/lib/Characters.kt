package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.type.Character
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.type.KSBoolean

object Characters {
    val primitives : Array<Primitive> = arrayOf(CharQ)

    object CharQ : Primitive(
        name = "char?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "True if arg is character, else false",
        documentation = "(boolean? #\\a) => #t"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is Character)
        }
    }
}
