import ru.bmstu.kscheme.lang.Environment
import ru.bmstu.kscheme.lang.Interpreter
import ru.bmstu.kscheme.lang.io.InputPort
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.test.BeforeTest
import kotlin.test.Test

class ArythmText {
    lateinit var interpreter: Interpreter

    @BeforeTest
    fun init() {
        interpreter = Interpreter.newInterpreter()
    }

    @Test
    fun testSum() {
        val expr = "(+ 1 2)".byteInputStream()
        val res = "3"
        run(expr, res)
    }

    @Test
    fun testMul() {
        val expr = "(* 10 2)".byteInputStream()
        val res = "20"
        run(expr, res)
    }

    @Test
    fun testSub() {
        val expr = "(- 10 2)".byteInputStream()
        val res = "8"
        run(expr, res)
    }

    @Test
    fun testDiv() {
        val expr = "(/ 125 5)".byteInputStream()
        val res = "25"
        run(expr, res)
    }

    @Test
    fun testLongExpr() {
        val expr = "(/ 1000 (* 5 (+ 2 (- 10 7))))".byteInputStream()
        val res = "40"
        run(expr, res)
    }

    private fun run(expr: InputStream, expectedRes: String) {
        val test = InputPort(InputStreamReader(expr))
        val testResult = interpreter.loadForTest(test, interpreter.sessionEnv!!)
        assert(expectedRes == testResult)
    }
}
