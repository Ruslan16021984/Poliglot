# Lesson 10 Design: Time and Daily Routine

## Goal

Add a new A1 lesson focused on practical time expressions and daily routine.
The lesson should teach learners to understand and build common everyday phrases
about schedules, work, study, meetings, errands, and returning home.

This lesson is not a pure grammar lesson. It is a situational lesson where time
prepositions and time expressions are introduced through useful phrases.

## Lesson Theme

Lesson title:

- `Урок 10`
- `Время и распорядок дня`

Core user outcome:

- understand when something happens
- say when they work, study, meet, drink coffee, go to the store, and return home
- use the most important A1 time constructions in short complete sentences

## Scope

Version 1 of the lesson will include:

- days of the week in a small controlled set
- parts of the day
- simple clock-time phrases
- the prepositions and structures `в`, `след`, `преди`, `от ... до ...`
- a small set of daily-routine verbs
- translation/building exercises in the same style as the current app

Version 1 will not include:

- advanced date expressions
- month and year grammar
- complex past/future scheduling
- restaurant vocabulary
- city-direction vocabulary
- open-ended dialogue

## User-Facing Content

### Theory Blocks

The lesson theory should explain the topic with short practical examples.

Planned theory sections:

1. `Когда происходит действие`
   Explain that in Bulgarian time is often expressed through ready-made phrases.

   Example ideas:
   - `в понеделник`
   - `в два часа`
   - `сутрин`
   - `вечер`

2. `До и после`
   Introduce:
   - `преди обяд`
   - `след работа`

3. `С ... до ...`
   Introduce time intervals:
   - `от девет до пет`
   - `от два до три`

4. `Полезные фразы`
   Complete practical sentences:
   - `В понеделник работя.`
   - `В два часа имам урок.`
   - `Сутрин пия кафе.`
   - `След работа отивам в магазина.`
   - `Вечер се прибирам вкъщи.`

### Vocabulary

Version 1 vocabulary should stay intentionally small and safe.

Days:

- `понеделник`
- `вторник`
- `сряда`

Parts of day:

- `сутрин`
- `следобед`
- `вечер`

Simple time anchors:

- `днес`
- `утре`

Time prepositions / structures:

- `в`
- `след`
- `преди`
- `от`
- `до`

Clock values:

- `един`
- `два`
- `три`
- `пет`
- `девет`

Routine nouns:

- `работа`
- `урок`
- `магазин`
- `кафе`
- `вкъщи`

Routine verbs:

- `работя`
- `имам`
- `уча`
- `отивам`
- `пия`
- `почивам`
- `се прибирам`

## Architecture

The lesson should follow the existing hybrid architecture:

- JSON stores lesson content and phrase-building data
- Kotlin handles generation logic and exercise assembly

### Assets Changes

#### `lessons.json`

Add lesson 10 metadata:

- `id = 10`
- title
- subtitle
- theory blocks

#### `lesson_session_content.json`

Add a dedicated lesson 10 block with structured content.

Recommended new asset models:

- `lesson10TimeAnchors`
- `lesson10Intervals`
- `lesson10Templates`

If needed, a more compact structure is also acceptable, but the data should stay
lesson-specific and not be hardcoded in the generator.

## Generator Design

Add a new generator:

- `Lesson10RealGenerator`

The generator should keep the same style as the current lesson generators:

- produce `List<LessonExercise>`
- return `sourceText`
- return `correctAnswerWords`
- return `availableWords`

### Sentence Types

Version 1 should generate a controlled mix of:

1. Day-based routine
   - `В понеделник работя.`
   - `Във вторник имам урок.`

2. Part-of-day routine
   - `Сутрин пия кафе.`
   - `Вечер се прибирам вкъщи.`

3. Before/after phrases
   - `Преди обяд уча.`
   - `След работа отивам в магазина.`

4. Time-of-day phrases
   - `В два часа имам урок.`
   - `В пет часа почивам.`

5. Interval phrases
   - `Работя от девет до пет.`
   - `Уча от два до три.`

### Practical Simplifications

To keep the lesson A1-safe:

- do not introduce too many verb persons at once
- prefer first person singular and a few second person questions
- use a small controlled pool of verbs and nouns
- keep questions limited to clear patterns if added later

## Exercise Strategy

Version 1 exercises should focus on building correct phrases rather than free production.

Recommended distribution:

- 40 percent statement templates
- 30 percent before/after templates
- 30 percent interval and clock-time templates

Initial lesson size:

- `60` exercises, consistent with recent lessons

## Testing

Add tests for:

- lesson 10 content loads from JSON
- lesson 10 session builds valid exercises
- generated exercises include:
  - `след`
  - `преди`
  - `от`
  - `до`
  - at least one day-of-week sentence
  - at least one clock-time sentence

## Why This Design

This lesson is intentionally practical.
It avoids turning time prepositions into a dry grammar table and instead teaches
the learner to use them in meaningful daily situations.

It also creates a clean path for future thematic lessons:

- restaurant / food
- city and directions
- rent and housing

without overloading lesson 10 with unrelated vocabulary.
