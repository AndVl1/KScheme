package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.io.InputPort
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.MutableString
import ru.bmstu.kscheme.lang.type.Void

object SystemInterface {
    val primitives: Array<Primitive> = arrayOf(Load)

    object Load : Primitive(
        name = "load",
        definitionEnv = Environment.Kind.REPORT_ENV,
        keyword = Primitive.IDENTIFIER,
        minArgs = 1,
        maxArgs = 1,
        comment = "Loads and executes source file",
        documentation = null,
    ) {
        override fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
            val fileName = arg1 as? MutableString ?: throw Exception("$this: Argument not a string $arg1")
            try {
                val inPort = InputPort(fileName.toString())
                inPort.loadForEval(environment, cont)
                return Void.VALUE
            } catch (e: Exception) {
                throw Exception("$this: File not found $arg1")
            }
        }
    }
}