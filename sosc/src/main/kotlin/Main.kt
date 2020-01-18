import java.io.*

/**
 * The entry point into the program. The first argument should be
 * the file written in sos that they want compiled.
 *
 * @author Jonathan Metcalf
 *
 * @param args the arguments to the function
 */
fun main(args: Array<String>) {
    Parser(args[0], args[1]).parse()
}

/**
 * Parses a sos file and compiles it.
 *
 * @author Jonathan Metcalf and Martin Bleakley
 *
 * @property inputPath the path of the file to be compiled
 * @property outputPath the path of the compiled file
 */
class Parser constructor(private val inputPath: String, private val outputPath: String) {

    private val reader: BufferedReader = BufferedReader(FileReader(inputPath))
    private val writer: BufferedWriter = BufferedWriter(FileWriter(outputPath))
    private val regex: Regex = Regex("[ ]+")

    /**
     * Parses through every line of the program to be compiled.
     *
     * @author Jonathan Metcalf and Martin Bleakley
     *
     */
    fun parse() {
        // Convert to characters
        var file = ""
        reader.lines().forEach { file += "$it\n" }
        val characters: MutableList<String> = regex.replace(file.replace("\n", " \\n "), " ").split(" ") as MutableList<String>

        val tokens = mutableListOf<Token>()
        for ()

        testSyntax(tokens)
    }

    private fun testSyntax(tokens: List<Token>) {

    }
}