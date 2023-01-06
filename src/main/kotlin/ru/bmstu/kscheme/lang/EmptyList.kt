package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.base.AbstractEntity
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.base.List
import java.io.PrintWriter

class EmptyList : AbstractEntity(), List {
    companion object {
        val VALUE = EmptyList()
    }

    override var car: Entity
        get() {
            throw Exception("car: invalid arguments $this")
        }
        set(value) {}

    override var cdr: Entity
        get() {
            throw Exception("cdr: invalid arguments $this")
        }
        set(value) {}

    override fun write(out: PrintWriter) {
        out.print("()")
    }

    override fun iterator(): Iterator<Entity?> = ListIterator(this)
}