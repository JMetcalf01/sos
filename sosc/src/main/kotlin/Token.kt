/**
 * The implementation of tokens (including emojis) for the programming language sos.
 *
 * @author Jonathan Metcalf
 *
 * @property unicode the unicode for the token
 */
class Token (private val unicode: String) {

    /**
     * Returns the unicode representation of the emoji
     *
     * @return the unicode representation of the emoji
     */
    override fun toString(): String {
        return unicode
    }
}

/**
 * A list of the keywords in sos.
 *
 * @author Jonathan Metcalf
 */
object Tokens {
    val SPACE = Token(" ")
    val NEW_LINE = Token("\n")
    val FUNC = Token("\uD83D\uDCDD")
    val MAIN = Token("\uD83D\uDCAF")
    val IF = Token("\uD83E\uDD14")
    val ELIF = Token("\uD83E\uDD37\u200D\uD83E\uDD14")
    val ELSE = Token("\uD83D\uDC49")
    val LEFT_PARENTHESES = Token("\uD83C\uDF1C")
    val RIGHT_PARENTHESES = Token("\uD83C\uDF1B")
    val WHILE = Token("\uD83D\uDD01")
    val PLUS = Token("\u2795")
    val MINUS = Token("\u2796")
    val MULTIPLY = Token("\u2716")
    val DIVIDE = Token("\u2797")
    val LESS_THAN = Token("\u25C0")
    val MORE_THAN = Token("\u25B6")
    val CONTINUE = Token("\u23ED")
    val QUOTEMARK = Token("\uD83D\uDD24")
    val VAR = Token("\uD83D\uDCB2")
    val BRACE = Token("\uD83D\uDD36")
    val ERROR = Token("\u2622")
    val EQUALS = Token("\uD83C\uDF9E")
    val IMPORT = Token("\u27A1")
    val RETURN = Token("\u2B05")
    val PRINT = Token("\uD83D\uDDA8")
    val COMMENT = Token("\uD83E\uDD56\uD83E\uDD56")
    val ZERO = Token("\u0030\uFE0F\u20E3")
    val ONE = Token("\u0031\uFE0F\u20E3")
    val TWO = Token("\u0032\uFE0F\u20E3")
    val THREE = Token("\u0033\uFE0F\u20E3")
    val FOUR = Token("\u0034\uFE0F\u20E3")
    val FIVE = Token("\u0035\uFE0F\u20E3")
    val SIX = Token("\u0036\uFE0F\u20E3")
    val SEVEN = Token("\u0037\uFE0F\u20E3")
    val EIGHT = Token("\u0038\uFE0F\u20E3")
    val NINE = Token("\u0039\uFE0F\u20E3")
    val INFINITY = Token("\u267E")
}