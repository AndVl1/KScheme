package ru.bmstu.kscheme.lang.base

import ru.bmstu.kscheme.lang.base.Entity

interface List : Entity, Iterable<Entity?> {
    var car: Entity
    var cdr: Entity
}
