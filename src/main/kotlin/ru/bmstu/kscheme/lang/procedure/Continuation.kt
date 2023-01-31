package ru.bmstu.kscheme.lang.procedure

import ru.bmstu.kscheme.lang.EmptyList
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.action.ExpressionAction
import ru.bmstu.kscheme.lang.base.Action
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.base.List
import ru.bmstu.kscheme.lang.base.Procedure
import java.io.PrintWriter

class Continuation: Procedure {

    var head: Action? = null

    constructor()
    constructor(other: Continuation) {
        head = other.head
    }

    override fun apply(arg: List, env: Environment, cont: Continuation): Entity {
        if (arg != EmptyList.VALUE) {
            if (arg.cdr == EmptyList.VALUE) {
                cont.replaceContinuation(this)
                return arg.car
            } else {
                throw Exception("continuation: too many args $arg")
            }
        } else {
            throw Exception("continuation: too few args $arg")
        }
    }

    override fun write(out: PrintWriter) {
        out.write("#<continuation")
    }

    fun begin(action: Action): Action {
        action.next = this.head
        this.head = action
        return action
    }

    fun beginSequence(): Action {
        return begin(DUMMY_ACTION)
    }

    fun endSequence() {
        if (this.head == DUMMY_ACTION) {
            this.head = this.head?.next
        }
    }

    fun addCommandSequenceActions(body: Iterable<Entity?>, env: Environment) {
        var currAction = beginSequence()
        for (expr in body) {
            currAction = currAction.andThen(ExpressionAction(expr ?: throw Exception("expr null"), env))
        }
        endSequence()
    }

    fun clear() {
        this.head = null
    }

    private fun replaceContinuation(cont: Continuation) {
        this.head = cont.head
    }

    companion object {
        private const val SERIAL_VERSION_UID = 1L

        private val DUMMY_ACTION = object : Action(null, null) {
            override fun invoke(arg: Entity, cont: Continuation): Entity? {
                throw Exception("Dummy action invoked")
            }
        }
    }
}