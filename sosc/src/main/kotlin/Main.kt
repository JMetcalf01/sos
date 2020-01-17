
/**
 * The entry point into the program. The first argument should be
 * the file written in sos that they want compiled.
 *
 * @author Jonathan Metcalf
 *
 * @param args the arguments to the function
 */
fun main(args: Array<String>) {
    Parser(args[0]).parse()
}

/**
 * Parses a sos file and compiles it.
 *
 * @author Jonathan Metcalf and Martin Bleakley
 *
 * @property path the path of the file to be compiled
 */
class Parser constructor(private val path: String) {
    fun parse() {
        print(Emojis.ONE)
    }
}