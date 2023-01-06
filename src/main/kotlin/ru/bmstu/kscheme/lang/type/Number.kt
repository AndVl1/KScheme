package ru.bmstu.kscheme.lang.type

import ru.bmstu.kscheme.lang.base.AbstractEntity
import java.math.BigDecimal

abstract class Number : AbstractEntity() {
    abstract val value: BigDecimal
}