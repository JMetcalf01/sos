import java.io.*

/**
 * The entry point into the program. The first argument should be
 * the file written in 🆘 that they want compiled.
 *
 * @author Jonathan Metcalf
 *
 * @param args the arguments to the function
 */
fun main(args: Array<String>) {
    Tokenizer(args[0], args[1]).run()
}

/**
 * Parses a sos file and compiles it.
 *
 * @author Jonathan Metcalf
 *
 * @property inputPath the path of the file to be compiled
 * @property outputPath the path to write the compiled 🆘🥐 code to
 */
class Tokenizer constructor(private val inputPath: String, private val outputPath: String) {

    private val reader: BufferedReader = BufferedReader(FileReader(inputPath))
    private val regex: Regex = Regex("[ ]+")
    private val tokens: MutableList<Token> = mutableListOf()
    private val writer = BufferedWriter(FileWriter(outputPath))

    /**
     * Parses through every line of the program to be compiled.
     *
     * @author Jonathan Metcalf
     */
    fun run() {
        tokenize()
        val compiled = "${Parser().parseFile(tokens)}\n"
        writer.write(if (compiled == "null\n") throw Exception("Compile failed!") else compiled)
        writer.close()
    }

    /**
     * Breaks up the file into a string of tokens to be parsed and checked for syntax.
     *
     * @author Jonathan Metcalf
     *
     */
    private fun tokenize() {
        // Read file to string
        var file = ""
        reader.lines().forEach { file += "$it \\n " }
        file = regex.replace(file, " ")

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

            // If it gets here, then it is an unknown variable or a raw and we need to detect it.
            val current = file.substring(i)
            for (x in 0..current.length) {
                // Detects if it's a raw
                if (current.substring(0, current.indexOf(TokenType.SPACE.unicode!!)).matches(Regex(TokenType.RAW.unicode!!))) {
                    val value = current.substring(0, current.indexOf(TokenType.SPACE.unicode))
                    tokens.add(Token(TokenType.RAW, value))
                    i += value.length
                    continue@here
                }
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
    }
}