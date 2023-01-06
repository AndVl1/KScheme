package ru.bmstu.kscheme.lang.type

import java.io.PrintWriter
import java.math.BigDecimal
import java.util.Objects

class Real(
    override val value: BigDecimal
) : Number() {
    constructor(value: Double) : this(value.toBigDecimal())
    constructor(value: Int): this(value.toBigDecimal())

    override fun equals(other: Any?): Boolean {
        return other is Real && other.value == this.value
    }

    override fun hashCode(): Int {
        return Objects.hashCode(value)
    }

    override fun write(out: PrintWriter) {
        out.print(value)
    }
}