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
    private val tokens: MutableList<Token> = mutableListOf()

    /**
     * Parses through every line of the program to be compiled.
     *
     * @author Jonathan Metcalf and Martin Bleakley
     *
     */
    fun parse() {
        tokenize()
        testSyntax()
    }

    private fun tokenize() {
        // Read file to string
        var file = ""
        reader.lines().forEach { file += "$it \\n " }
        file = regex.replace(file, " ")

        println(file)

        // Tokenize it
        var i = 0
        here@while (i < file.length) {
            // Check every keyword
            for (special in TokenType.values()) {
                if (i >= file.length) break
                if (special.unicode != null && file.substring(i).startsWith(special.unicode)) {
                        tokens.add(Token(special, special.unicode))
                        i += if (special == TokenType.NEW_LINE) 3 else special.unicode.length
                        continue@here
                }
            }

            // If it gets here, then it is an unknown variable and we need to detect it.
            val current = file.substring(i)

            for (x in 0..current.length) {
                for (special in TokenType.values()) {
                    if (special.unicode != null && current.substring(x).startsWith(special.unicode)) {
                        val name = current.substring(0, x)
                        tokens.add(Token(TokenType.UNKNOWN, name))
                        i += name.length
                        continue@here
                    }
                }
            }
        }

        println(tokens)
    }


    /**
     * Tests the syntax of the string to confirm there are no errors.
     * If there are, then throw up an error.
     */
    private fun testSyntax() {

    }
}