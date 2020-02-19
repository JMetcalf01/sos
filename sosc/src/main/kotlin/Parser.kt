/**`
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
    fun parseFile(list: MutableList<Token>): String? {
        if (list.isEmpty()) return null

        // Does some cleaning up before it parses it
        val remove = mutableListOf<Token>()
        var inString = false
        var inParams = false
        var i = 0
        while (i < list.size) {
            // Toggles whether in a string or not
            if (list[i].type == TokenType.QUOTE_MARK) inString = !inString

            // Toggles if in parameters
            if (list[i].type == TokenType.LEFT_PARENTHESES) inParams = true
            if (list[i].type == TokenType.RIGHT_PARENTHESES) inParams = false

            // Remove new lines, spaces if not in string and not in parameters
            if ((list[i].type == TokenType.SPACE || list[i].type == TokenType.NEW_LINE) && !inString && !inParams)
                remove.add(list[i])

            // Removes spaces at the beginning of parameters
            if (list[i].type == TokenType.LEFT_PARENTHESES && list[i + 1].type != TokenType.RIGHT_PARENTHESES) {
                i++
                while (list[i].type == TokenType.SPACE) {
                    remove.add(list[i])
                    i++
                }
            }

            // Removes spaces at the end of parameters
            if (list[i].type == TokenType.RIGHT_PARENTHESES) {
                var temp = i - 1
                while (list[temp].type == TokenType.SPACE && list[temp].type != TokenType.LEFT_PARENTHESES) {
                    remove.add(list[temp])
                    temp--
                }
            }

            // Reduces new lines or spaces down to just one space in parameters
            if (inParams && i < list.size - 1 &&
                (list[i].type == TokenType.SPACE || list[i].type == TokenType.NEW_LINE) &&
                (list[i + 1].type == TokenType.SPACE || list[i + 1].type == TokenType.NEW_LINE)
            )
                remove.add(list[i])

            i++
        }
        list.removeAll(remove)

        if (list[0].type != TokenType.FUNCTION) return null
        val index = list.advanceToNext(TokenType.FUNCTION, start = 1)

        // If there is no second function, parse all of it as one function
        if (index == -1) return "${parseFunction(list.subList(0, list.size))}"

        // Otherwise recursively parse all of the functions
        val first = parseFunction(list.subList(0, index)) ?: return null
        val body = parseFile(list.subList(index, list.size)) ?: return first
        return "$first\n$body"
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
        if (list.isEmpty() || list[0].type != TokenType.FUNCTION) return null

        // Gets the name of the function
        val index = list.advanceToNext(TokenType.LEFT_PARENTHESES)
        val name = if (list[1].type == TokenType.MAIN) "main" else parseName(list.subList(1, index))

        // Gets parameters
        var params = ""
        val end = list.advanceToNext(TokenType.RIGHT_PARENTHESES, TokenType.CLOSE_BRACE, start = index)
        if (end <= index) return null
        val test = parseFuncParams(list.subList(index + 1, end))
        if (test != null) params = test
        if (name == "main" && !params.isBlank()) return null // If main has parameters, it's wrong

        // Gets body
        val body = parseBody(
            list.subList(
                list.advanceToNext(TokenType.OPEN_BRACE, start = index) + 1,
                list.advanceToLast(TokenType.CLOSE_BRACE)
            ) as MutableList<Token>
        ) ?: return null
        return "func $name $params\n$body"
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
            var next = list.advanceToNext(TokenType.SPACE, start = index)
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
    private fun parseBody(list: MutableList<Token>): String? {
        if (list.isEmpty()) return null

        // todo make it parse if else as one statement


        // Try to figure if it's a multi-line statement
        var index = list.advanceToNext(TokenType.CLOSE_BRACE)
        if (list.size > index + 1 && list[index + 1].type == TokenType.ELSE)
            index = list.advanceToNext(TokenType.CLOSE_BRACE, start = index + 1)
        else if (list.advanceToNext(TokenType.SEMICOLON) < list.advanceToNext(TokenType.OPEN_BRACE) || index == -1)
            index = list.advanceToNext(TokenType.SEMICOLON)

        val first = parseStatement(list.subList(0, index + 1)) ?: return null
        val body = parseBody(list.subList(index + 1, list.size)) ?: return first
        return "$first$body"  // Recursively parse all of the statements
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
        if (list.isEmpty()) return null

        val ifs = parseIf(list)
        if (ifs != null) return ifs

        val whiles = parseWhile(list as MutableList<Token>)
        if (whiles != null) return whiles

        val call = parseCall(list.subList(0, list.size - 1))
        if (call != null || list[list.size - 1].type != TokenType.SEMICOLON) return call

        val declaration = parseDeclaration(list)
        if (declaration != null) return declaration

        val set = parseSet(list)
        if (set != null) return set

        val returns = parseReturn(list)
        if (returns != null) return returns

        val continues = parseContinue(list)
        if (continues != null) return continues

        return null
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
        if (list.isEmpty() || list[0].type != TokenType.IF || list[1].type != TokenType.LEFT_PARENTHESES) return null

        // Gets expression
        val index = list.advanceToNext(TokenType.RIGHT_PARENTHESES)
        if (index == -1) return null
        val expression = parseExpression(list.subList(2, index)) ?: return null

        // Gets body
        if (list[index + 1].type != TokenType.OPEN_BRACE) return null
        var end = list.advanceToNext(TokenType.ELSE) - 1
        if (end == -2) end = list.advanceToLast(TokenType.CLOSE_BRACE)

        val ifBody = parseBody(list.subList(index + 2, end) as MutableList<Token>)
        end = list.advanceToNext(TokenType.ELSE)
        var elseBody: String? = ""
        if (end != -1) elseBody = parseBody(list.subList(end + 2, list.size - 1) as MutableList<Token>)
        if (elseBody == null) return null

        // todo goto
        return "${expression}if\ngoto (linecount + 1)\ngoto (linecount + 2 + ifBody length)\n$ifBody$elseBody"
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
    private fun parseWhile(list: MutableList<Token>): String? {
        if (list.isEmpty()) return null

        // Should start with a while keyword and should have a left parentheses
        if (list[0].type != TokenType.WHILE || list[1].type != TokenType.LEFT_PARENTHESES) return null

        // Now parse the expression and right parentheses
        val right = list.advanceToNext(TokenType.RIGHT_PARENTHESES)
        if (right == -1) return null
        val expression = parseExpression(list.subList(2, right)) ?: return null

        // Parse the body
        val body = parseBody(list.subList(right + 2, list.size - 1)) ?: return null

        // todo fix while loop to actually be the machine code representation using goto
        return "${expression}while\n$body"
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
        if (list.isEmpty()) return null

        // Index will be sitting at the left parentheses or space after this
        val index = list.advanceToNext(TokenType.LEFT_PARENTHESES)
        if (index == -1) return null

        // Checks whether name of function is valid
        val name = parseName(list.subList(0, index)) ?: return null

        // Checks parameters of function
        val end = list.advanceToLast(TokenType.RIGHT_PARENTHESES)
        if (end == -1 || end != list.size - 1) return null
        var params = parseCallParams(list.subList(index + 1, end))
        if (params == null || params == "null") params = ""

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
        // If list is empty, return empty string
        if (list.isEmpty()) return ""

        // If list has a single value, then return its string
        val value = parseValue(list)
        if (value != null) return value

        // If the list has multiple values, recursively check each whether it's a value
        val index = list.advanceToNext(TokenType.SPACE)
        if (index == -1) return null

        return "${parseValue(list.subList(0, index))}${parseCallParams(list.subList(index + 1, list.size))}"
    }

    /**
     * Parses a declaration based on the following rule:
     * DECLARE -> 💲 NAME 🎞 EXPRESSION
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the declaration
     */
    private fun parseDeclaration(list: List<Token>): String? {
        if (list.isEmpty() || list[0].type != TokenType.VAR) return null

        // Index will be sitting at the equals sign after this
        val index = list.advanceToNext(TokenType.EQUALS)
        if (index == -1) return null

        // Check to make sure it is a valid name and value
        val name = parseName(list.subList(1, index))
        val expression = parseExpression(list.subList(index + 1, list.size - 1))
        if (name == null || expression == null || list[list.size - 1].type != TokenType.SEMICOLON) return null

        return "${expression}store ${name}\n"
    }

    /**
     * Parses a setting based on the following rule:
     * SET -> NAME 🎞 EXPRESSION | NAME➕➕ | NAME➖➖ | NAME➕🎞EXPRESSION | NAME➖🎞EXPRESSION  | NAME➗🎞EXPRESSION | NAME✖🎞EXPRESSION
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the setting
     */
    private fun parseSet(list: List<Token>): String? {
        if (list.isEmpty()) return null

        val plus = list.advanceToNext(TokenType.PLUS)
        var name: String? = null
        if (plus != -1)
            name = parseName(list.subList(0, plus))

        // Checks for NAME++
        var index = list.advanceToNext(TokenType.PLUS)
        if (index != -1 &&
            name != null &&
            list.size == index + 3 &&
            list[list.size - 3].type == TokenType.PLUS &&
            list[list.size - 2].type == TokenType.PLUS &&
            list[list.size - 1].type == TokenType.SEMICOLON
        ) return "loadr $name\nload 1\ncall +\nstore $name"

        // Checks for NAME--
        index = list.advanceToNext(TokenType.PLUS)
        if (index != -1 &&
            name != null &&
            list.size == index + 3 &&
            list[list.size - 3].type == TokenType.MINUS &&
            list[list.size - 2].type == TokenType.MINUS &&
            list[list.size - 1].type == TokenType.SEMICOLON
        ) return "loadr $name\nload 1\ncall -\nstore $name"

        // Checks for NAME+=EXPRESSION
        val plusString = parseAssignment(list, TokenType.PLUS)
        if (plusString != null) return plusString

        // Checks for NAME-=EXPRESSION
        val minusString = parseAssignment(list, TokenType.MINUS)
        if (minusString != null) return minusString

        // Checks for NAME*=EXPRESSION
        val multiplyString = parseAssignment(list, TokenType.MULTIPLY)
        if (multiplyString != null) return multiplyString

        // Checks for NAME/=EXPRESSION
        val divideString = parseAssignment(list, TokenType.DIVIDE)
        if (divideString != null) return divideString

        // Else, check to make sure it is a valid name and value
        index = list.advanceToNext(TokenType.EQUALS)
        if (index == -1) return null
        name = parseName(list.subList(0, index))
        val expression = parseExpression(list.subList(index + 1, list.size - 1))
        if (name == null || expression == null || list[list.size - 1].type != TokenType.SEMICOLON) return null

        return "${expression}store $name\n"
    }

    /**
     * Parses NAME+=VALUE, NAME-=VALUE, NAME*=VALUE, NAME/=VALUE.
     *
     * @author Jonathan Metcalf
     *
     * @param list the list to check
     * @param token either plus, minus, multiply, or divide
     * @return the string representation of the assignment
     */
    private fun parseAssignment(list: List<Token>, token: TokenType): String? {
        val index = list.advanceToNext(token)
        if (index != -1) {
            val name = parseName(list.subList(0, index))
            val value = parseExpression(list.subList(index + 2, list.size - 1))
            if (name != null &&
                value != null &&
                list[index].type == token &&
                list[index + 1].type == TokenType.EQUALS &&
                list[list.size - 1].type == TokenType.SEMICOLON
            ) return "${value}load $name\ncall ${parseOperator(Token(token))}\nstore $name"
        }

        return null
    }

    /**
     * Parses an expression by the following rule:
     * EXPRESSION -> VALUE | CALL | EXPRESSION OPERATOR EXPRESSION
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of the expression
     */
    private fun parseExpression(list: List<Token>): String? {
        if (list.isEmpty()) return null

        val call = parseCall(list)
        if (call != null) return call

        // todo order of operations correctly and parentheses

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

        // If the first token isn't return, it is not valid
        if (list[0].type != TokenType.RETURN) return null

        // If the list from 2-onwards can successfully be parsed into a value, it is valid
        val value = parseValue(list.subList(1, list.nextIndexOf(1, TokenType.SEMICOLON)))
        return if (value != null) "${value}exit\n"
        else null
    }

    /**
     * Parses a continue by the following rule:
     * CONTINUE -> ⏭
     *
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the machine code representation of continue
     */
    private fun parseContinue(list: List<Token>): String? {
        if (list.isEmpty()) return null

        // todo actually make this work
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
        if (list.isEmpty()) return null

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
     * @author Jonathan Metcalf
     *
     * @param list the list of tokens
     * @return the string representation of the name
     */
    private fun parseName(list: List<Token>): String? {
        if (list.isEmpty()) return null

        // Make sure the name isn't a raw
        for (token in list)
            if (token.type == TokenType.RAW)
                return null

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
        if (list.joinToString("").matches(Regex("[0-9]+|[0-9]+\\.[0-9]+")))
            return list.joinToString("")

        // Otherwise, it doesn't match anything
        return null
    }
}

