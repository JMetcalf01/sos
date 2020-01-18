/**
 * A list of the keywords in sos.
 *
 * @author Jonathan Metcalf
 */
enum class TokenType(val unicode: String?) {
    UNKNOWN(null),
    NEW_LINE("\\n"),
    SPACE(" "),
    FUNC("\uD83D\uDCDD"),
    MAIN("\uD83D\uDCAF"),
    IF("\uD83E\uDD14"),
    ELIF("\uD83E\uDD37\u200D\uD83E\uDD14"),
    ELSE("\uD83D\uDC49"),
    LEFT_PARENTHESES("\uD83C\uDF1C"),
    RIGHT_PARENTHESES("\uD83C\uDF1B"),
    WHILE("\uD83D\uDD01"),
    PLUS("\u2795"),
    MINUS("\u2796"),
    MULTIPLY("\u2716"),
    DIVIDE("\u2797"),
    LESS_THAN("\u25C0"),
    MORE_THAN("\u25B6"),
    CONTINUE("\u23ED"),
    QUOTEMARK("\uD83D\uDD24"),
    VAR("\uD83D\uDCB2"),
    BRACE("\uD83D\uDD36"),
    ERROR("\u2622"),
    EQUALS("\uD83C\uDF9E"),
    IMPORT("\u27A1"),
    RETURN("\u2B05"),
    PRINT("\uD83D\uDDA8"),
    COMMENT("\uD83E\uDD56\uD83E\uDD56"),
    ZERO("\u0030\uFE0F\u20E3"),
    ONE("\u0031\uFE0F\u20E3"),
    TWO("\u0032\uFE0F\u20E3"),
    THREE("\u0033\uFE0F\u20E3"),
    FOUR("\u0034\uFE0F\u20E3"),
    FIVE("\u0035\uFE0F\u20E3"),
    SIX("\u0036\uFE0F\u20E3"),
    SEVEN("\u0037\uFE0F\u20E3"),
    EIGHT("\u0038\uFE0F\u20E3"),
    NINE("\u0039\uFE0F\u20E3"),
    INFINITY("\u267E");
}

/**
 * An implementation of a token, with a type and the unicode representation.
 *
 * @author Jonathan Metcalf
 *
 * @property tokenType the type of the token
 * @property unicode the unicode representation of the token
 */
class Token(private val tokenType: TokenType, private val unicode: String) {
    override fun toString(): String {
        return if (tokenType == TokenType.UNKNOWN) unicode else tokenType.name
    }
}