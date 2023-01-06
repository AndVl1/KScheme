package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.base.AbstractEntity
import ru.bmstu.kscheme.lang.type.Symbol
import ru.bmstu.kscheme.util.Logger
import java.io.PrintWriter
import java.lang.Exception

class KtAnyObject : AbstractEntity {
    var value: Any? = null
        private set

    constructor(any: Any? = null) {
        this.value = any
    }

    constructor(s: Symbol) {
        val className = s.toString()
        var obj: Any? = null
        try {
            obj = Class.forName(className).getConstructor().newInstance()
        } catch (e: Exception) {
            Logger.warning(e)
        } finally {
            this.value = obj
        }
    }

    override fun write(out: PrintWriter) {
        out.println(this)
    }

    override fun toString(): String {
        return "$value"
    }

    companion object {
        private const val SERIAL_VERSION_UID = 1L
    }
}
