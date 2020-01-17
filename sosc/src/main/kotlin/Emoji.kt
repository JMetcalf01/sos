/**
 * The implementation of emojis for the programming language sos.
 *
 * @author Jonathan Metcalf
 *
 * @property unicode the unicode for the emoji
 */
class Emoji (private val unicode: String) {

    /**
     * Returns the unicode representation of the emoji
     *
     * @return the unicode representation of the emoji
     */
    override fun toString(): String {
        return unicode
    }

    /**
     * A list of the keywords in sos.
     *
     * @author Jonathan Metcalf
     */
    object Emojis {
        val FUNC = Emoji("\uD83D\uDCDD")
        val MAIN = Emoji("\uD83D\uDCAF")
        val IF = Emoji("\uD83E\uDD14")
        val ELIF = Emoji("\uD83E\uDD37\u200D\uD83E\uDD14")
        val ELSE = Emoji("\uD83D\uDC49")
        val LEFT_PARENTHESES = Emoji("\uD83C\uDF1C")
        val RIGHT_PARENTHESES = Emoji("\uD83C\uDF1B")
        val WHILE = Emoji("\uD83D\uDD01")
        val PLUS = Emoji("\u2795")
        val MINUS = Emoji("\u2796")
        val MULTIPLY = Emoji("\u2716")
        val DIVIDE = Emoji("\u2797")
        val LESS_THAN = Emoji("\u25C0")
        val MORE_THAN = Emoji("\u25B6")
        val CONTINUE = Emoji("\u23ED")
        val QUOTEMARK = Emoji("\uD83D\uDD24")
        val VAR = Emoji("\uD83D\uDCB2")
        val BRACE = Emoji("\uD83D\uDD36")
        val ERROR = Emoji("\u2622")
        val EQUALS = Emoji("\uD83C\uDF9E")
        val IMPORT = Emoji("\u27A1")
        val RETURN = Emoji("\u2B05")
        val PRINT = Emoji("\uD83D\uDDA8")
        val COMMENT = Emoji("\uD83E\uDD56\uD83E\uDD56")
        val ZERO = Emoji("\u0030\uFE0F\u20E3")
        val ONE = Emoji("\u0031\uFE0F\u20E3")
        val TWO = Emoji("\u0032\uFE0F\u20E3")
        val THREE = Emoji("\u0033\uFE0F\u20E3")
        val FOUR = Emoji("\u0034\uFE0F\u20E3")
        val FIVE = Emoji("\u0035\uFE0F\u20E3")
        val SIX = Emoji("\u0036\uFE0F\u20E3")
        val SEVEN = Emoji("\u0037\uFE0F\u20E3")
        val EIGHT = Emoji("\u0038\uFE0F\u20E3")
        val NINE = Emoji("\u0039\uFE0F\u20E3")
        val INFINITY = Emoji("\u267E")
    }
}