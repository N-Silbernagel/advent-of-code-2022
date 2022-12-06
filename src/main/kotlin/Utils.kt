import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readFileAsList(name: String) = File("src/main/kotlin", "$name.txt")
    .readLines()
