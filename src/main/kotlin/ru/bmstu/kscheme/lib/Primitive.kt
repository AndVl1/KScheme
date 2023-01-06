package ru.bmstu.kscheme.lib

import ru.bmstu.kscheme.lang.procedure.Continuation
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.base.List
import java.io.Serializable
import java.lang.StringBuilder

abstract class Primitive(
    val name: String,
    val definitionEnv: Environment.Kind,
    val keyword: Boolean,
    val minArgs: Int,
    val maxArgs: Int,
    comment: String?,
    documentation: String?,
) : Serializable {
    val comment: String = comment ?: "No documentation defined"
    val documentation: String = "${this.comment}\n${documentation ?: ""}"

    open fun apply0(environment: Environment, cont: Continuation): Entity {
        throw Exception("Not implemented")
    }

    open fun apply1(arg1: Entity, environment: Environment, cont: Continuation): Entity {
        throw Exception("Not implemented")
    }

    open fun apply2(arg1: Entity, arg2: Entity, environment: Environment, cont: Continuation): Entity {
        throw Exception("Not implemented")
    }

    open fun apply3(arg1: Entity, arg2: Entity, arg3: Entity, environment: Environment, cont: Continuation): Entity {
        throw Exception("Not implemented")
    }

    open fun applyN(args: List, environment: Environment, cont: Continuation): Entity {
        throw Exception("Not implemented")
    }

    override fun toString(): String {
        val sb = StringBuilder().apply {
            append(name)
            append(":")
            append(minArgs)
            if (minArgs != maxArgs) {
                if (maxArgs < 0) {
                    append("..*")
                } else {
                    append("..")
                    append(maxArgs)
                }
            }
        }
        return sb.toString()
    }

    companion object {
        const val KEYWORD = true
        const val IDENTIFIER = false
    }
}
