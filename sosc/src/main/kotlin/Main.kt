import java.nio.file.Files
import java.nio.file.Paths

/**
 * The entry point into the program. The first argument should be
 * the file written in 🆘 that they want compiled.
 *
 * @author Jonathan Metcalf
 *
 * @param args the arguments to the function
 */
fun main(args: Array<String>) {
    Tokenizer().run(args)
}

/**
 * Parses a sos file and compiles it.
 *
 * @author Jonathan Metcalf
 */
class Tokenizer {

    /**
     * Parses through the list of files to parse and parses each individual one
     *
     * @author Jonathan Metcalf
     *
     * @param args should either be nothing or the path to the file containing the list of files to compile and their output paths
     */
    fun run(args: Array<String>) {
        // Defaults to SOS unless user overrides with args
        // Reads the file and skips to the beginning of the parsing
        val reader = Files.newBufferedReader(Paths.get(if (args.isEmpty()) "sos" else args[0]))
        var file = ""
        reader.lines().forEach { file += "$it\n" }

        // Get input and output directories
        val inputDirectory =
            if (file.indexOf("INPUT DIRECTORY:") == -1) "sossource"
            else file.substring(file.indexOf("INPUT DIRECTORY:") + "INPUT DIRECTORY:".length + 1, file.nextIndexOf("\n", file.indexOf("INPUT DIRECTORY:") + "INPUT DIRECTORY:".length + 1))
        val outputDirectory =
            if (file.indexOf("OUTPUT DIRECTORY:") == -1) "sosbuild"
            else file.substring(file.indexOf("OUTPUT DIRECTORY:") + "OUTPUT DIRECTORY:".length + 1, file.nextIndexOf("\n", file.indexOf("OUTPUT DIRECTORY:") + "OUTPUT DIRECTORY:".length + 1))

        // Compile every file in the list
        val lines = (file.substring(file.indexOf("FILES:") + "FILES:".length + 1).split("\n") as MutableList<String>).filter { it != "" }
        for (index in lines.indices) {
            var fileName = lines[index]
            if (!lines[index].endsWith(".🆘")) fileName += ".🆘"
            compileFile("$inputDirectory\\$fileName", "$outputDirectory\\$fileName🥐")
        }
    }

    /**
     * Parses through every line of the file to be compiled.
     *
     * @author Jonathan Metcalf
     *
     * @param inputPath the path of the file to be compiled
     * @param outputPath the path to write the compiled 🆘🥐 code to
     */
    private fun compileFile(inputPath: String, outputPath: String) {
        val tokens = tokenize(inputPath)
        compile(tokens as MutableList<Token>, outputPath)
    }

    /**
     * Breaks up the file into a string of tokens to be parsed and checked for syntax.
     *
     * @author Jonathan Metcalf
     *
     * @param inputPath the input path of the file to tokenize
     * @return the list of tokens generated
     */
    private fun tokenize(inputPath: String): List<Token> {
        // Read file to string
        val reader = Files.newBufferedReader(Paths.get(inputPath))
        var file = ""
        reader.lines().forEach { file += "$it \\n " }
        file = Regex("[ ]+").replace(file, " ")

        // Tokenize it
        var i = 0
        val tokens: MutableList<Token> = mutableListOf()
        here@ while (i < file.length) {
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
                if (current.substring(
                        0,
                        current.indexOf(TokenType.SPACE.unicode!!)
                    ).matches(Regex(TokenType.RAW.unicode!!))
                ) {
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
        return tokens
    }

    /**
     * Compiles a list of tokens and writes it to a file.
     *
     * @author Jonathan Metcalf
     *
     * @param tokens the list of tokens to be compiled
     * @param outputPath the output path to write to
     */
    private fun compile(tokens: MutableList<Token>, outputPath: String) {
        val compiled = "${Parser().parseFile(tokens)}\n"
        val writer = Files.newBufferedWriter(Paths.get(outputPath))
        writer.use {
            if (compiled == "null\n") throw Exception("Compile failed!") else it.write(compiled)
        }
    }
}

/**
 * Returns the next index of a string given the starting index and the string to look for
 *
 * @author Jonathan Metcalf
 *
 * @param test the string testing for
 * @param startIndex the starting index to start looking
 * @return the index of the tested string, or -1 if not found
 */
fun String.nextIndexOf(test: String, startIndex: Int = 0): Int {
    for (index in startIndex until length) {
        if (this.substring(index).startsWith(test)) return index
    }
    return -1
}