package ru.bmstu.kscheme.lang.base

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.Environment
import java.io.PrintWriter
import java.io.Serializable

interface Entity : Serializable {

    fun eval(env: Environment, continuation: Continuation): Entity?

    fun analyze(env: Environment): Entity

    fun optimize(env: Environment): Entity

    fun write(out: PrintWriter)

    fun display(out: PrintWriter)

    fun toWriteFormat(): String

    override fun toString(): String
}
