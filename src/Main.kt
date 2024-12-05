import java.io.File

fun main() {
    val filePath = "resources/source.txt"
    val numbers = readNumbersFromFile(filePath)

    if (numbers.isEmpty()) {
        println("Файл порожній або не знайдено чисел.")
        return
    }

    val result = findLongestSequence(numbers)
    println("Найдовший ланцюжок: $result")
}

fun readNumbersFromFile(filePath: String): List<String> {
    return try {
        File(filePath).readLines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && it.all { char -> char.isDigit() } }
    } catch (e: Exception) {
        println("Помилка читання файлу: ${e.message}")
        emptyList()
    }
}

fun findLongestSequence(numbers: List<String>): String {
    val graph = mutableMapOf<String, MutableList<String>>()

    // Створюємо граф
    for (num in numbers) {
        val prefix = num.takeLast(2)
        graph.computeIfAbsent(prefix) { mutableListOf() }
    }

    for (num in numbers) {
        val suffix = num.take(2)
        if (graph.containsKey(suffix)) {
            graph[suffix]?.add(num)
        }
    }

    // Знаходимо найдовший шлях
    var longestPath = listOf<String>()
    val visited = mutableSetOf<String>()

    fun dfs(path: List<String>, current: String) {
        if (path.size > longestPath.size) {
            longestPath = path
        }

        graph[current]?.forEach { next ->
            if (!visited.contains(next)) {
                visited.add(next)
                dfs(path + next, next.takeLast(2))
                visited.remove(next)
            }
        }
    }

    for (start in numbers) {
        visited.add(start)
        dfs(listOf(start), start.takeLast(2))
        visited.remove(start)
    }

    return longestPath.joinToString("")
}
