//import java.io.BufferedWriter
//import java.io.File
//import java.io.FileWriter
//
///**
// * Checks through the list of tokens for syntax and converts it into 🆘🥐 code.
// *
// * @author Jonathan Metcalf and Martin Bleakley
// *
// * @property tokens the list of tokens to check
// * @property outputPath the path to write the compiled 🆘🥐 code to
// */
//class Parser constructor(private val tokens: List<Token>, private val outputPath: String) {
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
//
//    private fun getParameters(): List<Token> {
//        assert(tokens[index].tokenType == TokenType.LEFT_PARENTHESES)
//        index++
//
//        val list = mutableListOf<Token>()
//        while (tokens[index].tokenType != TokenType.RIGHT_PARENTHESES) {
//            // If it's unknown, it's a parameter to add
//            if (tokens[index].tokenType == TokenType.UNKNOWN)
//                list.add(tokens[index])
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
//        return list
//    }
//
//}