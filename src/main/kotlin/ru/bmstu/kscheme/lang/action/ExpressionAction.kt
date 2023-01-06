package ru.bmstu.kscheme.lang.action

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.base.Action
import ru.bmstu.kscheme.lang.base.Entity

class ExpressionAction(
    private val expr: Entity,
    env: Environment,
    next: Action?
) : Action(env, next) {
    constructor(value: Entity, env: Environment) : this(value, env, null)

    override fun invoke(arg: Entity, cont: Continuation): Entity? {
        cont.head = next
        trace({ port -> port.printf("${expr.toWriteFormat()}\n") }, env)
        return expr.eval(env ?: return null, cont)
    }
}