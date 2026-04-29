package com.carbit3333333.oiiglot_bulgary.data.lesson_session

internal fun buildAvailableWords(
    correctWords: List<String>,
    distractorPool: List<String>,
    totalWords: Int = 8
): List<String> {
    val uniqueCorrectWords = correctWords.distinct()

    require(uniqueCorrectWords.isNotEmpty()) {
        "Correct words must not be empty"
    }

    require(uniqueCorrectWords.size <= totalWords) {
        "Correct words count (${uniqueCorrectWords.size}) can't be greater than totalWords ($totalWords)"
    }

    val distractors = distractorPool
        .filterNot { it in uniqueCorrectWords }
        .distinct()
        .shuffled()
        .take(totalWords - uniqueCorrectWords.size)

    val result = (uniqueCorrectWords + distractors).shuffled()

    require(result.size == totalWords) {
        "Available words size must be $totalWords, but was ${result.size}. Correct=$uniqueCorrectWords, distractors=$distractors"
    }

    require(uniqueCorrectWords.all { it in result }) {
        "Not all correct words were added. Correct=$uniqueCorrectWords, result=$result"
    }

    return result
}
