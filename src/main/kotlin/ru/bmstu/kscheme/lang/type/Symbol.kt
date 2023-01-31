package ru.bmstu.kscheme.lang.type

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.base.AbstractEntity
import ru.bmstu.kscheme.lang.base.Entity
import java.io.PrintWriter

class Symbol(
    val value: String,
    val interned: Boolean
) : AbstractEntity() {

    constructor(value: String): this(value, false)

    override fun eval(env: Environment, continuation: Continuation): Entity {
        return env.lookup(this)
    }

    override fun toString(): String {
        return value
    }

    override fun write(out: PrintWriter) {
        out.write(value)
    }

    companion object {
        val symtable = HashMap<String, Symbol>(512)
        val AND = makeSymbol("and")
        val APPEND = makeSymbol("append")
        val ARROW = makeSymbol("=>")
        val BEGIN = makeSymbol("begin")
        val CALL_CC = makeSymbol("call/cc")
        val CALL_WITH_CURRENT_CONTINUATION = makeSymbol("call-with-current-continuation")
        val CASE = makeSymbol("case")
        val COND = makeSymbol("cond")
        val CONS = makeSymbol("cons")
        val DEFINE = makeSymbol("define")
        val DELAY = makeSymbol("delay") // TODO implement delay / force
        val DO = makeSymbol("do")
        val ELSE = makeSymbol("else")
        val ERROBJ = makeSymbol("__errobj")
        val HELP = makeSymbol("help")
        val IF = makeSymbol("if")
        val LAMBDA = makeSymbol("lambda")
        val LET = makeSymbol("let")
        val LETREC = makeSymbol("letrec")
        val LETSTAR = makeSymbol("let*")
        val LIST = makeSymbol("list")
        val OR = makeSymbol("or")
        val QUASIQUOTE = makeSymbol("quasiquote")
        val QUOTE = makeSymbol("quote")
        val SET = makeSymbol("set!")
        val UNQUOTE = makeSymbol("unquote")
        val UNQUOTE_SPLICING = makeSymbol("unquote-splicing")

        fun makeSymbol(string: String): Symbol {
            return symtable.computeIfAbsent(string) { s -> Symbol(s) }
        }

        fun makeUninternedSymbol(s: String): Symbol = Symbol(s, false)
    }
}