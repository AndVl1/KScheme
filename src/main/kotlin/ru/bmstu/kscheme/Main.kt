import ru.bmstu.kscheme.Consts
import ru.bmstu.kscheme.lang.*
import ru.bmstu.kscheme.lang.base.Entity
import ru.bmstu.kscheme.lang.io.OutputPort
import ru.bmstu.kscheme.lang.type.Void
import ru.bmstu.kscheme.util.Logger

fun main(args: Array<String>) {

    /*val parser = ArgParser("example")
    val input by parser.option(ArgType.String, shortName = "i", description = "Input file").required()
    val output by parser.option(ArgType.String, shortName = "o", description = "Output file name")
    parser.parse(args)

    val inpText = File(input).readText()
    println(inpText)
    val interpreter = Interpreter()
    interpreter.evaluate(inpText)*/

    val intp = bootstrap() ?: throw Exception("Error during bootstrapping")
    val session = intp.sessionEnv ?: throw Exception("Session null")
    val outputPort = intp.cout ?: throw Exception("cout null")

    var obj : Entity? = Void.VALUE

    do {
        try {
            prompt(outputPort, Consts.PROMPT)
            obj = readEntity(intp)
            if (obj != null) {
                val result = intp.eval(obj, session)
                if (result != Void.VALUE) {
                    outputPort.write(result)
                }
                outputPort.newline()
            }
        } catch (e: Exception) {
            Logger.warning(e)
            intp.clearContinuation()
        }
    } while (obj != null)
}

private fun bootstrap(): Interpreter? {
    return try {
        Logger.log(Logger.Level.DEBUG, "Bootstrapping")
        Interpreter.newInterpreter().also {
            Logger.log(Logger.Level.DEBUG, "...bootstrapped")
        }
    } catch (e: Exception) {
        Logger.warning(e)
        null
    }
}

private fun prompt(w: OutputPort, prompt: String) {
    if (w.isConsole) {
        w.print(prompt)
        w.flush()
    }
}

private fun readEntity(intp: Interpreter): Entity? {
    var obj = intp.cin?.read() ?: throw Exception("cin null")

    // check for EOF
    if (obj == Eof.VALUE || obj == Consts.QUIT) {
        return null
    }

    return obj
}
