package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.action.EvalAction
import ru.bmstu.kscheme.lang.action.ExpressionAction
import ru.bmstu.kscheme.lang.action.ObtainArgumentAction
import ru.bmstu.kscheme.lang.action.ProcedureCallAction
import ru.bmstu.kscheme.lang.base.AbstractEntity
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.base.List
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.Symbol
import ru.bmstu.kscheme.util.Logger
import java.io.PrintWriter


class Pair(
    override var car: Entity,
    override var cdr: Entity
) : AbstractEntity(), List {

    private var analyzed = false

    override fun analyze(env: Environment): Entity {
        if (!analyzed) {
            if (car is Symbol && System.isSpecialForm(car as Symbol, env)) {
                /* we have a special form, so let's
                 * perform syntax analysis
                 * -- may change car, cdr
                 */
                System.analyzeSpecialForm(this, env)
                analyzed = true
                return this
            }
            /* we have a procedure application
             * first car, then cdr
             */
            car = car.analyze(env)

            /* now process rest of the list (i.e. the form arguments)
             *
             * we can't simply do: cdr = cdr.analyze()
             * but we must traverse the cdr list ourselves at this
             * level; otherwise (f1 f2 f3) would be analyzed as
             * (f1 (f2 (f3)), which is wrong.
             */
            var rest = cdr
            var restParent: List = this
            while (rest != EmptyList.VALUE) {
                if (rest is List) {
                    val restAsPair = rest
                    restParent = restAsPair
                    restAsPair.car = restAsPair.car.analyze(env)
                    rest = restAsPair.cdr
                } else {
                    Logger.log(Logger.Level.INFO, "check for correctness")
                    restParent.cdr = rest.analyze(env)
                    break
                }
            }
            analyzed = true
        }
        return this
    }

    override fun eval(env: Environment, continuation: Continuation): Entity? {
        val iterator = ListIterator(this)
        val operator = iterator.next()
        if (operator is Symbol) {
            val e = env.lookup(operator)
            if (e is SyntaxRewriter) {
                rewriteAndEval(e, ArgumentList(cdr as List), env, continuation)
                return null
            } else if (e is SyntaxProcedure) {
                continuation.begin(ExpressionAction(operator, env))
                    .andThen(ProcedureCallAction(ArgumentList(cdr as List), env))
                return null
            }
        } else if (operator is Location) {
            val e = operator.value
            if (e is SyntaxRewriter) {
                rewriteAndEval(e, ArgumentList(), env, continuation)
                return null
            }
        }
        val argList = ArgumentList()
        var action = continuation.beginSequence()

        var argIndex = 0
        while (iterator.hasNext()) {
            val nextArg = iterator.next()
            action = action
                .andThen(ExpressionAction(nextArg, env))
                .andThen(ObtainArgumentAction(argList, argIndex++, env))
        }
        action = action
            .andThen(ExpressionAction(operator, env))
            .andThen(ProcedureCallAction(argList, env))

        continuation.endSequence()
        return null
    }

    override fun write(out: PrintWriter) {
        when {
            car == Symbol.QUOTE && cdr !is EmptyList && cdr is Pair && (cdr as Pair).cdr is EmptyList -> {
                out.print("'")
                (cdr as Pair).car.write(out)
            }
            car == Symbol.QUASIQUOTE && cdr !is EmptyList && cdr is Pair && (cdr as Pair).cdr is EmptyList -> {
                out.print("`")
                (cdr as Pair).car.write(out)
            }
            car == Symbol.UNQUOTE && cdr !is EmptyList && cdr is Pair && (cdr as Pair).cdr is EmptyList -> {
                out.print(",@")
                (cdr as Pair).car.write(out)
            }
            else -> {
                var current = this
                out.print("(")
                car.write(out)
                while (current.cdr is Pair && current.cdr !is EmptyList) {
                    current = current.cdr as Pair
                    out.print(" ")
                    if (current.car == null) {
                        out.print("ERROR")
                        Logger.log(Logger.Level.WARNING, "null car $current")
                    } else {
                        current.car.write(out)
                    }
                }
                if (current.cdr !is EmptyList) {
                    out.print(" . ")
                    current.cdr.write(out)
                }
                out.print(")")
            }
        }
    }

    override fun iterator(): Iterator<Entity?> = ListIterator(this)

    private fun rewriteAndEval(sr: SyntaxRewriter, args: ArgumentList, env: Environment, cont: Continuation) {
        cont.begin(ExpressionAction(sr, env))
            .andThen(ProcedureCallAction(args, env))
            .andThen(EvalAction(env))
        args.set(0, this)
    }

    companion object {
        private const val SERIAL_VERSION_UID = 1L
    }
}
