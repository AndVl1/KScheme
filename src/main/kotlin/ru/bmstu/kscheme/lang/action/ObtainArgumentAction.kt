package ru.bmstu.kscheme.lang.action

import ru.bmstu.kscheme.lang.ArgumentList
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.base.Action
import ru.bmstu.kscheme.lang.base.Entity

class ObtainArgumentAction(
    private val argList: ArgumentList,
    private val argumentIndex: Int,
    env: Environment,
    next: Action? = null
) : Action(env, next) {

    override fun invoke(arg: Entity, cont: Continuation): Entity? {
        cont.head = next
        argList.set(argumentIndex, arg)
        trace({ out -> out.print("") }, env)
        return arg
    }
}
