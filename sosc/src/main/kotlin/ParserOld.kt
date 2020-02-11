//import java.io.BufferedWriter
//import java.io.File
//import java.io.FileWriter
//import kotlin.RuntimeException
//
///**
// * Checks through the list of tokens for syntax and converts it into 🆘🥐 code.
// *
// * @author Jonathan Metcalf and Martin Bleakley
// *
// * @property tokens the list of tokens to check
// * @property outputPath the path to write the compiled 🆘🥐 code to
// */
//class ParserOld constructor(private val tokens: List<Token>, private val outputPath: String) {
//
//
//    // list of parameters between parentheses
//    // return a string between two quotes
//    // move the index an arbitrary amount of characters (vararg)
//    //
//
//    private val writer: BufferedWriter = BufferedWriter(FileWriter(File(outputPath)))
//    private var text = ""
//    private var index = 0
//    private var lineCount = 0
//
//    /**
//     *
//     *
//     */
//    fun parse() {
//        while (index < tokens.size) {
//            when (tokens[index].tokenType) {
//                TokenType.FUNC -> {
//                    funcNameChecker()
//                    funcBodyChecker()
//                    text += "\n"
//                    lineCount++
//                }
//            }
//
//            index++
//        }
//
//        // Writes text to the file and closes
//        writer.write(text + "\n")
//        writer.close()
//    }
//
//    /**
//     * Checks whether a function name is correct or not.
//     *
//     * @author Jonathan Metcalf
//     *
//     */
//    private fun funcNameChecker() {
//        // If the next character after function is not a space, then throw an error
//        if (tokens[index + 1].tokenType != TokenType.SPACE)
//            throw RuntimeException("Expected ${TokenType.SPACE} but found ${tokens[index + 1].tokenType}!")
//
//        // If it's the main function, add func main to text
//        index += 2
//        if (tokens[index].tokenType == TokenType.MAIN) {
//            text += "func main\n"
//            lineCount++
//        } else {
//            text += "func ${tokens[index].unicode}"
//
//            // Does parameters
//            index++
//            findFuncParams()
//            text += "\n"
//            lineCount++
//        }
//
//        // Moves index to next line
//        while (tokens[index].tokenType != TokenType.NEW_LINE) {
//            index++
//        }
//        index++
//    }
//
//    /**
//     * Returns a list of the params between two parentheses.
//     *
//     * @author Jonathan Metcalf
//     *
//     * @return the list of the params
//     */
//    private fun findFuncParams() {
//        while (tokens[index].tokenType != TokenType.RIGHT_PARENTHESES) {
//            // If it's unknown, it's a parameter to add
//            if (tokens[index].tokenType == TokenType.UNKNOWN)
//                text += " ${tokens[index].unicode}"
//
//            // Finding string
//            if (tokens[index].tokenType == TokenType.QUOTE_MARK)
//                getString()
//
//            // Parentheses are incorrect if this is true
//            if (tokens[index].tokenType == TokenType.BRACE || tokens[index].tokenType == TokenType.NEW_LINE)
//                throw RuntimeException("Expected ${TokenType.RIGHT_PARENTHESES} but found a ${tokens[index].tokenType} instead!")
//
//            index++
//        }
//    }
//
//    /**
//     * Reads the body of a function and parses it.
//     *
//     * @author Jonathan Metcalf
//     */
//    private fun funcBodyChecker() {
//        when (tokens[index].tokenType) {
//            TokenType.UNKNOWN -> {
//                val start = index
//                index++
//                findCalledParams()
//                text += "call ${tokens[start].unicode}\n"
//                lineCount++
//            }
//
//            TokenType.VAR -> {
//                storeVariable()
//            }
//
//            TokenType.IF -> {
//                ifFunction()
//            }
//
//            TokenType.WHILE -> {
//
//            }
//
//            TokenType.RETURN -> {
//                index += 2
//                if (tokens[index].tokenType == TokenType.UNKNOWN) {
//                    text += "load ${tokens[index].unicode}\n"
//                    lineCount++
//                } else if (tokens[index].tokenType == TokenType.QUOTE_MARK) {
//                    text += "loadr "
//                    getString()
//                    text += "\n"
//                    lineCount++
//                }
//
//                text += "exit\n"
//                lineCount++
//            }
//        }
//    }
//
//    /**
//     * Returns a string from the starting quotemark to
//     * the next quotemark.
//     *
//     * @return the string (minus the quotemarks)
//     */
//    private fun getString() {
//        index++
//        val start = index
//        while (tokens[index].tokenType != TokenType.QUOTE_MARK) {
//            index++
//        }
//
//        for (i in start until index) {
//            text += tokens[i].unicode
//        }
//    }
//
//    /**
//     * Find the parameters of a function that is being called.
//     *
//     * @author Jonathan Metcalf and Troy Mullenberg
//     *
//     */
//    private fun findCalledParams() {
//        while (tokens[index].tokenType != TokenType.RIGHT_PARENTHESES) {
//            // If it's unknown, it's a parameter to add
//            if (tokens[index].tokenType == TokenType.UNKNOWN) {
//                text += "load ${tokens[index].unicode}\n"
//                lineCount++
//            }
//
//            // Finding string
//            if (tokens[index].tokenType == TokenType.QUOTE_MARK) {
//                index++
//                text += "loadr "
//                getString()
//                text += "\n"
//                lineCount++
//            }
//
//            // Parentheses are incorrect if this is true
//            if (tokens[index].tokenType == TokenType.BRACE || tokens[index].tokenType == TokenType.NEW_LINE)
//                throw RuntimeException("Expected ${TokenType.RIGHT_PARENTHESES} but found a ${tokens[index].tokenType} instead!")
//
//            index++
//        }
//    }
//
//    /**
//     * Stores a variable name.
//     *
//     * @author Jonathan Metcalf and Troy Mullenberg
//     *
//     */
//    private fun storeVariable() {
//        index++
//        if (tokens[index].tokenType != TokenType.SPACE)
//            throw RuntimeException("Expected ${TokenType.SPACE} but found ${tokens[index].tokenType}!")
//
//        index++
//        text += "store ${tokens[index].unicode} "
//        index++
//        findValue()
//    }
//
//    /**
//     * Finds the value of a specific string or number, converting it from emoji into text.
//     *
//     * @author Jonathan Metcalf and Troy Mullenberg
//     *
//     */
//    private fun findValue() {
//        while (tokens[index].tokenType == TokenType.SPACE) {
//            index++
//        }
//
//        if (tokens[index].tokenType != TokenType.EQUALS)
//            throw RuntimeException("Expected ${TokenType.EQUALS} but found ${tokens[index].tokenType}!")
//
//        index++
//        while (tokens[index].tokenType == TokenType.SPACE) {
//            index++
//        }
//
//        while (tokens[index].tokenType != TokenType.NEW_LINE) {
//            if (tokens[index].tokenType == TokenType.QUOTE_MARK)
//                getString()
//            else if (tokens[index].tokenType != TokenType.SPACE) {
//                text += getStringNumber(tokens[index])
//
//            }
//            index++
//        }
//
//        text += "\n"
//        lineCount++
//    }
//
//    /**
//     * Returns the string representation of each number emoji.
//     *
//     * @author Jonathan Metcalf
//     *
//     * @param token the token to be converted
//     * @return the string representation
//     */
//    private fun getStringNumber(token: Token): String {
//        return when (token.tokenType) {
//            TokenType.ONE -> "1"
//            TokenType.TWO -> "2"
//            TokenType.THREE -> "3"
//            TokenType.FOUR -> "4"
//            TokenType.FIVE -> "5"
//            TokenType.SIX -> "6"
//            TokenType.SEVEN -> "7"
//            TokenType.EIGHT -> "8"
//            TokenType.NINE -> "9"
//            TokenType.ZERO -> "0"
//            else -> throw IllegalStateException("getStringNumber called on non-number!")
//        }
//    }
//
//    /**
//     * Parses a bool used in an if statement
//     * (like (2<3)
//     */
//    private fun parseBool() {
//        val load1: Token = tokens[index]
//        index++
//        val call: Token = tokens[index]
//        index++
//        val load2: Token = tokens[index]
//        index++
//
//        if (load1.tokenType == TokenType.UNKNOWN) {
//            text += "load ${load1.unicode}\n"
//            lineCount++
//        } else {
//            text += "loadr ${load1.unicode}\n"
//            lineCount++
//        }
//
//        if (load2.tokenType == TokenType.UNKNOWN) {
//            text += "load ${load2.unicode}\n"
//            lineCount++
//        } else {
//            text += "loadr ${load2.unicode}\n"
//            lineCount++
//        }
//
//        text += "call ${call.unicode}\n"
//        lineCount++
//    }
//
//    /**
//     * Deals with if statements.
//     *
//     * @author Jonathan Metcalf and Troy Mullenberg
//     *
//     */
//    private fun ifFunction() {
//        index++
//        while (tokens[index].tokenType == TokenType.SPACE) {
//            index++
//        }
//        index++
//        parseBool()
//        text += "if\n"
//        lineCount++
//
//        // using a different string, print false route with new lines, and also give us how many lines that is
//
//        var elseIndex = index
//        while (tokens[elseIndex].tokenType != TokenType.ELSE) {
//            elseIndex++
//        }
//        while (tokens[elseIndex].tokenType != TokenType.NEW_LINE) {
//            elseIndex++
//        }
//        index++
//
//        val tempIndex = index
//        val lines = translateFalse()
//        text += "goto ${(lineCount + lines + 2)}"
//        // add lines to linecount for real
//        // Actually print false
//
//        // back to printing the true route with new lines, keeping linecount accurate
//
//
//    }
//
//    private fun translateFalse(): Int {
//        var falseIndex = index
//        var lines = 0
//        while (tokens[falseIndex].tokenType != TokenType.BRACE) {
//           if (tokens[falseIndex].tokenType == TokenType.NEW_LINE) lines++
//
//            funcBodyChecker()
//
//            falseIndex++
//        }
//        return lines
//    }
//}