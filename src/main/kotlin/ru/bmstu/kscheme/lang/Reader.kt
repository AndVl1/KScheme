package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.type.*
import ru.bmstu.kscheme.util.Logger
import java.io.IOException
import java.io.Reader
import java.io.StreamTokenizer
import java.math.BigDecimal

class Reader(reader: Reader) {

    private val tokenizer = StreamTokenizer(reader)
    init {
        tokenizer.resetSyntax()
        tokenizer.lowerCaseMode(false)
        tokenizer.slashStarComments(false)
        tokenizer.commentChar(';'.code)
        tokenizer.quoteChar('\"'.code)
        tokenizer.whitespaceChars('\u0000'.code, '\u0020'.code)
        tokenizer.eolIsSignificant(false)
        tokenizer.wordChars('A'.code,'Z'.code) // A-Z
        tokenizer.wordChars('a'.code,'z'.code) // a-z
        tokenizer.wordChars('0'.code,'9'.code) // 0-9
        tokenizer.wordChars('\u00A1'.code,'\u00FF'.code) // Unicode latin-1 supplement, symbols and letters
        tokenizer.wordChars('*'.code,'.'.code) // '*', '+', ',', '-', '.'
        tokenizer.wordChars('!'.code,'!'.code) // '!'
        tokenizer.wordChars('#'.code, '#'.code)
        tokenizer.wordChars('\\'.code,'\\'.code) // '\'
        tokenizer.wordChars('/'.code,'/'.code) // '/'
        tokenizer.wordChars('$'.code,'$'.code) // '$'
        tokenizer.wordChars('_'.code,'_'.code) // '_'
        tokenizer.wordChars('<'.code,'@'.code) // '<', '=', '>', '?', '@'
    }

    fun read(): Entity? {
        try {
            var o: Entity? = null
            if (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                tokenizer.pushBack()
                o = readObject()
            }
            return o
        } catch (e: IOException) {
            throw Exception("IO", e)
        }
    }

    private fun readObject(): Entity? {
        val token = readToken() ?: throw Exception("Unexpected end")
        return readObject(token)
    }

    private fun readObject(token: String): Entity? {
        if (token.isEmpty()) {
            return readObject()
        }
        when (token) {
            "(" -> {
                return readList(Pair(car = EmptyList.VALUE, cdr = EmptyList.VALUE))
            }
            "'" -> { return Pair(car = Symbol.QUOTE, Pair(car = readObject() ?: return null, cdr = EmptyList.VALUE)) }
            "`" -> { return Pair(car = Symbol.QUASIQUOTE, Pair(car = readObject() ?: return null, cdr = EmptyList.VALUE)) }
            ")" -> { throw Exception("Unexpected \")\"") }
            else -> { return readOthers(token) }
        }
    }
    
    private fun readList(l: Pair): Entity? {
        var t: String?
        var first = true
        var seendot = false

        var ins = l // which cons are we inserting stuff into?

        while (true) {
            t = readToken()
            if (t == null) {
                throw Exception("read: unterminated list")
            }
            if (t == ")") {
                return if (first) {
                    EmptyList.VALUE
                } else {
                    l
                }
            } else if (t == ".") {
                seendot = if (!seendot) {
                    if (first) {
                        throw Exception("read: at least one datum must precede \".\"")
                    } else {
                        ins.cdr = readObject() ?: return null
                        true
                    }
                } else {
                    throw Exception("read: more than one \".\" in a list")
                }
            } else if (!seendot) {
                tokenizer.pushBack()
                if (first) {
                    first = false
                    ins.car = readObject() ?: return null
                } else {
                    val nextcons = Pair(readObject() ?: return null, EmptyList.VALUE)
                    ins.cdr = nextcons
                    ins = nextcons
                }
            } else {
                throw Exception("read: missing \")\"")
            }
        }
    }

    private fun readOthers(t: String): Entity? {
        if (".+-0123456789".indexOf(t[0]) >= 0
            && t != "+"
            && t != "-"
            && t != "..."
        ) {
            try {
                logReadOthers(t, "number")
                return Real(BigDecimal(t))
            } catch (e: NumberFormatException) {
                throw Exception("Invalid number $t")
            }
        } else if (t.startsWith(",@")) {
            return Pair(
                car = Symbol.UNQUOTE_SPLICING,
                cdr = Pair(
                    car = readObject(t.substring(2)) ?: return null ,
                    cdr = EmptyList.VALUE
                )
            )
        } else if (t.startsWith(",")) {
            return Pair(
                car = Symbol.UNQUOTE,
                cdr = Pair(
                    car = readObject(t.substring(1)) ?: return null ,
                    cdr = EmptyList.VALUE
                )
            )
        } else if (t.startsWith("\"")) {
            logReadOthers(t, "string")
            return MutableString(t.substring(1))
        } else if (t.equals("#f", true)) {
            return KSBoolean.FALSE
        } else if (t.equals("#t", true)) {
            return KSBoolean.TRUE
        } else if (t.startsWith("#\\")) {
            val charString = t.substring(2)
            return if (charString.equals("space", true)) {
                Character(' ')
            } else if (charString.equals("newline", true)) {
                Character('\n')
            } else if (charString.length == 1) {
                Character(charString[0])
            } else {
                throw Exception("Invalid character")
            }
        } else {
            logReadOthers(t, "symbol")
            return Symbol.makeSymbol(t)
        }
    }

    private fun readToken(): String? {
        val c = tokenizer.nextToken()
        return when (tokenizer.ttype) {
            StreamTokenizer.TT_EOF -> null
            StreamTokenizer.TT_NUMBER -> tokenizer.nval.toString()
            StreamTokenizer.TT_WORD -> tokenizer.sval
            '\"'.code -> "\"${tokenizer.sval}"
            else -> c.toChar().toString()
        }.also {
            if (it != null) {
                Logger.log(Logger.Level.DEBUG, "TOKEN=$it")
            }
        }
    }

    private fun logReadOthers(token: String, type: String) {
        Logger.log(Logger.Level.DEBUG, "readOthers: $token as a $type")
    }
}
