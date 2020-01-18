import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import kotlin.RuntimeException

/**
 * Checks through the list of tokens for syntax and converts it into 🆘🥐 code.
 *
 * @author Jonathan Metcalf and Martin Bleakley
 *
 * @property tokens the list of tokens to check
 * @property outputPath the path to write the compiled 🆘🥐 code to
 */
class Parser constructor(private val tokens: List<Token>, private val outputPath: String) {

    private val writer: BufferedWriter = BufferedWriter(FileWriter(File(outputPath)))
    private var text = ""
    private var index = 0

    fun parse() {
        println(tokens)

        while (index < tokens.size) {
            // If it's a function, check the syntax
            if (tokens[index].tokenType == TokenType.FUNC) {
                funcNameChecker()
                funcBodyChecker()
            }

            index++
        }

        // Writes text to the file and closes
        writer.write(text)
        writer.close()
    }

    /**
     * Checks whether a function name is correct or not.
     *
     * @author Jonathan Metcalf
     *
     */
    private fun funcNameChecker() {
        // If the next character after function is not a space, then throw an error
        if (tokens[index + 1].tokenType != TokenType.SPACE)
            throw RuntimeException("Expected ${TokenType.SPACE} but found ${tokens[index + 1].tokenType}!")

        // If it's the main function, add func main to text
        index += 2
        if (tokens[index].tokenType == TokenType.MAIN) {
            text += "func main\n"
        } else {
            text += "func ${tokens[index].unicode}"

            // Does parameters
            index++
            for (param in findParams(index)) {
                text += " $param"
            }
        }

        // Moves index to next line
        while (tokens[index].tokenType != TokenType.NEW_LINE) {
            index++
        }
        index++
    }

    /**
     * Returns a list of the params between two parentheses.
     *
     * @author Jonathan Metcalf
     *
     * @param start starting index of the tokens to look through (inclusive)
     * @return the list of the params
     */
    private fun findParams(start: Int): List<String> {
        val list = mutableListOf<String>()
        var currentIndex = start
        while (tokens[currentIndex].tokenType != TokenType.RIGHT_PARENTHESES) {
            // If it's unknown, it's a parameter to add
            if (tokens[currentIndex].tokenType == TokenType.UNKNOWN)
                list.add(tokens[currentIndex].unicode)

            // Parentheses are incorrect if this is true
            if (tokens[currentIndex].tokenType == TokenType.BRACE || tokens[currentIndex].tokenType == TokenType.NEW_LINE)
                throw RuntimeException("Expected ${TokenType.RIGHT_PARENTHESES} but found a ${tokens[currentIndex].tokenType} instead!")
            currentIndex++
        }

        return list
    }

    private fun funcBodyChecker() {

    }
}