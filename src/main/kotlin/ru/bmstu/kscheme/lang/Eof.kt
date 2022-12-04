package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.base.AbstractEntity
import java.io.PrintWriter

class Eof : AbstractEntity() {

    override fun write(out: PrintWriter) {

    }

    companion object {
        val VALUE = Eof()
    }
}
