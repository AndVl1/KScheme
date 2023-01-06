package ru.bmstu.kscheme.lang.type

import ru.bmstu.kscheme.lang.base.AbstractEntity
import java.io.PrintWriter

class KSBoolean(val value: Boolean) : AbstractEntity() {

    override fun write(out: PrintWriter) {
        out.print(if (value) "#t" else "#f")
    }

    companion object {
        val TRUE = KSBoolean(true)
        val FALSE = KSBoolean(false)
        fun makeKSBoolean(b: Boolean) = if (b) TRUE else FALSE
    }
}
