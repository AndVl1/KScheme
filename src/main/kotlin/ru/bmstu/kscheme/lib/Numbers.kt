package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.base.List
import ru.bmstu.kscheme.lang.ListIterator
import ru.bmstu.kscheme.lang.type.Number
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.Real
import java.math.BigDecimal

object Numbers {

    val primitives = arrayOf(
        Minus, Plus, Divide, Multiply
    )

    object Minus : Primitive(
        name = "-",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = -1,
        comment = "Sub (- 5 2)",
        documentation = "(- 5 2) => 3"
    ) {
        override fun applyN(args: List, environment: Environment, cont: Continuation): Entity {
            var result: BigDecimal
            val iterator = ListIterator(args)
            result = -getNumberArgument(this, iterator.next())
            if (iterator.hasNext()) {
                result = -result
            }
            while (iterator.hasNext()) {
                result -= getNumberArgument(this, iterator.next())
            }
            return Real(result)
        }
    }

    object Plus : Primitive(
        name = "+",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = -1,
        comment = "Add (+ 1 2)",
        documentation = "(+ 1 2) => 3"
    ) {
        override fun applyN(args: List, environment: Environment, cont: Continuation): Entity {
            var result = BigDecimal(0)
            val iterator = ListIterator(args)
            while (iterator.hasNext()) {
                result += getNumberArgument(this, iterator.next())
            }
            return Real(result)
        }
    }

    object Divide : Primitive(
        name = "/",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = -1,
        comment = "Add (/ 6 2)",
        documentation = "(/ 6 2) => 3"
    ) {
        override fun applyN(args: List, environment: Environment, cont: Continuation): Entity {
            var result = BigDecimal(1)
            val iterator = ListIterator(args)
            var next = getNumberArgument(this, iterator.next())
            if (next == BigDecimal.ZERO) {
                throw Exception("Division by zero")
            }
            if (!iterator.hasNext()) {
                result /= next
            } else {
                result = next
            }
            while (iterator.hasNext()) {
                next = getNumberArgument(this, iterator.next())
                if (next == BigDecimal.ZERO) {
                    throw Exception("Division by zero")
                }
                result /= next
            }
            return Real(result)
        }
    }

    object Multiply : Primitive(
        name = "*",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = -1,
        comment = "Multiply (* 6 2)",
        documentation = "(* 6 2) => 12"
    ) {
        override fun applyN(args: List, environment: Environment, cont: Continuation): Entity {
            var result = BigDecimal.ONE
            val iterator = ListIterator(args)
            while (iterator.hasNext()) {
                result *= getNumberArgument(this, iterator.next())
            }
            return Real(result)
        }
    }

    private fun getNumberArgument(primitive: Primitive, obj: Entity): BigDecimal {
        return (obj as? Number)?.value ?: throw Exception("$obj Not a number")
    }
}