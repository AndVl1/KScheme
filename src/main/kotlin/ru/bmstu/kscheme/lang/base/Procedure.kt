package ru.bmstu.kscheme.lang.base

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.Environment

abstract class Procedure : AbstractEntity() {
    abstract fun apply(arg: List, env: Environment, cont: Continuation): Entity?
}
