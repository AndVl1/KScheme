package ru.bmstu.kscheme.lang.action

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.type.Symbol
import ru.bmstu.kscheme.lang.type.Void
import ru.bmstu.kscheme.lang.base.Action
import ru.bmstu.kscheme.lang.base.Entity

class AssignmentAction(
    val symbol: Symbol,
    env: Environment,
    next: Action?,
) : Action(env, next) {

    constructor(symbol: Symbol, env: Environment) : this(symbol, env, null)

    override fun invoke(arg: Entity, cont: Continuation): Entity? {
        cont.head = next
        env?.getLocation(symbol)!!.value = arg
        trace({ out -> out.print("${symbol.toWriteFormat()} <- ${arg.toWriteFormat()}\n") }, env)
        return Void.VALUE
    }
}