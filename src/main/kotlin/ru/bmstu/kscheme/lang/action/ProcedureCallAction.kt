package ru.bmstu.kscheme.lang.action

import ru.bmstu.kscheme.lang.ArgumentList
import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.base.Procedure
import ru.bmstu.kscheme.lang.base.Action
import ru.bmstu.kscheme.lang.base.Entity

class ProcedureCallAction(
    private val argList: ArgumentList,
    env: Environment,
    next: Action?
) : Action(env, next) {
    constructor(argList: ArgumentList, env: Environment) : this(argList, env, null)

    override fun invoke(arg: Entity, cont: Continuation): Entity? {
        cont.head = next
        val operator: Procedure = arg as? Procedure ?: throw Exception("Operator not a procedure")
        trace({ out -> out.print("${arg.toWriteFormat()}\n") }, env)
        return operator.apply(argList.getArgs(), env!!, cont)
    }
}
