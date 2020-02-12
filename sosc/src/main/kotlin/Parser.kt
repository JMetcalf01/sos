/**
 * Checks through the list of tokens for syntax and converts it into 🆘🥐 code.
 * Uses the rules found in ParserLanguage.txt
 *
 * @author Jonathan Metcalf
 */
class Parser {

    /**
     * Parses the list of tokens of the file and prints the machine code
     * into the output file.
     *
     * Parses a file based on the following rule:
     * FILE -> [FUNCTION[\n]+]+
     *
     * @author Jonathan Metcalf
     *
     * @param list the list to parse
     */
    fun parseFile(list: List<Token>): String? {
        // Advance to the first function
        val index = advanceNextToken(list, 0, TokenType.FUNCTION)
        if (index == -1) throw Exception("Compile failed!")

        // If it's only one function, just return the statement
        val next = advanceNextToken(list, index, TokenType.FUNCTION)
        if (next == 0) return "${parseFunction(list.subList(0, list.size))}"

        // Otherwise recursively parse all of the statements
        return "${parseFunction(list.subList(0, list.size))}\n${parseFile(list.subList(index + 1, list.size))}"
    }

    /**
     * Parses a function based on the following rule:
     * FUNCTION -> 📝 NAME FUNC_PARAMS 🔶 BODY 🔶
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the function
     */
    private fun parseFunction(list: List<Token>): String? {
        if (list[0].type != TokenType.FUNCTION) return null

        // Gets the name of the function
        var index = advanceNextToken(list, 2, TokenType.SPACE)
        val name = if (list[2].type == TokenType.MAIN) "main" else parseName(list.subList(2, index))
        index++

        // Gets parameters (if it's not the main function)
        var params = ""
        if (name != "main") {
            index = advanceNextToken(list, index, TokenType.LEFT_PARENTHESES)
            val end = advanceNextToken(list, index, TokenType.RIGHT_PARENTHESES)
            val test = parseFuncParams(list.subList(index + 1, end))
            if (test != null) params = test
        }

        // Gets body
        val body = parseBody(list) ?: return null
        return "func $name $params\n$body\n"
    }

    /**
     * Parses a function parameters based on the following rule:
     * FUNC_PARAMS -> EMPTY | NAME | NAME FUNC_PARAMS
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the parameters
     */
    private fun parseFuncParams(list: List<Token>): String? {
        // If it's empty, there are no parameters
        if (list.isEmpty()) return ""

        // Check to make sure all the tokens are valid
        var index = 0
        for (token in list) {
            var next = advanceNextToken(list, index, TokenType.SPACE)
            if (next == -1) next = list.size
            if (token.type != TokenType.SPACE && parseName(list.subList(index, next)) == null) return null
            else index = next + 1
            if (next >= list.size) break
        }

        return list.joinToString("")
    }

    /**
     * Parses a body of a function/statement (if applicable) based on the following rule:
     * BODY -> STATEMENT | [STATEMENT\n]+
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the body
     */
    private fun parseBody(list: List<Token>): String? {
        // If the list has multiple values, recursively check each whether it's a value
        val index = advanceNextToken(list, 0, TokenType.NEW_LINE)
        if (index == -1) return null

        // If it's only one statement, just return the statement
        if (index == list.size - 1) return "${parseStatement(list.subList(0, index - 1))}\n"

        // Otherwise recursively parse all of the statements
        return "${parseStatement(list.subList(0, index - 1))}${parseBody(list.subList(index + 1, list.size))}"
    }

    /**
     * Parses a statement based on the following rule:
     * STATEMENT -> IF | WHILE | CALL | DECLARE | SET | RETURN | CONTINUE
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the statement
     */
    private fun parseStatement(list: List<Token>): String? {
        val ifs = parseIf(list)
        if (ifs != null) return ifs

        val whiles = parseWhile(list)
        if (whiles != null) return whiles

        val call = parseCall(list)
        if (call != null) return call

        val declaration = parseDeclaration(list)
        if (declaration != null) return declaration

        val set = parseSet(list)
        if (set != null) return set

        val returns = parseReturn(list)
        if (returns != null) return returns

        val continues = parseContinue(list)
        if (continues != null) return continues

        return ""
    }

    /**
     * Parses an if statement based on the following rule:
     * IF -> 🤔 🌜EXPRESSION🌛 🔶 BODY 🔶 | 🤔 🌜EXPRESSION🌛 🔶 BODY 🔶 👉 🔶 BODY 🔶
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the if statement
     */
    private fun parseIf(list: List<Token>): String? {
        // TODO
        return null
    }

