package ru.bmstu.kscheme.lang.io

import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.util.Logger
import java.io.Closeable
import java.io.PrintWriter

class InputPort : Port, Closeable {
    constructor(fileName: String) {
        this.fileName = fileName
    }
    constructor(reader: java.io.Reader) {
        fileName = null
        this.reader = reader
        kSReader = Reader(reader)
    }

    private var fileName: String? = null
    private var reader: java.io.Reader? = null
    override val isOpen: Boolean
        get() = kSReader != null
    override val kind: Kind
        get() = Kind.TEXT

    @Transient
    private var kSReader: Reader? = null

    override fun write(out: PrintWriter) {
        out.print("#<input-port>")
    }

    override fun close() {
        kSReader = null
        reader?.close()
        reader = null
    }

    fun read(): Entity {
        if (isOpen) {
            return kSReader?.read() ?: Eof.VALUE
        } else {
            throw Exception("InputPost closed")
        }
    }

    fun loadForEval(env: Environment, cont: Continuation) {
        var obj = this.read()
        while (obj != Eof.VALUE) {
            Logger.log(Logger.Level.DEBUG, "loadForEval read $obj")
            Interpreter.addForEval(obj, env, cont)
            obj = this.read()
        }
    }

    companion object {

    }
}