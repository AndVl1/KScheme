package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.base.List

class ListIterator(private var pair: List, private val allowImproper: Boolean) : Iterator<Entity?> {
    private var isImproper = false
    private var restPair: List? = null

    constructor(pair: List) : this(pair, false) {}

    override fun hasNext(): Boolean {
        return pair !== EmptyList.VALUE
    }

    override fun next(): Entity {
        var retVal: Entity? = null
        try {
            if (isImproper) {
                retVal = pair.cdr
                pair = EmptyList.VALUE
                if (!allowImproper) {
                    throw (NoSuchElementException("improper list").initCause(Exception("incorrect list $retVal")) as NoSuchElementException)
                }
            } else {
                retVal = pair.car
                restPair = pair
                if (pair.cdr is List) {
                    pair = pair.cdr as List
                } else {
                    isImproper = true
                }
            }
        } catch (e: Exception) {
            throw (NoSuchElementException().initCause(e) as NoSuchElementException)
        }
        if (retVal == null) {
            throw NoSuchElementException("null element")
        }
        return retVal
    }

    /**
     * Replaces current value.
     * Must be called after next().
     */
    fun replace(newArg: Entity?) {
        if (newArg == null) {
            throw Exception("Unexpected null")
        }
        if (restPair == null) {
            throw Exception(
                "No current value to replace $pair"
            )
        }
        if (isImproper && pair === EmptyList.VALUE) {
            restPair?.cdr = newArg
        } else {
            restPair?.car = newArg
        }
    }


    /**
     * Returns the remaining portion of the list as a Pair.
     */
    fun rest(): Entity {
        return pair
    }
}
