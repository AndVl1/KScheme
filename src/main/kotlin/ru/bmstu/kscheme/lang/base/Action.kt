package ru.bmstu.kscheme.lang.base

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.io.OutputPort
import java.io.Serializable

abstract class Action(
    val env: Environment?,
    var next: Action?,
) : Serializable {

    fun andThen(action: Action): Action {
        action.next = this.next
        this.next = action
        return action
    }

    abstract fun invoke(arg: Entity, cont: Continuation): Entity?

    protected fun interface Printer {
        fun print(port: OutputPort)
    }

    protected fun trace(doo: Printer, env: Environment?) {
        if (env?.interpreter?.traceEnabled == true) {
            val cout = env.out ?: return
            val actionName = this.javaClass.simpleName.replace("Action", "")
            cout.printf("%s ", actionName)
            doo.print(cout)
        }
    }
}
