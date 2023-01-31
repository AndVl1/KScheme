package ru.bmstu.kscheme.lang.io

import ru.bmstu.kscheme.lang.base.AbstractEntity
import java.io.IOException
import kotlin.jvm.Throws

abstract class Port : AbstractEntity() {
    abstract val isOpen: Boolean
    abstract val kind: Kind
    @Throws(IOException::class)
    abstract fun close()

    enum class Kind {
        TEXT, BINARY
    }

    companion object {
        private const val SERIAL_VERSION_UID = 1L

    }
}
