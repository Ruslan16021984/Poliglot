# Course To Textbook Alignment

This file fixes the target course structure so the app lessons follow the
textbook lesson order instead of the older grammar-first sequence.

Goal:
- `Lesson 1..11` in the app must match `lesson 1..11` in the textbook.
- Exercises should come from fixed JSON textbook data.
- Grammar should support the textbook topic, not replace it.

## Target Lesson Order

| New App Lesson | Textbook Lesson | Theme | Status | Reuse |
|---|---:|---|---|---|
| 1 | 1 | Greeting, introduction, nationality | started | `textbook_exercises_lesson1.json` |
| 2 | 2 | Food and breakfast | pending | textbook words from lesson 2, part of old lesson 4 verbs |
| 3 | 3 | Restaurant | pending | textbook words from lesson 3, part of old lesson 4 verbs |
| 4 | 4 | Shopping, supermarket, market | started | `textbook_exercises_lesson4.json` |
| 5 | 5 | City, address, directions | pending | old lesson 6 prepositions + textbook lesson 5 vocabulary |
| 6 | 6 | Family | sample ready | `textbook_exercises_lesson6.json`, part of old lesson 2 and old lesson 7 |
| 7 | 7 | Weather, days, months, time | pending | part of old lesson 10 |
| 8 | 8 | Clothes and colors | pending | mostly new textbook-driven content |
| 9 | 9 | Home, rooms, furniture | pending | mostly new textbook-driven content |
| 10 | 10 | Transport and road | pending | part of old lesson 5 and old lesson 6 |
| 11 | 11 | Daily routine | pending | part of old lesson 10 |

## Old Grammar Lessons And Their Fate

The current course contains grammar-heavy lessons that do not match the
textbook order. They should not stay as the main user-facing lesson sequence.

| Old App Lesson | Old Theme | New Fate |
|---:|---|---|
| 1 | Verb forms | fold into textbook lesson 1 as support grammar |
| 2 | `съм` | split between textbook lessons 1, 5, 6 |
| 3 | Past tense | keep as support grammar later, not a main early textbook lesson |
| 4 | Object or action | split between textbook lessons 2, 3, 4 |
| 5 | Can, want, need | split between textbook lessons 3, 4, 10, 11 |
| 6 | Prepositions and nouns | split between textbook lessons 5, 10 |
| 7 | Possessives and article | split between textbook lessons 6, 9 |
| 8 | Comparison | move to a later support block after core textbook path |
| 9 | Numbers | distribute into textbook lessons 4, 5, 6 |
| 10 | Time and routine | split between textbook lessons 7 and 11 |

## Migration Rules

1. New textbook order is the source of truth.
2. Existing grammar content may be reused only as support inside the matching textbook lesson.
3. New user-facing lessons must use fixed JSON exercises, not random sentence generation.
4. Every new lesson should eventually have:
   - textbook theory
   - textbook words
   - textbook phrases
   - textbook exercises with `ru` and `uk`
5. Old grammar-only lessons should disappear from the main path after migration is complete.

## Recommended Migration Order

### Wave 1
- Lesson 1
- Lesson 2
- Lesson 3
- Lesson 4

Reason:
- these lessons define the first impression of the course
- they are the most important for synchronizing book + app

### Wave 2
- Lesson 5
- Lesson 6
- Lesson 7

Reason:
- these lessons already have strong lexical support in `textbook_dictionary_words.json`

### Wave 3
- Lesson 8
- Lesson 9
- Lesson 10
- Lesson 11

Reason:
- they require more new content and less reuse from the old app structure

## Immediate Practical Next Steps

1. Keep textbook exercise runtime enabled for lesson 1 and lesson 4.
2. Build `textbook_exercises_lesson2.json`.
3. Build `textbook_exercises_lesson3.json`.
4. Build `textbook_exercises_lesson5.json`.
5. Promote `textbook_exercises_lesson6.json` from sample to runtime once lesson sequence is switched.
6. After lessons 1–6 exist in textbook order, rename the visible lesson metadata to match the textbook.

## Current Ready Assets

- `app/src/main/assets/textbook_dictionary_words.json`
- `app/src/main/assets/textbook_exercises_lesson1.json`
- `app/src/main/assets/textbook_exercises_lesson4.json`
- `app/src/main/assets/textbook_exercises_lesson6.json`

## Main Principle

The app should stop asking:
"Which old grammar lesson does this belong to?"

It should ask:
"Which textbook lesson is this for, and which grammar helps the learner survive that topic?"
