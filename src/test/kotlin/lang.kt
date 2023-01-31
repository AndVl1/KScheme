import ru.bmstu.kscheme.lang.Interpreter
import ru.bmstu.kscheme.lang.io.InputPort
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.test.BeforeTest
import kotlin.test.Test

class LangTests {
    lateinit var interpreter: Interpreter

    @BeforeTest
    fun init() {
        interpreter = Interpreter.newInterpreter()
    }

    @Test
    fun quote() {
        val expr = "(quote (+ 1 2))".byteInputStream()
        val res = "(+ 1 2)"
        run(expr, res)
    }

    @Test
    fun quote2() {
        val expr = "'(+ 1 2)".byteInputStream()
        val res = "(+ 1 2)"
        run(expr, res)
    }

    @Test
    fun eval() {
        val expr = "(eval (quote (+ 1 2)))".byteInputStream()
        val res = "3"
        run(expr, res)
    }

    @Test
    fun eval2() {
        val expr = "(eval '(+ 1 2))".byteInputStream()
        val res = "3"
        run(expr, res)
    }

    @Test
    fun booleanNotFalse() {
        val expr = "(not #f)".byteInputStream()
        val res = "#t"
        run(expr, res)
    }

    @Test
    fun booleanNotTrue() {
        val expr = "(not #t)".byteInputStream()
        val res = "#f"
        run(expr, res)
    }

    @Test
    fun defineAndLambda() {
        val expr = """(define inc (lambda (x) (+ x 1))) 
            |(define value 3)
            |(inc value)""".trimMargin().byteInputStream()
        val res = "4"
        run(expr, res)
    }

    @Test
    fun setValue() {
        val expr = """(define inc (lambda (x) (+ x 1))) 
            |(define value 3)
            |(set! value (inc value))
            |value""".trimMargin().byteInputStream()
        val res = "4"
        run(expr, res)
    }

    @Test
    fun tailRecursion() {
        val expr = """(define x #f)
            |(define (loop z)
            |  (if (eqv? z 0)
            |      'done
            |      (begin
            |        (set! x z)
            |        (loop (- z 1)))))
            |(loop 2000000)
        """.trimMargin().byteInputStream()
        val res = "done"
        run(expr, res)
    }

    @Test
    fun `if`() {
        val expr = "(if (> 5 2) 'true 'false)".byteInputStream()
        val res = "true"
        run(expr, res)
    }

    @Test
    fun if2() {
        val expr = "(if (< 5 2) 'true 'false)".byteInputStream()
        val res = "false"
        run(expr, res)
    }

    @Test
    fun cond1() {
        val expr = "(cond ((> 5 2) 'true) ((< 5 2) 'false))".byteInputStream()
        val res = "true"
        run(expr, res)
    }

    @Test
    fun cond2() {
        val expr = "(cond ((< 5 2) 'less) ((> 5 2) 'more))".byteInputStream()
        val res = "more"
        run(expr, res)
    }

    private fun run(expr: InputStream, expectedRes: String) {
        val test = InputPort(InputStreamReader(expr))
        val testResult = interpreter.loadForTest(test, interpreter.sessionEnv!!)
        assert(expectedRes == testResult)
    }
}
