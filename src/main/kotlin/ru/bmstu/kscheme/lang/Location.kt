package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.base.AbstractEntity
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.procedure.Continuation
import java.io.PrintWriter

class Location(var value: Entity): AbstractEntity() {

    override fun eval(env: Environment, continuation: Continuation): Entity {
        return value
    }

    override fun write(out: PrintWriter) {
        out.write("#<location of ")
        value.write(out)
        out.write(">")
    }

    companion object {
        private const val SERIAL_VERSION_UID = 1L
    }
}
