package ru.bmstu.kscheme.lang.base

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.Environment
import java.io.PrintWriter
import java.io.StringWriter

abstract class AbstractEntity : Entity {
    override fun eval(env: Environment, continuation: Continuation): Entity? = this

    override fun analyze(env: Environment): Entity = this

    override fun optimize(env: Environment): Entity = this

    override fun display(out: PrintWriter) {
        write(out)
    }

    override fun toWriteFormat(): String {
        val sw = StringWriter()
        display(PrintWriter(sw))
        return sw.toString()
    }

    override fun toString(): String {
        val sw = StringWriter()
        display(PrintWriter(sw))
        return sw.toString()
    }
}
