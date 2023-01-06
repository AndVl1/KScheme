package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.base.List
import ru.bmstu.kscheme.lang.type.Undefined
import java.io.Serializable

class ArgumentList : Serializable {
    private var listArg: ArrayList<Entity>? = null
    private var pairArgs: List? = null
    constructor() {
        listArg = arrayListOf()
        pairArgs = null
    }
    constructor(args: List) {
        this.pairArgs = args
        listArg = null
    }

    fun set(index: Int, value: Entity) {
        assert(listArg != null) {
            "set called on immutable ArgumentList"
        }
        ensureSize(listArg!!, index + 1)
        listArg?.set(index, value)
    }

    fun getArgs(): List {
        return listArg?.let { k2ks(it) } ?: pairArgs ?: EmptyList.VALUE
    }

    fun k2ks(lst: kotlin.collections.List<Entity>?): List {
        if (lst.isNullOrEmpty()) {
            return EmptyList.VALUE
        }
        var p: List = EmptyList.VALUE
        for (i in lst.size - 1 downTo 0) {
            p = Pair(lst[i], p)
        }
        return p
    }

    private fun ensureSize(listArgs: ArrayList<Entity>, size: Int) {
        val missing = size - listArgs.size
        listArgs.ensureCapacity(size)
        for (i in 0 until missing) {
            listArg?.add(Undefined.VALUE)
        }
    }
}
