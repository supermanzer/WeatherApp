package com.supermanzer.weatherapp.api

// NWS text products (e.g. AFD) are hard-wrapped at ~69 columns for fixed-width
// terminal display. Section headers look like ".SHORT TERM..."; "&&" and "$$"
// are structural delimiters with no display meaning. Compose already wraps
// text to its container, so the original hard line breaks just chop mid-
// sentence lines short — this rebuilds real paragraphs while keeping headers
// and blank-line-separated blocks (paragraphs, bullets) distinct.
private val SECTION_HEADER = Regex("^\\.[A-Z][A-Z0-9 /,'-]*\\.\\.\\.$")

fun formatNwsProductText(raw: String): String {
    val output = StringBuilder()
    val paragraph = StringBuilder()

    fun flushParagraph() {
        if (paragraph.isNotEmpty()) {
            if (output.isNotEmpty()) output.append("\n\n")
            output.append(paragraph)
            paragraph.clear()
        }
    }

    raw.lineSequence().forEach { rawLine ->
        val line = rawLine.trim()
        when {
            line.isEmpty() || line == "&&" || line == "$$" -> flushParagraph()
            SECTION_HEADER.matches(line) -> {
                flushParagraph()
                if (output.isNotEmpty()) output.append("\n\n")
                output.append(line)
            }
            else -> {
                if (paragraph.isNotEmpty()) paragraph.append(' ')
                paragraph.append(line)
            }
        }
    }
    flushParagraph()

    return output.toString()
}
