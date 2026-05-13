package com.wastingnotime.contactsmobile.infrastructure.http

object ContactsJsonParser {
    fun parseContacts(payload: String): List<RemoteContact> {
        val objects = extractObjects(payload)
        return objects.map { parseContactObject(it) }
    }

    fun parseContact(payload: String): RemoteContact {
        return parseContactObject(payload)
    }

    private fun extractObjects(payload: String): List<String> {
        val trimmed = payload.trim()
        if (trimmed == "[]") {
            return emptyList()
        }
        require(trimmed.startsWith("[") && trimmed.endsWith("]")) {
            "Expected a JSON array of contacts."
        }

        val results = mutableListOf<String>()
        var depth = 0
        var inString = false
        var escape = false
        var startIndex = -1

        for (index in 1 until trimmed.length - 1) {
            val char = trimmed[index]
            if (escape) {
                escape = false
                continue
            }
            when (char) {
                '\\' -> if (inString) escape = true
                '"' -> inString = !inString
                '{' -> if (!inString) {
                    if (depth == 0) {
                        startIndex = index
                    }
                    depth++
                }
                '}' -> if (!inString) {
                    depth--
                    if (depth == 0 && startIndex >= 0) {
                        results += trimmed.substring(startIndex, index + 1)
                        startIndex = -1
                    }
                }
            }
        }

        return results
    }

    private fun parseContactObject(objectPayload: String): RemoteContact {
        return RemoteContact(
            id = extractField(objectPayload, "id"),
            first_name = extractField(objectPayload, "first_name"),
            last_name = extractField(objectPayload, "last_name"),
            phone_number = extractField(objectPayload, "phone_number"),
        )
    }

    private fun extractField(objectPayload: String, fieldName: String): String {
        val pattern = Regex(""""$fieldName"\s*:\s*"([^"]*)"""")
        val match = pattern.find(objectPayload)
            ?: throw IllegalArgumentException("Missing field: $fieldName")
        return unescapeJsonString(match.groupValues[1])
    }

    private fun unescapeJsonString(value: String): String {
        val builder = StringBuilder(value.length)
        var escape = false
        for (char in value) {
            if (escape) {
                builder.append(
                    when (char) {
                        '"', '\\', '/' -> char
                        'b' -> '\b'
                        'f' -> '\u000C'
                        'n' -> '\n'
                        'r' -> '\r'
                        't' -> '\t'
                        else -> char
                    },
                )
                escape = false
            } else if (char == '\\') {
                escape = true
            } else {
                builder.append(char)
            }
        }
        if (escape) {
            throw IllegalArgumentException("Invalid escaped string.")
        }
        return builder.toString()
    }
}
