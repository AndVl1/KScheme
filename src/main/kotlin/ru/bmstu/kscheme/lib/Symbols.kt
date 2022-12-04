package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.KSBoolean
import ru.bmstu.kscheme.lang.type.Symbol
import java.util.concurrent.atomic.AtomicInteger

object Symbols {
    val primitives = arrayOf(SymbolQ, GenSymbol)

    private val genCount = AtomicInteger(0)

    object SymbolQ : Primitive(
        name = "symbol?",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "Returns true if arg is symbol, else false",
        documentation = "(symbol? 'sym) => #t",
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            return KSBoolean.makeKSBoolean(arg1 is Symbol)
        }
    }

    object GenSymbol : Primitive(
        name = "generate-symbol",
        definitionEnv = Environment.Kind.INTERACTION_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 0,
        maxArgs = 0,
        comment = "Makes a new symbol",
        documentation = "(generate-symbol)",
    ) {
        override fun apply0(environment: Environment, cont: Continuation): Entity {
            return Symbol.makeUninternedSymbol("__S${genCount.getAndIncrement()}")
        }
    }
}
