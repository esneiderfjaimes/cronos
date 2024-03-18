package cronos.core.model

import androidx.annotation.Size

sealed interface Brush {
    companion object {
        fun parse(input: String): Brush {
            return when {
                input.startsWith("radial-gradient") -> RadialGradient.parse(input)
                input.startsWith("linear-gradient") -> LinearGradient.parse(input)
                input.startsWith("solid-color") -> SolidBackground.parse(input)
                else -> throw IllegalArgumentException("Invalid input format")
            }
        }
    }
}

class SolidBackground(val color: Int) : Brush {
    companion object {
        fun parse(input: String): SolidBackground {
            val regex = Regex("""solid-color\((#[a-zA-Z0-9]+)\)""")
            val matchResult =
                regex.find(input) ?: throw IllegalArgumentException("Invalid input format")
            val color = parseColor(matchResult.groupValues[1])
            return SolidBackground(color)
        }
    }
}

class RadialGradient(
    val colorStops: Array<Pair<Float, Int>>,
) : Brush {
    companion object {
        fun parse(input: String): RadialGradient {
            val regex = Regex("""radial-gradient\((.+)\)""")
            val matchResult =
                regex.find(input) ?: throw IllegalArgumentException("Invalid input format")

            // color stops
            val groupColorStops = matchResult.groupValues[1]
            val colorStops = matchesToColorStops(groupColorStops)

            return RadialGradient(colorStops.toTypedArray())
        }
    }
}

class LinearGradient(
    val direction: Direction,
    val colorStops: Array<Pair<Float, Int>>,
) : Brush {
    companion object {
        fun parse(input: String): LinearGradient {
            val regex =
                Regex("""linear-gradient\(direction:(top|right|bottom|left|top-left|top-right|bottom-left|bottom-right), (.+)\)""")
            val matchResult =
                regex.find(input) ?: throw IllegalArgumentException("Invalid input format")

            // direction
            val groupDirection = matchResult.groupValues[1]
            val direction = Direction.entries.first { it.value == groupDirection }

            // color stops
            val groupColorStops = matchResult.groupValues[2]
            val colorStops = matchesToColorStops(groupColorStops)

            return LinearGradient(direction, colorStops.toTypedArray())
        }
    }

    enum class Direction(val value: String) {
        TOP("top"),
        RIGHT("right"),
        BOTTOM("bottom"),
        LEFT("left"),
        TOP_LEFT("top-left"),
        TOP_RIGHT("top-right"),
        BOTTOM_LEFT("bottom-left"),
        BOTTOM_RIGHT("bottom-right");
    }
}

private fun matchesToColorStops(input: String): List<Pair<Float, Int>> {
    val regexColorStops = Regex("""([0,1](?:\.\d+)?) to (#[a-zA-Z0-9]+)""")
    return regexColorStops.findAll(input).map {
        val percentage = it.groupValues[1].toFloat()
        val color = parseColor(it.groupValues[2])
        percentage to color
    }.toList()
}

private fun parseColor(@Size(min = 1) colorString: String): Int {
    if (colorString[0] == '#') {
        // Use a long to avoid rollovers on #ffXXXXXX
        var color = colorString.substring(1).toLong(16)
        if (colorString.length == 7) {
            // Set the alpha value
            color = color or 0x00000000ff000000L
        } else require(colorString.length == 9) { "Unknown color" }
        return color.toInt()
    }
    throw IllegalArgumentException("Unknown color")
}
