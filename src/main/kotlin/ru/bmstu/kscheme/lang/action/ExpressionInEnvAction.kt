package ru.bmstu.kscheme.lang.action

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.base.Action
import ru.bmstu.kscheme.lang.base.Entity

class ExpressionInEnvAction(
    private var expr: Entity,
    env: Environment,
    next: Action?
) : Action(env, next) {

    constructor(expr: Entity, env: Environment) : this(expr, env, null)

    override fun invoke(arg: Entity, cont: Continuation): Entity? {
        cont.head = next
        if (arg !is Environment) {
            throw Exception("Not an environment $arg")
        }
        val evalEnv = arg
        expr = expr.analyze(evalEnv).optimize(evalEnv)
        trace({ out -> out.print("${expr.toWriteFormat()}\n") }, env)
        return expr.eval(evalEnv, cont)
    }
}