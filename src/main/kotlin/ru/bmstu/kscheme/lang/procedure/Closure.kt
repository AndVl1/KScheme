package ru.bmstu.kscheme.lang.procedure

import ru.bmstu.kscheme.lang.EmptyList
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.Pair
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.base.List
import ru.bmstu.kscheme.lang.base.Procedure
import ru.bmstu.kscheme.lang.type.Symbol
import ru.bmstu.kscheme.util.Logger
import java.io.PrintWriter

open class Closure(
    val param: Entity?,
    val body: List,
    val definitionEnv: Environment?,
) : Procedure() {

    /**
     * Applies this closure.
     */
    override fun apply(arg: List, env: Environment, cont: Continuation): Entity? {
        var args = arg
        val localenv = Environment(definitionEnv)
        var currparam = param
        var prev: List? = null
        var dotparam = false

        /* bind actual arguments to formals (long)
         */try {
            // for each passed arg
            while (args !== EmptyList.VALUE) {
                // get next already-evaluated arg
                val obj: Entity = args.car
                if (!dotparam) {
                    if (currparam === EmptyList.VALUE) {
                        throw Exception("apply: too many arguments $this")
                    } else if (currparam is Pair) {
                        val p: Entity = currparam.car
                        if (p is Symbol) {
                            localenv.define(p, obj)
                        } else {
                            Logger.warning(Logger.Level.WARNING,  "apply: param is not a symbol")
                        }
                        // next param, please
                        currparam = currparam.cdr
                    } else if (currparam is Symbol) {
                        prev = Pair(obj, EmptyList.VALUE)
                        localenv.define((currparam as Symbol?)!!, prev)
                        dotparam = true
                    } else {
                        throw Exception("apply: invalid formal parameter $currparam")
                    }
                } else {
                    // accumulate argument
                    prev?.cdr = Pair(obj, EmptyList.VALUE)
                    prev = prev?.cdr as List?
                }
                // next argument, please
                args = args.cdr as List
            }
        } catch (e: ClassCastException) {
            throw Exception("apply: improper list $currparam")
        }
        if (currparam is Symbol && !dotparam) {
            // special case:
            // a "." notation parameter taking the empty list
            localenv.define((currparam as Symbol?)!!, EmptyList.VALUE)
        } else if (currparam !== EmptyList.VALUE && !dotparam) {
            throw Exception("apply: too few arguments $this")
        }

        /* we have bound params, let's eval body
         * by adding to the continuation
         */
        cont.addCommandSequenceActions(body, localenv)
        return null
    }

    /**
     * Writes a Closure
     */
    override fun write(out: PrintWriter) {
        out.write("#<procedure")
        if (Logger.levelValue < Logger.Level.INFO.value) {
            out.write(" ")
            Pair(Symbol.LAMBDA, Pair(param ?: throw Exception(), body)).write(out)
        }
        out.write(">")
    }

    fun getMaxArity(): Int {
        var count = 0
        var currParam = param
        while (currParam is Pair) {
            count++
            currParam = (currParam as List).cdr
        }
        return if (currParam is Symbol) {
            -1
        } else count
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}