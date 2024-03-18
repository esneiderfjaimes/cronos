package cronos.core.data.utils

suspend fun <T> executeToResult(block: suspend () -> T) = try {
    Result.success(block())
} catch (e: Exception) {
    e.printStackTrace()
    Result.failure(e)
}