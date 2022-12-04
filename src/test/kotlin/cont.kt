import kotlin.test.BeforeTest
import ru.bmstu.kscheme.lang.Interpreter
import ru.bmstu.kscheme.lang.io.InputPort
import java.io.InputStreamReader
import kotlin.test.Test

class Continuation {
    lateinit var interpreter: Interpreter

    @BeforeTest
    fun init() {
        interpreter = Interpreter.newInterpreter()
        val preExpr = """
            |(define param #f)
            |(define con1 #f)
        """.trimMargin().byteInputStream()
        val preTest = InputPort(InputStreamReader(preExpr))
        interpreter.loadForTest(preTest, interpreter.sessionEnv!!)
    }

    @Test
    fun testDisplay() {
        val expr = """
            |((call/cc (lambda (x) (set! con1 x) (if param car cdr))) (list 1 2 3 4))
        """.trimMargin().byteInputStream()
        val expResult = "(2 3 4)"
        val test = InputPort(InputStreamReader(expr))
        val testResult = interpreter.loadForTest(test, interpreter.sessionEnv!!)
        assert(expResult == testResult)
    }
}
