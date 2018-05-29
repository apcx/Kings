package apc.kings.common

fun underscoresToCamelCase(underscores: String): String {
    var first = true
    return underscores.split("_").joinToString("") {
        if (first) {
            first = false
            it
        } else it.capitalize()
    }
}