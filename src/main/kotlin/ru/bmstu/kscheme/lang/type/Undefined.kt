package ru.bmstu.kscheme.lang.type

import ru.bmstu.kscheme.lang.base.AbstractEntity
import java.io.PrintWriter

class Undefined : AbstractEntity() {
    override fun write(out: PrintWriter) {
        out.write("#<undefined>")
    }

    companion object {
        val VALUE = Undefined()
    }
}
