package ru.bmstu.kscheme.lang

import java.io.PrintWriter

class SystemEnvironment(
    parent: Environment?,
    private val kind: Environment.Kind
) : Environment(parent) {

    constructor(kind: Environment.Kind) : this(null, kind)

    override fun write(out: PrintWriter) {
        out.write("<system-environment: $kind>")
    }
}
