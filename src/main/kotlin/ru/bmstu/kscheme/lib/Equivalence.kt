package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.type.KSBoolean
import ru.bmstu.kscheme.lang.type.Number

object Equivalence {

    val primitives = arrayOf(
        Eq, Eqv, More, Less
    )

    object Eq : Primitive(
        name = "eq?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 2,
        maxArgs = 2,
        comment = "Same values in memory",
        documentation = "(eq? 'a 'a) => #t but (eq? (list 'a) (list 'a)) => #f"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 === arg2)
        }
    }

    object Eqv : Primitive(
        name = "eqv?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 2,
        maxArgs = 2,
        comment = "Same values",
        documentation = "(eq? 'a 'a) => #t (eq? (list 'a) (list 'a)) => #t"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 == arg2)
        }
    }

    object More : Primitive(
        name = ">",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 2,
        maxArgs = 2,
        comment = "a > b",
        documentation = "(> 1 2) => #f"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            if (arg1 is Number && arg2 is Number) {
                return KSBoolean.makeKSBoolean(arg1.value > arg2.value)
            }
            else throw Exception("Trying to compare not numbers")
        }
    }

    object Less : Primitive(
        name = "<",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 2,
        maxArgs = 2,
        comment = "a < b",
        documentation = "(< 1 2) => #t"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            if (arg1 is Number && arg2 is Number) {
                return KSBoolean.makeKSBoolean(arg1.value < arg2.value)
            }
            else throw Exception("Trying to compare not numbers")
        }
    }
}