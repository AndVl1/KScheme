package ru.bmstu.kscheme.lang.type

import ru.bmstu.kscheme.lang.base.AbstractEntity
import java.io.PrintWriter

class Void : AbstractEntity() {

    override fun write(out: PrintWriter) {
        out.print("#<void>")
    }

    private fun readResolve(): Any = VALUE

    companion object {
        val VALUE = Void()
    }
}
