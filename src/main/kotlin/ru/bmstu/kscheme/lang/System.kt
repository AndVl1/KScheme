package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.base.List
import ru.bmstu.kscheme.lang.type.Symbol

class System {
    companion object {
        fun isSpecialForm(symbol: Symbol, env: Environment): Boolean {
            return false
        }

        fun analyzeSpecialForm(form: List, env: Environment) {}
    }
}