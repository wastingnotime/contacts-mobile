package org.wastingnotime.contactsmobile.infrastructure.http

data class RemoteContactRequest(
    val first_name: String,
    val last_name: String,
    val phone_number: String,
) {
    fun toJson(): String {
        return buildString {
            append('{')
            append("\"first_name\":\"")
            append(escapeJson(first_name))
            append("\",\"last_name\":\"")
            append(escapeJson(last_name))
            append("\",\"phone_number\":\"")
            append(escapeJson(phone_number))
            append("\"}")
        }
    }

    private fun escapeJson(value: String): String {
        val builder = StringBuilder(value.length)
        for (char in value) {
            when (char) {
                '\\' -> builder.append("\\\\")
                '"' -> builder.append("\\\"")
                '\b' -> builder.append("\\b")
                '\u000C' -> builder.append("\\f")
                '\n' -> builder.append("\\n")
                '\r' -> builder.append("\\r")
                '\t' -> builder.append("\\t")
                else -> builder.append(char)
            }
        }
        return builder.toString()
    }
}
