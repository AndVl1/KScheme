package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.base.AbstractEntity
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.io.InputPort
import ru.bmstu.kscheme.lang.io.OutputPort
import ru.bmstu.kscheme.lang.type.Symbol
import java.io.PrintWriter

open class Environment : AbstractEntity {

    val interpreter: Interpreter?
        get() {
            val j = this.lookup(Interpreter.INTP_SYMBOL) as KtAnyObject
            return j.value as? Interpreter
        }

    var `in`: InputPort? = null
    var out: OutputPort? = null
    var parent: Environment? = null

    constructor(`in`: InputPort, out: OutputPort) {
        this.`in` = `in`
        this.out = out
    }
    constructor(parent: Environment?) {
        this.parent = parent
        if (parent == null) {
            this.`in` = null
            this.out = null
        } else {
            this.`in` = parent.`in`
            this.out = parent.out
        }
    }

    /** Association function: symbol --> location */
    val assoc = HashMap<Symbol, Location>()

    override fun write(out: PrintWriter) {
        out.write("#<environment>")
    }

    @Synchronized
    fun define(s: Symbol, entity: Entity) {
        if (assoc[s] != null) {
            assoc[s]?.value = entity
        } else {
            assoc[s] = Location(entity)
        }
    }

    fun lookup(s: Symbol): Entity = getLocation(s).value

    fun getLocation(s: Symbol): Location {
        return getNullableLocation(s) ?: throw Exception("Unbound $s")
    }

    private fun getNullableLocation(s: Symbol): Location? {
        var loc: Location?
        var e: Environment? = this
        while (e != null) {
            loc = e.assoc[s]
            if (loc == null) {
                e = e.parent
            } else {
                return loc
            }
        }
        return null
    }

    enum class Kind {
        NULL_ENV, REPORT_ENV, INTERACTION_ENV
    }

    companion object {
        private const val SERIAL_VERSION_UID = 1L

        fun newEnvironment(`in`: InputPort, out: OutputPort): Environment {
            return Environment(`in`, out)
        }
    }
}
