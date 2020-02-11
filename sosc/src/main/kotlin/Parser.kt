/**
 * Checks through the list of tokens for syntax and converts it into 🆘🥐 code.
 *
 * @author Jonathan Metcalf
 *
 * @property tokens the list of tokens to check
 * @property outputPath the path to write the compiled 🆘🥐 code to
 */
class Parser(private val tokens: List<Token>, private val outputPath: String) {

    fun parse(): String? {
        return parseOperator(tokens[0])
    }

    /**
     * Parses an operator by the following rule:
     * OPERATOR -> ◀ | ▶ | ✔ | ➕ | ➖ | ✖ | ➗
     *
     * @param token
     * @return
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
     * @return a string representation of the return
     */
    private fun parseReturn(list: List<Token>): String? {
        if (list.isEmpty()) return null

        // If the first and second tokens aren't return and space, it is not valid
        if (list[0].type != TokenType.RETURN || list[1].type != TokenType.SPACE) return null

        // If the list from 2-onwards can successfully be parsed into a value, it is valid
        val value = parseValue(list.subList(2, list.indexOfOnwards(2, TokenType.SPACE)))
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
     * @return a string representation of the value
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
}

private fun List<Token>.indexOfOnwards(start: Int, match: TokenType): Int {
    for (i in start..size) {
        if (this[i].type == match)
            return i
    }
    return -1
}