    /**
     * Parses a while loop based on the following rules:
     * WHILE -> 🔁 🌜EXPRESSION🌛 🔶 BODY 🔶
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the while loop
     */
    private fun parseWhile(list: List<Token>): String? {
        // Should start with a while keyword
        var index = 0
        if (list[index].type != TokenType.WHILE) return null
        index++

        index = advanceSpaceTo(list, index, TokenType.LEFT_PARENTHESES)

        // Should have a left parentheses
        if (list[index].type != TokenType.LEFT_PARENTHESES) return null
        index++

        // Now parse the expression and right parentheses
        val right = advanceNextToken(list, index, TokenType.RIGHT_PARENTHESES)
        val expression = parseExpression(list.subList(index, right)) ?: return null
        index = right
        if (list[index].type != TokenType.RIGHT_PARENTHESES) return null

        // Finds the next brace with any amount of spaces
        index = advanceSpaceTo(list, index, TokenType.BRACE)
        if (index != -1) index++ else return null

        val body = parseBody(list.subList(index, advanceToLastToken(list, TokenType.BRACE))) ?: return null

        // todo fix this to actually be the machine code representation using goto
        return "while\n$expression$body"
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
        if (index == -1) return null

        // Checks whether name of function is valid
        val name = parseName(list.subList(0, index)) ?: return null

        // Advances index to be definitely at the left parentheses
        index = advanceSpaceTo(list, index, TokenType.LEFT_PARENTHESES)
        if (index == -1) return null

        var params = parseCallParams(list.subList(index + 1, list.size - 1))
        if (params == null) params = ""

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
        val index = advanceNextToken(list, 0, TokenType.SPACE)
        if (index == -1) return null

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
        val index = advanceNextToken(mutable, 0, TokenType.EQUALS)
        if (index == -1) return null

        // Check to make sure it is a valid name and value
        val name = parseName(mutable.subList(0, index))
        val expression = parseExpression(mutable.subList(index + 1, mutable.size))
        if (name == null || expression == null) return null

        return "${expression}store ${name}\n"
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

        val value = parseValue(list)
        if (value != null) return value

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
        if (list.isEmpty()) return null

        // If the first and second tokens aren't return and space, it is not valid
        if (list[0].type != TokenType.RETURN || list[1].type != TokenType.SPACE) return null

        // If the list from 2-onwards can successfully be parsed into a value, it is valid
        val value = parseValue(list.subList(2, list.nextIndexOf(2, TokenType.SPACE, TokenType.NEW_LINE)))
        if (value != null) {
            return "${value}exit\n"
        }

        // Otherwise, it isn't valid
        return null
    }

    /**
     * Parses a continue by the following rule:
     * CONTINUE -> ⏭
     *
     * TODO ACTUALLY MAKE THIS WORK
     *
     * @param list the list of tokens
     * @return the machine code representation of continue
     */
    private fun parseContinue(list: List<Token>): String? {
        return if (list.size == 1 && list[0].type == TokenType.CONTINUE) "continue\n"
        else null
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
        // todo fix random actual strings from counting!
        if (list.isEmpty()) return null

        for (token in list) {
            if (token.type == TokenType.RAW) {
                throw Exception("Name shouldn't be a ")
            }
        }

        for (token in list) {
            // If there are spaces, it is not valid
            if (token.type == TokenType.SPACE) return null
            // If there are keywords, it is not valid
            if (TokenType.values().contains(token.type) && token.type != TokenType.UNKNOWN) return null
            // If there is a literal, it is not valid
            if (token.type == TokenType.UNKNOWN && token.raw!!.matches(Regex(TokenType.RAW.unicode!!))) return null
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
        for (i in 1 until list.size - 1) {
            if (list[i].type == TokenType.QUOTE_MARK) {
                return null
            }
        }

        if (list[0].type == TokenType.QUOTE_MARK)
            return list.subList(1, list.size - 1).joinToString("")

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
     * @return the index of the first token found that is contained in types, or -1 if not found
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
     * In this case, the tokens in between must all be of spaces or new lines.
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @param start the starting index to look for
     * @param types the list of tokens that should stop the search
     * @return the index of the first token found that is contained in types, or -1 if not found
     */
    private fun advanceSpaceTo(list: List<Token>, start: Int = 0, vararg types: TokenType): Int {
        var index = start
        while (!(types.contains(list[index].type)) && (list[index].type == TokenType.SPACE || list[index].type == TokenType.NEW_LINE)) {
            if (index == list.size - 1) return start + 1
            index++
        }
        return index
    }

    /**
     * Advances to the last token in the list of the specified type.
     *
     * @param list the list of tokens
     * @param type the type of token
     * @return the index of the last token, or -1 if not found
     */
    private fun advanceToLastToken(list: List<Token>, type: TokenType): Int {
        var index = -1
        list.forEachIndexed { i, token ->
            if (token.type == type) {
                index = i
            }
        }
        return index
    }
}

/**
 * Finds the index of any amount of tokens from a certain point onwards.
 *
 * @author Jonathan Metcalf
 *
 * @param start the starting index to look at
 * @param matches the TokenTypes to look for
 * @return the index of the first matching token from that point onwards, or -1 if not found
 *
 * @see TokenType
 * @see List
 */
private fun List<Token>.nextIndexOf(start: Int, vararg matches: TokenType): Int {
    for (i in start until size) {
        if (matches.contains(this[i].type))
            return i
    }
    return start + 1
}
