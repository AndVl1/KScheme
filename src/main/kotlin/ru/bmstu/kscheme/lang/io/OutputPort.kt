package ru.bmstu.kscheme.lang.io

import ru.bmstu.kscheme.lang.EmptyList
import ru.bmstu.kscheme.lang.Pair
import ru.bmstu.kscheme.lang.base.Entity
import java.io.*
import kotlin.Exception

class OutputPort(printWriter: PrintWriter, val isConsole: Boolean) : Port(), Closeable, Flushable {

    @Transient
    var out: PrintWriter?
        private set
    private val fileName: String?

    constructor(out: PrintStream, isConsole: Boolean): this(PrintWriter(out), isConsole)
    constructor(fileName: String) : this(PrintWriter(FileOutputStream(fileName), true), false)
    init {
        this.fileName = null
        out = printWriter
    }

    override val isOpen: Boolean
        get() = out != null
    override val kind: Kind
        get() = Kind.TEXT

    @Throws(IOException::class)
    override fun close() {
        out?.close()
        out = null
    }

    fun write(obj: Entity) {
        if (isOpen) {
            obj.write(out ?: throw Exception("OutPort null"))
        }
    }

    fun newline() {
        out?.println()
        flush()
    }

    override fun flush() {
        out?.flush()
        if (isConsole) {
            java.lang.System.console().flush()
        }
    }

    override fun write(out: PrintWriter) {
        out.println("#<output-port>")
    }

    fun print(s: String) {
        out?.print(s)
    }

    fun printf(format: String, vararg args: Any) {
        out?.printf(format, args)
    }

    fun display(obj: Entity) {
        when (obj) {
            EmptyList.VALUE -> out?.println("()")
            is Pair -> {
                out?.print("(")
                obj.display(out ?: throw Exception("Out null"))
                out?.print(")")
            }
            else -> obj.display(out ?: throw Exception("Out null"))
        }
    }


    private fun openFile(fileName: String) {
        val stream = FileOutputStream(fileName)
        out = PrintWriter(stream, true)
    }

    fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
    }

    private fun readObject(`in`: ObjectInputStream) {
        `in`.defaultReadObject()
        if (this.fileName != null) {
            openFile(this.fileName)
        } else {
            out = null
        }
    }

}
