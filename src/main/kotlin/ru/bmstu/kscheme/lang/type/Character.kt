package ru.bmstu.kscheme.lang.type

import ru.bmstu.kscheme.lang.base.AbstractEntity
import java.io.PrintWriter

class Character(
    val value : Char
) : AbstractEntity() {

    override fun write(out: PrintWriter) {
        out.print(
            when (value) {
                '\n' -> "#\\newline"
                ' ' -> "#\\space"
                else -> "#\\$value"
            }
        )
    }

    override fun display(out: PrintWriter) {
        out.print(value)
    }
}
