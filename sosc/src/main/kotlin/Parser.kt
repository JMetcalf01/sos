import java.io.BufferedWriter
import java.io.FileWriter

/**
 * Checks through the list of tokens for syntax and converts it into 🆘🥐 code.
 * Uses the rules found in ParserLanguage.txt
 *
 * @author Jonathan Metcalf
 *
 * @property outputPath the path to write the compiled 🆘🥐 code to
 */
class Parser(private val outputPath: String) {

    private val writer = BufferedWriter(FileWriter(outputPath))

    /**
     * Parses the list of tokens and prints the machine code
     * into the output file.
     *
     * @author Jonathan Metcalf
     *
     * @param toParse the list to parse
     */
    fun parse(toParse: List<Token>) {
        writer.write("${parseCall(toParse.subList(0, toParse.size - 2))}\n")
        writer.close()
    }

    /**
     * Parses a method call based on the following rule:
     * CALL -> NAME🌜CALL_PARAMS🌛
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the list of parameters being loaded
     */
    private fun parseCall(list: List<Token>): String? {
        // Index will be sitting at the left parentheses or space after this
        var index = advanceNextToken(list, 0, TokenType.LEFT_PARENTHESES, TokenType.SPACE)

        // Checks whether name of function is valid
        val name = parseName(list.subList(0, index)) ?: return null

        // Advances index to be definitely at the left parentheses
        index = advanceNextToken(list, index, TokenType.LEFT_PARENTHESES)
        val params = parseCallParams(list.subList(index + 1, list.size - 1)) ?: return null

        return "${params}call $name\n"
    }

    /**
     * Parses a list of parameters in a method call based on the following rule:
     * CALL_PARAMS -> EMPTY | VALUE | VALUE CALL_PARAMS
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the list of parameters being loaded
     */
    private fun parseCallParams(list: List<Token>): String? {
        // If list is empty, return empty list
        if (list.isEmpty()) return ""

        // If list has a single value, then return a list containing that one value
        val value = parseValue(list)
        if (value != null) return value

        // If the list has multiple values, recursively check each whether it's a value
        val index = advanceNextToken(list, 1, TokenType.SPACE)
        return "${parseValue(list.subList(0, index))}${parseCallParams(list.subList(index + 1, list.size))}"
    }

    /**
     * Parses a declaration based on the following rule:
     * DECLARE -> 💲 SET
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the declaration
     */
    private fun parseDeclaration(list: List<Token>): String? {
        if (list.isEmpty()) return null

        if (list[0].type != TokenType.VAR || list[1].type != TokenType.SPACE) return null

        return parseSet(list.subList(2, list.size))
    }

    /**
     * Parses a setting based on the following rule:
     * SET -> NAME 🎞 VALUE
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the setting
     */
    private fun parseSet(list: List<Token>): String? {
        if (list.isEmpty()) return null

        // Remove spaces
        val mutable = list.toMutableList()
        val toRemove = mutableListOf<Token>()
        for (token in mutable)
            if (token.type == TokenType.SPACE) toRemove.add(token)
        mutable.removeAll(toRemove)

        // Index will be sitting at the equals sign after this
        val index = advanceNextToken(list, 0, TokenType.EQUALS)

        // Check to make sure it is a valid name and value
        val name = parseName(mutable.subList(0, index))
        val value = parseValue(mutable.subList(index + 1, mutable.size))
        if (name == null || value == null) return null

        return "${value}store ${name}\n"
    }

    /**
     * Parses an expression by the following rule:
     * EXPRESSION -> VALUE | EXPRESSION OPERATOR EXPRESSION
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the expression
     */
    private fun parseExpression(list: List<Token>): String? {
        if (list.isEmpty()) return null

        var index = list.size - 1
        // Index is now sitting at the operator
        while (parseOperator(list[index]) == null) {
            if (index == 0) return null
            index--
        }

        val left = parseExpression(list.subList(0, index)) ?: parseValue(list.subList(0, index))
        val operator = parseOperator(list[index])
        val right =
            parseExpression(list.subList(index + 1, list.size)) ?: parseValue(list.subList(index + 1, list.size))

        if (left == null || operator == null || right == null) return null

        return "${left}${right}call ${operator}\n"
    }

