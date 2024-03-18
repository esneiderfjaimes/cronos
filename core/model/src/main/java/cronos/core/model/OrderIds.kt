package cronos.core.model

typealias Ids = List<Long>

fun Ids.compact(): String = IdsParser.to(this)

object IdsParser {

    fun from(ids: String): Ids {
        if (ids.isBlank()) return emptyList()
        return ids.split(SEPARATOR)
            .takeIf { it.isNotEmpty() }
            ?.map { it.toLong() }
            ?: emptyList()
    }

    fun to(ids: Ids): String {
        return ids.joinToString(SEPARATOR)
    }

    private const val SEPARATOR = ","
}
