import ru.bmstu.kscheme.lang.Interpreter
import ru.bmstu.kscheme.lang.io.InputPort
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.test.BeforeTest
import kotlin.test.Test

class ArythmTest {
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
        val expr = "(/ 1000 (* 5 (+ 2 (- (+ 15 (- 5)) 7))))".byteInputStream()
        val res = "40"
        run(expr, res)
    }

    @Test
    fun testMore() {
        val expr = "(> 1 2)".byteInputStream()
        val res = "#f"
        run (expr, res)
    }

    @Test
    fun testMore2() {
        val expr = "(> 125 1.5)".byteInputStream()
        val res = "#t"
        run (expr, res)
    }

    @Test
    fun testLess() {
        val expr = "(< 1 2)".byteInputStream()
        val res = "#t"
        run (expr, res)
    }

    @Test
    fun testLess2() {
        val expr = "(< 125 1.5)".byteInputStream()
        val res = "#f"
        run (expr, res)
    }

    private fun run(expr: InputStream, expectedRes: String) {
        val test = InputPort(InputStreamReader(expr))
        val testResult = interpreter.loadForTest(test, interpreter.sessionEnv!!)
        assert(expectedRes == testResult)
    }
}
