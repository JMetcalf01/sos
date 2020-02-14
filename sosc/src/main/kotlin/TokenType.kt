/**
 * A list of the keywords in sos.
 *
 * @author Jonathan Metcalf
 */
enum class TokenType(val unicode: String?) {
    FUNCTION("\uD83D\uDCDD"),
    MAIN("\uD83D\uDCAF"),
    IF("\uD83E\uDD14"),
    ELSE("\uD83D\uDC49"),
    LEFT_PARENTHESES("\uD83C\uDF1C"),
    RIGHT_PARENTHESES("\uD83C\uDF1B"),
    WHILE("\uD83D\uDD01"),
    PLUS("\u2795"),
    MINUS("\u2796"),
    MULTIPLY("\u2716"),
    DIVIDE("\u2797"),
    LESS_THAN("\u25C0"),
    GREATER_THAN("\u25B6"),
    CONTINUE("\u23ED"),
    QUOTE_MARK("\uD83D\uDD24"),
    VAR("\uD83D\uDCB2"),
    BRACE("\uD83D\uDD36"),
    ERROR("\u2622"),
    EQUALS("\uD83C\uDF9E"),
    COMPARE("\u2714"),
    IMPORT("\u27A1"),
    RETURN("\u2B05"),
    COMMENT("\uD83E\uDD56\uD83E\uDD56"),
    INFINITY("\u267E"),
    AND("\u2194"),
    NEW_LINE("\\n"),
    SPACE(" "),
    UNKNOWN(null),
    RAW("[a-zA-Z0-9]+|[0-9]+|[0-9]+.[0-9]+");
}

/**
 * An implementation of a token, with a type and the unicode representation.
 *
 * @author Jonathan Metcalf
 *
 * @property type the type of the token
 */
class Token(val type: TokenType, val raw: String? = null) {
    override fun toString(): String {
        return if (type == TokenType.UNKNOWN || type == TokenType.RAW) raw!! else type.unicode!!
    }
}