    /**
     * Parses an operator by the following rule:
     * OPERATOR -> ◀ | ▶ | ✔ | ➕ | ➖ | ✖ | ➗
     *
     * @author Jonathan Metcalf
     *
     * @param token the single token to be parsed
     * @return the string representation of the operator
     */
    private fun parseOperator(token: Token): String? {
        if (token.type == TokenType.GREATER_THAN) return ">"
        if (token.type == TokenType.LESS_THAN) return "<"
        if (token.type == TokenType.COMPARE) return "=="
        if (token.type == TokenType.PLUS) return "+"
        if (token.type == TokenType.MINUS) return "-"
        if (token.type == TokenType.MULTIPLY) return "*"
        if (token.type == TokenType.DIVIDE) return "/"
        return null
    }

    /**
     * Parses a return by the following rule:
     * RETURN -> ➡ VALUE
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the return
     */
    private fun parseReturn(list: List<Token>): String? {
        println(list)
        if (list.isEmpty()) return null

        // If the first and second tokens aren't return and space, it is not valid
        if (list[0].type != TokenType.RETURN || list[1].type != TokenType.SPACE) return null

        // If the list from 2-onwards can successfully be parsed into a value, it is valid
        val value = parseValue(list.subList(2, list.nextIndexOf(2, TokenType.SPACE)))
        if (value != null) return "${value}exit\n"

        // Otherwise, it isn't valid
        return null
    }

    /**
     * Parses a value by the following rule:
     * VALUE -> RAW | NAME
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the value
     */
    private fun parseValue(list: List<Token>): String? {
        val name = parseName(list)
        val raw = parseRaw(list)
        if (name != null) return "load $name\n"
        if (raw != null) return "loadr $raw\n"
        return null
    }

    /**
     * Parses a name by the following rule:
     * NAME -> [❔ (non-keyword)]+
     *
     * @param list the list of tokens
     * @return the string representation of the name
     */
    private fun parseName(list: List<Token>): String? {
        if (list.isEmpty()) return null

        for (token in list) {
            // If there are spaces, it is not valid
            if (token.type == TokenType.SPACE) return null
            // If there are keywords, it is not valid
            if (TokenType.values().contains(token.type) && token.type != TokenType.UNKNOWN) return null
            // If there is a literal, it is not valid
            if (token.type != TokenType.UNKNOWN && token.type.unicode!!.matches(Regex(TokenType.RAW.unicode!!))) return null
        }
        return list.joinToString("")
    }

    /**
     * Parses a literal of the form int, double, string of the form:
     * RAW -> [0-9]+ | [0-9]+.[0-9]+ | 🔤[a-zA-Z0-9]🔤
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the string representation of the raw
     */
    private fun parseRaw(list: List<Token>): String? {
        if (list.isEmpty()) return null

        // This means it's a string
        if (list[0].type == TokenType.QUOTE_MARK && list[list.size - 1].type == TokenType.QUOTE_MARK) {
            return list.joinToString("")
        }

        // Otherwise, it should only be numbers
        if (list.joinToString("").matches(Regex("[0-9]+|[0-9]+.[0-9]+")))
            return list.joinToString("")

        // Otherwise, it doesn't match anything
        return null
    }

    /**
     * Finds the first token after a starting index that matches with the list of tokens put in,
     * and returns the index of that first token.
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @param start the starting index to look for
     * @param types the list of tokens that should stop the search
     * @return the index of the first token found that is contained in types
     */
    private fun advanceNextToken(list: List<Token>, start: Int = 0, vararg types: TokenType): Int {
        var index = start
        while (!types.contains(list[index].type)) {
            if (index == list.size - 1) return -1
            index++
        }
        return index
    }

    /**
     * Finds the first token after a starting index that matches with the list of tokens put in,
     * and returns the index of that first token.
     *
     * In this case, the tokens in between must all be of spaces or new lines
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @param start the starting index to look for
     * @param types the list of tokens that should stop the search
     * @return the index of the first token found that is contained in types
     */
    private fun advanceSpaceTo(list: List<Token>, start: Int = 0, vararg types: TokenType): Int {
        var index = start
        while (!types.contains(list[index].type) && (list[index].type == TokenType.SPACE || list[index].type == TokenType.NEW_LINE)) {
            if (index == list.size - 1) return -1
            index++
        }
        return index
    }

}

/**
 * Finds the index of a token from a certain point onwards.
 *
 * @author Jonathan Metcalf
 *
 * @param start the starting index to look at
 * @param match the TokenType to look for
 * @return the index of the first matching token from that point onwards, or -1 if not found
 *
 * @see TokenType
 * @see List
 */
private fun List<Token>.nextIndexOf(start: Int, match: TokenType): Int {
    for (i in start..size) {
        if (this[i].type == match)
            return i
    }
    return -1
}
