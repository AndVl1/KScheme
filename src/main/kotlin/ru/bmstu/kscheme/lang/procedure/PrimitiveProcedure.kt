package ru.bmstu.kscheme.lang.procedure

import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.ListIterator
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.base.List
import ru.bmstu.kscheme.lang.base.Procedure
import ru.bmstu.kscheme.lang.type.Void
import ru.bmstu.kscheme.lib.Primitive
import java.io.PrintWriter

open class PrimitiveProcedure(
    val value: Primitive
) : Procedure() {

    override fun apply(arg: List, env: Environment, cont: Continuation): Entity? {
        if (value.maxArgs < 0 || value.maxArgs > 3) {
            if (value.minArgs >= 0 || value.maxArgs >= 0) {
                checkNumArgs(arg)
            }
            return value.applyN(arg, env, cont)
        }
        // ok, 0 <= maxArgs <= 3 : special rules
        // ok, 0 <= maxArgs <= 3 : special rules
        val argArray = arrayOf<Entity?>(null, null, null)
        var countedArgs = 0
        val it = ListIterator(arg)
        while (it.hasNext()) {
            argArray[countedArgs++] = it.next()
            if (countedArgs > value.maxArgs) {
                throw Exception("$value too many arguments $arg")
            }
        }
        if (countedArgs < value.minArgs) {
            throw Exception("$value too few arguments $arg")
        }
        return when (value.maxArgs) {
            0 -> value.apply0(env, cont)
            1 -> value.apply1(argArray[0] ?: Void.VALUE, env, cont)
            2 -> {
                value.apply2(argArray[0]!!, argArray[1] ?: Void.VALUE, env, cont)
            }
            else -> value.apply3(argArray[0]!!, argArray[1]!!, argArray[2]!!, env, cont)
        }
    }

    override fun write(out: PrintWriter) {
        out.write("#<primitive-procedure ${value}>")
    }

    private fun checkNumArgs(args: List) {
        val iterator = ListIterator(args)
        var i = 0
        while (i < value.minArgs) {
            if (!iterator.hasNext()) {
                throw Exception("$value too low args $args")
            }
            iterator.next()
            i++
        }
        if (value.maxArgs > 0) {
            while (iterator.hasNext()) {
                if (++i > value.maxArgs) {
                    throw Exception("$value too many args $args")
                }
            }
        }
    }
}