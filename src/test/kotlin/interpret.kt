import ru.bmstu.kscheme.lang.Interpreter
import ru.bmstu.kscheme.lang.io.InputPort
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.test.BeforeTest
import kotlin.test.Test

class SchemeInterpretTest {
    lateinit var interpreter: Interpreter

    @BeforeTest
    fun init() {
        interpreter = Interpreter.newInterpreter()
        val inputPort = InputPort(
            BufferedReader(
                InputStreamReader(
                    javaClass.getResourceAsStream("/scheme/scheme-interpreter.scm") ?: throw Exception("file null")
                )
            )
        )
        interpreter.loadForTest(inputPort, interpreter.sessionEnv!!)
    }

    @Test
    fun runTest() {
        val expr = "(if (null? '(a b c)) 'a 'b)"
        run(expr)
    }

    @Test
    fun runTest2() {
        val expr = "(* (+ 2 3) (- 10 2))"
        run(expr)
    }

    @Test
    fun runTest3() {
        val expr = "(car (cdr (quote (a b c))))"
        run(expr)
    }

    @Test
    fun runTest4() {
        val expr = "(cons (+ 1 7) '(b c d))"
        run(expr)
    }

    @Test
    fun runTest5() {
        val expr = "(list (+ 3 9) (* 5 6))"
        run(expr)
    }

    private fun run(expr: String) {
        val expected = expr.byteInputStream()
        val real = "(evaluate '$expr)".byteInputStream()
        val expectedResult = interpreter.loadForTest(InputPort(InputStreamReader(expected)), interpreter.sessionEnv!!)
        val realResult = interpreter.loadForTest(InputPort(InputStreamReader(real)), interpreter.sessionEnv!!)
        assert(expectedResult == realResult)
    }
}
