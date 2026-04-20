#!/usr/bin/env python3
import random

subjects = ["Аз", "Ти", "Той", "Ние", "Вие", "Те"]
question_subjects = ["Ти", "Той", "Вие", "Те"]

subject_ru = {
    "Аз": "Я", "Ти": "Ты", "Той": "Он",
    "Ние": "Мы", "Вие": "Вы", "Те": "Они"
}

ru_future = {
    "Аз": "буду", "Ти": "будешь", "Той": "будет",
    "Ние": "будем", "Вие": "будете", "Те": "будут"
}

verbs = [
    {
        "infinitive": "уча",
        "present": {"Аз": "уча", "Ти": "учиш", "Той": "учи", "Ние": "учим", "Вие": "учите", "Те": "учат"},
        "ru_present": {"Аз": "учусь", "Ти": "учишься", "Той": "учится", "Ние": "учимся", "Вие": "учитесь", "Те": "учатся"}
    },
    {
        "infinitive": "правя",
        "present": {"Аз": "правя", "Ти": "правиш", "Той": "прави", "Ние": "правим", "Вие": "правите", "Те": "правят"},
        "ru_present": {"Аз": "делаю", "Ти": "делаешь", "Той": "делает", "Ние": "делаем", "Вие": "делаете", "Те": "делают"}
    },
    {
        "infinitive": "отивам",
        "present": {"Аз": "отивам", "Ти": "отиваш", "Той": "отива", "Ние": "отиваме", "Вие": "отивате", "Те": "отиват"},
        "ru_present": {"Аз": "иду", "Ти": "идёшь", "Той": "идёт", "Ние": "идём", "Вие": "идёте", "Те": "идут"}
    }
]

types = ["PRESENT", "PRESENT_QUESTION", "PRESENT_NEGATIVE", "FUTURE", "FUTURE_QUESTION", "FUTURE_NEGATIVE"]

def generate_exercise(exercise_id):
    sentence_type = random.choice(types)
    verb = random.choice(verbs)

    if sentence_type in ["PRESENT", "PRESENT_NEGATIVE", "FUTURE", "FUTURE_NEGATIVE"]:
        subject = random.choice(subjects)
    else:
        subject = random.choice(question_subjects)

    bg_verb = verb["present"][subject]
    ru_subject = subject_ru[subject]
    ru_verb = verb["ru_present"][subject]
    ru_future_verb = ru_future[subject]

    # Build correct answer
    if sentence_type == "PRESENT":
        correct_words = [subject, bg_verb]
        source_text = f"{ru_subject} {ru_verb}"
    elif sentence_type == "PRESENT_QUESTION":
        correct_words = [subject, bg_verb, "ли"]
        source_text = f"{ru_subject} {ru_verb}?"
    elif sentence_type == "PRESENT_NEGATIVE":
        correct_words = [subject, "не", bg_verb]
        source_text = f"{ru_subject} не {ru_verb}"
    elif sentence_type == "FUTURE":
        correct_words = [subject, "ще", bg_verb]
        source_text = f"{ru_subject} {ru_future_verb} {verb['infinitive']}"
    elif sentence_type == "FUTURE_QUESTION":
        correct_words = [subject, "ще", bg_verb, "ли"]
        source_text = f"{ru_subject} {ru_future_verb} {verb['infinitive']}?"
    else:  # FUTURE_NEGATIVE
        correct_words = [subject, "няма", "да", bg_verb]
        source_text = f"{ru_subject} не {ru_future_verb} {verb['infinitive']}"

    # Build distractors pool
    all_verbs = []
    for v in verbs:
        all_verbs.extend(v["present"].values())

    pool = list(set(subjects + ["ли", "не", "ще", "няма", "да"] + all_verbs))
    distractors = [w for w in pool if w not in correct_words]
    random.shuffle(distractors)
    distractors = distractors[:8]  # Take 8

    # Combine
    available_words = list(set(correct_words + distractors))
    random.shuffle(available_words)

    return {
        "id": exercise_id,
        "type": sentence_type,
        "source_text": source_text,
        "correct_words": correct_words,
        "available_words": available_words
    }

print("\n" + "="*60)
print("  10 TEST EXERCISES - Lesson 1")
print("="*60 + "\n")

for i in range(10):
    ex = generate_exercise(i + 1)

    print(f"📝 Exercise {i+1} ({ex['type']})")
    print(f"   🇷🇺 Russian: {ex['source_text']}")
    print(f"   ✅ Correct answer: {' '.join(ex['correct_words'])}")
    print(f"   📦 Available words ({len(ex['available_words'])}): {', '.join(ex['available_words'])}")

    # Check if all correct words are in available
    missing = [w for w in ex['correct_words'] if w not in ex['available_words']]
    if missing:
        print(f"   ❌ ERROR: Missing words in available: {missing}")
    else:
        print(f"   ✓ All correct words present")
    print()
