package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.base.SyntaxObject
import ru.bmstu.kscheme.lang.procedure.PrimitiveProcedure
import ru.bmstu.kscheme.lib.Primitive
import java.io.PrintWriter

class SyntaxProcedure(p: Primitive) : PrimitiveProcedure(p), SyntaxObject {
    override fun write(out: PrintWriter) {
        out.write("#<syntax-procedure ${value.name}>")
    }
}
