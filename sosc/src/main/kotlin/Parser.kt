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

            when (tokens[index].tokenType) {
                TokenType.FUNC -> {
                    funcNameChecker()
                    funcBodyChecker()
                    text += "\n"
                }

            }

            index++
        }

        // Writes text to the file and closes
        text += "\n"
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
            findFuncParams()
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
     * @return the list of the params
     */
    private fun findFuncParams() {
        while (tokens[index].tokenType != TokenType.RIGHT_PARENTHESES) {
            // If it's unknown, it's a parameter to add
            if (tokens[index].tokenType == TokenType.UNKNOWN)
                text += " ${tokens[index].unicode}"

            // Finding string
            if (tokens[index].tokenType == TokenType.QUOTE_MARK)
                getString()

            // Parentheses are incorrect if this is true
            if (tokens[index].tokenType == TokenType.BRACE || tokens[index].tokenType == TokenType.NEW_LINE)
                throw RuntimeException("Expected ${TokenType.RIGHT_PARENTHESES} but found a ${tokens[index].tokenType} instead!")

            index++
        }
    }

    /**
     * Reads the body of a function and parses it.
     *
     * @author Jonathan Metcalf
     */
    private fun funcBodyChecker() {
        val start = index
        when (tokens[index].tokenType) {
            TokenType.UNKNOWN -> {
                index++
                findCalledParams()
                text += "call ${tokens[start].unicode}\n"
            }

            TokenType.VAR -> {
                storeVariable()
            }

            TokenType.IF -> {

            }

            TokenType.ELSE -> {

            }

            TokenType.WHILE -> {

            }
        }
    }

    /**
     * Returns a string from the starting quotemark to
     * the next quotemark.
     *
     * @return the string (minus the quotemarks)
     */
    private fun getString() {
        index++
        val start = index
        while (tokens[index].tokenType != TokenType.QUOTE_MARK) {
            index++
        }

        for (i in start until index) {
            text += tokens[i].unicode
        }

        index++
    }

    //find all params, figure out if variable or literal (loadr if literal, load if variable)
    private fun findCalledParams() {
        while (tokens[index].tokenType != TokenType.RIGHT_PARENTHESES) {
            // If it's unknown, it's a parameter to add
            if (tokens[index].tokenType == TokenType.UNKNOWN) {
                text += "load ${tokens[index].unicode}\n"

            }

            // Finding string
            if (tokens[index].tokenType == TokenType.QUOTE_MARK) {
                index++
                text += "loadr "
                getString()
                text += "\n"
            }

            // Parentheses are incorrect if this is true
            if (tokens[index].tokenType == TokenType.BRACE || tokens[index].tokenType == TokenType.NEW_LINE)
                throw RuntimeException("Expected ${TokenType.RIGHT_PARENTHESES} but found a ${tokens[index].tokenType} instead!")

            index++
        }
    }

    private fun storeVariable() {
        index++
        if (tokens[index].tokenType != TokenType.SPACE)
            throw RuntimeException("Expected ${TokenType.SPACE} but found ${tokens[index].tokenType}!")

        index++
        text += "store ${tokens[index].unicode} "
        index++
        findValue()
    }

    private fun findValue() {
        while (tokens[index].tokenType == TokenType.SPACE) {
            index++
        }

        if (tokens[index].tokenType != TokenType.EQUALS)
            throw RuntimeException("Expected ${TokenType.EQUALS} but found ${tokens[index].tokenType}!")

        index++
        while (tokens[index].tokenType == TokenType.SPACE) {
            index++
        }

        while (tokens[index].tokenType != TokenType.NEW_LINE) {
            if (tokens[index].tokenType == TokenType.QUOTE_MARK)
                getString()
            else if (tokens[index].tokenType != TokenType.SPACE) {
                text += getStringNumber(tokens[index])

            }
            index++
        }

        text += "\n"
    }

    private fun getStringNumber(token: Token): String {
        return when (token.tokenType) {
            TokenType.ONE -> "1"
            TokenType.TWO -> "2"
            TokenType.THREE -> "3"
            TokenType.FOUR -> "4"
            TokenType.FIVE -> "5"
            TokenType.SIX -> "6"
            TokenType.SEVEN -> "7"
            TokenType.EIGHT -> "8"
            TokenType.NINE -> "9"
            TokenType.ZERO -> "0"
            else -> throw IllegalStateException("getStringNumber called on non-number!")
        }
    }
}