/**
 * Finds the first token after a starting index that matches with the list of tokens put in,
 * and returns the index of that first token.
 *
 * @author Jonathan Metcalf
 *
 * @param start the starting index to look for
 * @param types the list of tokens that should stop the search
 * @return the index of the first token found that is contained in types, or -1 if not found
 *
 * @see TokenType
 * @see List
 */
private fun List<Token>.advanceToNext(vararg types: TokenType, start: Int = 0): Int {
    var index = start
    while (!types.contains(this[index].type)) {
        if (index == size - 1) return -1
        index++
    }
    return index
}


/**
 * Advances to the last token in the list of the specified type.
 *
 * @param type the type of token
 * @return the index of the last token, or -1 if not found
 *
 * @see TokenType
 * @see List
 */
private fun List<Token>.advanceToLast(type: TokenType): Int {
    var index = -1
    forEachIndexed { i, token ->
        if (token.type == type) {
            index = i
        }
    }
    return index
}

/**
 * Finds the index of any amount of tokens from a certain point onwards.
 *
 * @author Jonathan Metcalf
 *
 * @param start the starting index to look at
 * @param matches the TokenTypes to look for
 * @return the index of the first matching token from that point onwards, or the next index if not found
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

/**
 * Holds the line count in an object so when it is passed into a function and the function edits
 * it, it actually edits it in the outer function as well.
 *
 * @author Jonathan Metcalf
 *
 * @property lineCount the current lineCount of each function
 */
private class LineCount(var lineCount: Int = 0) {
    operator fun inc(): LineCount {
        lineCount++
        return this
    }
}