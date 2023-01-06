package ru.bmstu.kscheme.lang.action

import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.base.Action
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.type.KSBoolean

class IfAction(
    private val consequent: Entity,
    private val alternate: Entity,
    env: Environment,
    next: Action?
) : Action(env, next) {

    constructor(consequent: Entity, alternate: Entity, env: Environment)
            : this(consequent, alternate, env, null)

    override fun invoke(arg: Entity, cont: Continuation): Entity? {
        cont.head = next
        return if (arg != KSBoolean.FALSE) {
            consequent.eval(env ?: throw Exception("Null env"), cont)
        } else {
            alternate.eval(env ?: throw Exception("Null env"), cont)
        }
    }
}
