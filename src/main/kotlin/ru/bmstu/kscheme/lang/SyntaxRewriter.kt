package ru.bmstu.kscheme.lang

import ru.bmstu.kscheme.lang.base.SyntaxObject
import ru.bmstu.kscheme.lang.procedure.Closure
import java.io.PrintWriter

class SyntaxRewriter(
    rewriter: Closure
) : Closure(rewriter.param, rewriter.body, rewriter.definitionEnv), SyntaxObject {
    override fun write(out: PrintWriter) {
        out.write("#<syntax-rewriter>")
        out.write(">")
    }
}
