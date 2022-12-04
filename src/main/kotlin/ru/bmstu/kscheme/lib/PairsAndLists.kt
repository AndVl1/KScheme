package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.ListIterator
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.KSBoolean
import ru.bmstu.kscheme.lang.type.Void

object PairsAndLists {
    val primitives = arrayOf(Car, Cdr, Cons, List, PairQ, NullQ, SetCar, SetCdr)

    object Car : Primitive(
        name = "car",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "Gets first object in pair",
        documentation = "(car (list 1 2 3)) => 1"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return try {
                (arg1 as ru.bmstu.kscheme.lang.base.List).car
            } catch (e: Exception) {
                throw Exception("car: invalid argument $arg1")
            }
        }
    }

    object Cdr : Primitive(
        name = "cdr",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "Gets second object in pair",
        documentation = "(cdr (list 1 2 3)) => (2 3)"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return try {
                (arg1 as ru.bmstu.kscheme.lang.base.List).cdr
            } catch (e: Exception) {
                throw Exception("cdr: invalid argument $arg1")
            }
        }
    }

    object Cons : Primitive(
        name = "cons",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 2,
        maxArgs = 2,
        comment = "Creates new pair",
        documentation = "(cons 1 (list 2 3)) => 1 2 3"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            return Pair(arg1, arg2)
        }
    }

    object List : Primitive(
        name = "list",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 0,
        maxArgs = -1,
        comment = "Creates new list from arguments",
        documentation = "(list 1 2 3) => (1 2 3)"
    ) {
        override fun applyN(args: ru.bmstu.kscheme.lang.base.List, environment: Environment, cont: Continuation): Entity {
            val iterator = ListIterator(args)
            if (!iterator.hasNext()) {
                return EmptyList.VALUE
            }
            val l = Pair(iterator.next(), EmptyList.VALUE)
            var ins = l
            while (iterator.hasNext()) {
                val nextCons = Pair(iterator.next(), EmptyList.VALUE)
                ins.cdr = nextCons
                ins = nextCons
            }
            return l
        }
    }

    object PairQ : Primitive(
        name = "pair?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "True if arg is pair, else false",
        documentation = "(pair? (cons 1 2)) => #t"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is Pair)
        }
    }

    object NullQ : Primitive(
        name = "null?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "True if arg is empty list, else false",
        documentation = "(null? '()) => #t"
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is EmptyList)
        }
    }

    object SetCar : Primitive(
        name = "set-car!",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 2,
        maxArgs = 2,
        comment = "Sets car to pair",
        documentation = "(set-car! '(1 2) 5) => (5 1 2)"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            if (arg1 !is Pair) {
                throw Exception("$arg1 not a pair")
            }
            arg1.car = arg2
            return Void.VALUE
        }
    }

    object SetCdr : Primitive(
        name = "set-cdr!",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 2,
        maxArgs = 2,
        comment = "Sets cdr to pair",
        documentation = "(set-cdr! '(1 2) 5) => (1 2 5)"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            if (arg1 !is Pair) {
                throw Exception("$arg1 not a pair")
            }
            arg1.cdr = arg2
            return Void.VALUE
        }
    }
}
