import kotlin.random.Random

// Simplified version to test exercise generation logic
data class VerbForms(
    val infinitive: String,
    val present: Map<String, String>,
    val ruPresent: Map<String, String>
)

val subjects = listOf("Аз", "Ти", "Той", "Ние", "Вие", "Те")
val questionSubjects = listOf("Ти", "Той", "Вие", "Те")

val subjectRu = mapOf(
    "Аз" to "Я",
    "Ти" to "Ты",
    "Той" to "Он",
    "Ние" to "Мы",
    "Вие" to "Вы",
    "Те" to "Они"
)

val ruFuture = mapOf(
    "Аз" to "буду",
    "Ти" to "будешь",
    "Той" to "будет",
    "Ние" to "будем",
    "Вие" to "будете",
    "Те" to "будут"
)

val verbs = listOf(
    VerbForms(
        infinitive = "уча",
        present = mapOf(
            "Аз" to "уча",
            "Ти" to "учиш",
            "Той" to "учи",
            "Ние" to "учим",
            "Вие" to "учите",
            "Те" to "учат"
        ),
        ruPresent = mapOf(
            "Аз" to "учусь",
            "Ти" to "учишься",
            "Той" to "учится",
            "Ние" to "учимся",
            "Вие" to "учитесь",
            "Те" to "учатся"
        )
    ),
    VerbForms(
        infinitive = "правя",
        present = mapOf(
            "Аз" to "правя",
            "Ти" to "правиш",
            "Той" to "прави",
            "Ние" to "правим",
            "Вие" to "правите",
            "Те" to "правят"
        ),
        ruPresent = mapOf(
            "Аз" to "делаю",
            "Ти" to "делаешь",
            "Той" to "делает",
            "Ние" to "делаем",
            "Вие" to "делаете",
            "Те" to "делают"
        )
    )
)

enum class SentenceType {
    PRESENT, PRESENT_QUESTION, PRESENT_NEGATIVE,
    FUTURE, FUTURE_QUESTION, FUTURE_NEGATIVE
}

fun generateExercise(id: Int): Map<String, Any> {
    val type = SentenceType.values().random()
    val verb = verbs.random()
    val subject = when (type) {
        SentenceType.PRESENT, SentenceType.PRESENT_NEGATIVE,
        SentenceType.FUTURE, SentenceType.FUTURE_NEGATIVE -> subjects.random()
        SentenceType.PRESENT_QUESTION, SentenceType.FUTURE_QUESTION -> questionSubjects.random()
    }

    val bgVerb = verb.present.getValue(subject)
    val ruSubject = subjectRu.getValue(subject)
    val ruVerb = verb.ruPresent.getValue(subject)
    val ruFutureVerb = ruFuture.getValue(subject)

    val correctWords = when (type) {
        SentenceType.PRESENT -> listOf(subject, bgVerb)
        SentenceType.PRESENT_QUESTION -> listOf(subject, bgVerb, "ли")
        SentenceType.PRESENT_NEGATIVE -> listOf(subject, "не", bgVerb)
        SentenceType.FUTURE -> listOf(subject, "ще", bgVerb)
        SentenceType.FUTURE_QUESTION -> listOf(subject, "ще", bgVerb, "ли")
        SentenceType.FUTURE_NEGATIVE -> listOf(subject, "няма", "да", bgVerb)
    }

    val sourceText = when (type) {
        SentenceType.PRESENT -> "$ruSubject $ruVerb"
        SentenceType.PRESENT_QUESTION -> "$ruSubject $ruVerb?"
        SentenceType.PRESENT_NEGATIVE -> "$ruSubject не $ruVerb"
        SentenceType.FUTURE -> "$ruSubject $ruFutureVerb учиться" // simplified
        SentenceType.FUTURE_QUESTION -> "$ruSubject $ruFutureVerb учиться?"
        SentenceType.FUTURE_NEGATIVE -> "$ruSubject не $ruFutureVerb учиться"
    }

    // Build distractors
    val pool = (subjects + listOf("ли", "не", "ще", "няма", "да") + verbs.flatMap { it.present.values }).distinct()
    val distractors = pool.filterNot { it in correctWords }.shuffled().take(8)
    val availableWords = (correctWords + distractors).distinct().shuffled()

    return mapOf(
        "id" to id,
        "sourceText" to sourceText,
        "correctWords" to correctWords,
        "availableWords" to availableWords,
        "type" to type.name
    )
}

fun main() {
    println("\n=== 10 Test Exercises ===\n")

    repeat(10) { i ->
        val ex = generateExercise(i + 1)
        val correct = ex["correctWords"] as List<*>
        val available = ex["availableWords"] as List<*>

        println("📝 Exercise ${i + 1} (${ex["type"]})")
        println("   🇷🇺 Russian: ${ex["sourceText"]}")
        println("   ✅ Correct: ${correct.joinToString(" ")}")
        println("   📦 Available (${available.size}): ${available.joinToString(", ")}")

        val missing = correct.filterNot { it in available }
        if (missing.isNotEmpty()) {
            println("   ❌ ERROR: Missing words: $missing")
        } else {
            println("   ✓ OK")
        }
        println()
    }
}

main()
