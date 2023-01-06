package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.base.List
import ru.bmstu.kscheme.lang.ListIterator
import ru.bmstu.kscheme.lang.action.AssignmentAction
import ru.bmstu.kscheme.lang.action.ExpressionAction
import ru.bmstu.kscheme.lang.action.IfAction
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.procedure.Closure
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.Symbol
import ru.bmstu.kscheme.lang.type.Undefined
import ru.bmstu.kscheme.lang.type.Void

object Syntax {
    val primitives = arrayOf(`Set!`, Quote, Define, Lambda, If, Begin, Case, MakeRewriter, Cond, Let, Else, Quasiquote)

    object Quote : Primitive(
        name = "quote",
        definitionEnv = Environment.Kind.NULL_ENV,
        keyword = Primitive.KEYWORD,
        minArgs = 1,
        maxArgs = 1,
        comment = "Returns argument without evaluation",
        documentation = "(quote (+ 1 2)) => (+ 1 2)",
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return arg1
        }
    }

    object Quasiquote : Primitive(
        name = "quasiquote",
        definitionEnv = Environment.Kind.NULL_ENV,
        keyword = Primitive.KEYWORD,
        minArgs = 1, maxArgs = 1,
        comment = "",
        documentation = null
    )

    object Define : Primitive(
        name = "define",
        definitionEnv = Environment.Kind.NULL_ENV,
        keyword = Primitive.KEYWORD,
        minArgs = 2,
        maxArgs = -1,
        comment = "TBD",
        documentation = "TBD",
    ) {
        override fun applyN(args: List, environment: Environment, cont: Continuation): Entity {
//            try {
                val iterator = ListIterator(args)
                val target = iterator.next()
                val value = iterator.next()
                if (target is Symbol) {
                    if (iterator.hasNext()) {
                        throw Exception("$this: Too many arguments $args")
                    }
                    environment.define(target, Undefined.VALUE)
                    cont.begin(ExpressionAction(value, environment))
                        .andThen(AssignmentAction(target, environment))
                    return Void.VALUE
                } else if (target is Pair) {
                    val rtarget = (target as List).car
                    val params = (target as List).cdr
                    val body = Pair(value, iterator.rest())
                    if (rtarget is Symbol) {
                        environment.define(rtarget, Undefined.VALUE)
                        cont.begin(AssignmentAction(rtarget, environment))
                        return Closure(params, body, environment)
                    } else {
                        throw Exception("$this: Invalid procedure name $rtarget")
                    }
                } else {
                    throw Exception("$this: Invalid form $args")
                }
            /*} catch (e: Exception) {
                throw Exception("Invalid args $args")
            }*/
        }
    }

    object Lambda : Primitive(
        name = "lambda",
        definitionEnv = Environment.Kind.NULL_ENV,
        keyword = Primitive.KEYWORD,
        minArgs = 2,
        maxArgs = -1,
        comment = "Creates anonymous procedure",
        documentation = "(lambda (x) (+ x 1))",
    ) {
        override fun applyN(args: List, environment: Environment, cont: Continuation): Entity {
            try {
                val params = args.car
                val body = args.cdr as Pair
                return Closure(params, body, environment)
            } catch (e: Exception) {
                throw Exception("$this: invalid procedure definition")
            }
        }
    }

    object If : Primitive(
        name = "if",
        definitionEnv = Environment.Kind.NULL_ENV,
        keyword = KEYWORD,
        minArgs = 2,
        maxArgs = 3,
        comment = "Conditional evaluation",
        documentation = "(if (eqv? 1 0) 'yes 'no)"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            val alternate = Void.VALUE
            return apply3(arg1, arg2, alternate, environment, cont)
        }

        override fun apply3(
            arg1: Entity, // condition
            arg2: Entity, // if true
            arg3: Entity, // else
            environment: Environment,
            cont: Continuation
        ): Entity {
            cont.begin(ExpressionAction(arg1, environment))
                .andThen(IfAction(arg2, arg3, environment))
            return Void.VALUE
        }
    }

    object `Set!` : Primitive(
        name = "set!",
        definitionEnv = Environment.Kind.NULL_ENV,
        keyword = KEYWORD,
        minArgs = 2,
        maxArgs = 2,
        comment = "Assigns a variable",
        documentation = "Variable must be defined"
    ) {
        override fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
            if (arg1 is Symbol) {
                cont.begin(ExpressionAction(arg2, environment, null))
                    .andThen(AssignmentAction(arg1, environment, null))
                return Void.VALUE
            } else {
                throw Exception("$this: argument not a symbol $arg1")
            }
        }
    }

    object Begin : Primitive(
        name = "begin",
        definitionEnv = Environment.Kind.NULL_ENV,
        keyword = KEYWORD,
        minArgs = 0,
        maxArgs = -1,
        comment = "Step-by-step execution",
        documentation = "(begin (step1) (step2)"
    ) {
        override fun applyN(args: List, environment: Environment, cont: Continuation): Entity {
            cont.addCommandSequenceActions(args, environment)
            return Void.VALUE
        }
    }

    object Case : Primitive(
        name = "case",
        definitionEnv = Environment.Kind.NULL_ENV,
        keyword = KEYWORD,
        minArgs = 0,
        maxArgs = -1,
        comment = "",
        documentation = "(case (expr) ((variant1) expr1) ((variant2) expr2) ...)"
    ) {
        override fun applyN(args: List, environment: Environment, cont: Continuation): Entity {
            TODO("Not implemented yet")
        }
    }

    object Let : Primitive(
        name = "let",
        definitionEnv = Environment.Kind.NULL_ENV,
        keyword = KEYWORD,
        minArgs = 1,
        maxArgs = -1,
        comment = "Introduce bindings, e.g. (let ((x 2) (y 3)) (* x y))",
        documentation = null
    )

    object Cond : Primitive(
        name = "cond",
        definitionEnv = Environment.Kind.NULL_ENV,
        keyword = KEYWORD,
        minArgs = 0,
        maxArgs = -1,
        comment = "",
        documentation = "(cond ((variant1) expr1) ((variant2) expr2) ...)"
    )

    object Else : Primitive(
        name = "else",
        definitionEnv = Environment.Kind.NULL_ENV,
        keyword = Primitive.KEYWORD,
        minArgs = 0,
        maxArgs = -1,
        comment = null,
        documentation = null
    )

    object MakeRewriter : Primitive(
        name = "make-rewriter",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = IDENTIFIER,
        minArgs = 1,
        maxArgs =  1,
        comment = "Makes a syntax rewriter, e.g. (make-rewriter (lambda (exp) ...))",
        documentation = null
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            // evaluate argument: must be a function of exactly one argument
            // obj = obj.eval(env, cont);
            return if (arg1 !is Closure) {
                throw Exception("$this argument must be a function taking one argument $arg1")
            } else {
                if (arg1.getMaxArity() != 1) {
                    throw Exception("$this argument must be a function taking one argument $arg1")
                }
                SyntaxRewriter(arg1)
            }
        }
    }

    // TODO: do, ! let*, unquote, unquote-splicing, rewrite1
}
