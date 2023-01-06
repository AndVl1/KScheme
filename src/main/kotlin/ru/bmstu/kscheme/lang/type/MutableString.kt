package ru.bmstu.kscheme.lang.type

import ru.bmstu.kscheme.lang.base.AbstractEntity
import java.io.PrintWriter
import java.lang.StringBuilder

class MutableString(s: String) : AbstractEntity() {

    val value = StringBuilder(s)

    override fun write(out: PrintWriter) {
        out.print("\"")
        for (i in value.indices) {
            when (value[i]) {
                '\t' -> out.print("\\t")
                '\r' -> out.print("\\r")
                '\n' -> out.print("\\n")
                '\\' -> out.print("\\\\")
                '"' -> out.print("\\\"")
                else -> out.print(value[i])
            }
        }
        out.print("\"")
    }

    override fun display(out: PrintWriter) {
        out.print(value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MutableString

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value.toString()
    }

}