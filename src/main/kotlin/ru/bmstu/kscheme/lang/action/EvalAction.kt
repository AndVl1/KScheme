package ru.bmstu.kscheme.lang.action

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.base.Action
import ru.bmstu.kscheme.lang.base.Entity

class EvalAction(
    env: Environment,
    next: Action?
) : Action(env, next) {
    constructor(env: Environment) : this(env, null)

    override fun invoke(arg: Entity, cont: Continuation): Entity? {
        cont.head = next
        trace({ out -> out.print("${arg.toWriteFormat()}\n") }, env)
        return arg.eval(env ?: return null, cont)
    }
}