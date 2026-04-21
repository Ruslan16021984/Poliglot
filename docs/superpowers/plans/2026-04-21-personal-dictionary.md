# Personal Dictionary Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a personal dictionary feature with manual and voice-assisted word entry, multi-group assignment, compact word browsing, and flashcard training for all words or one selected group.

**Architecture:** Add a small Room-backed dictionary subsystem alongside the existing lesson subsystem. Keep the current app style: repository + AndroidViewModel + Compose screens + navigation routes, without introducing a full DI framework. Voice input should use the Android speech recognition intent on the add/edit screen, while flashcard training remains a separate focused screen with local swipe state.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, Navigation Compose, Room, Kotlin coroutines/Flow, Android speech recognition intent, JUnit, Gradle.

---

### Task 1: Add database dependencies and dictionary data layer skeleton

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/data/dictionary/WordCardEntity.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/data/dictionary/WordGroupEntity.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/data/dictionary/WordCardGroupCrossRef.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/data/dictionary/WordCardWithGroups.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/data/dictionary/WordCardDao.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/data/dictionary/WordGroupDao.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/data/dictionary/PersonalDictionaryDatabase.kt`

- [ ] Add Room version entries and KSP plugin entry in `gradle/libs.versions.toml`.
- [ ] Add `alias(libs.plugins.ksp)` and Room dependencies in `app/build.gradle.kts`.
- [ ] Define `WordCardEntity` with `id`, `bgWord`, `ruTranslation`, `createdAt`, `updatedAt`.
- [ ] Define `WordGroupEntity` with `id` and unique `name`.
- [ ] Define the many-to-many cross-ref entity `WordCardGroupCrossRef`.
- [ ] Define `WordCardWithGroups` relation model for UI/repository mapping.
- [ ] Add DAO contracts for:
  - observing all words
  - searching words by query
  - inserting/updating/deleting a word
  - inserting/deleting group cross-refs
  - observing all groups with counts
  - creating a group
- [ ] Add `PersonalDictionaryDatabase` singleton with Room `databaseBuilder(...)`.

### Task 2: Implement repository and dictionary domain mapping

**Files:**
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/model/dictionary/WordCard.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/model/dictionary/WordGroup.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/model/dictionary/DictionaryWordListItem.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/model/dictionary/FlashcardItem.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/data/dictionary/PersonalDictionaryRepository.kt`

- [ ] Add UI/domain models that do not expose Room annotations outside the data layer.
- [ ] Implement repository methods for:
  - observe all words
  - observe filtered words by query and optional group
  - observe groups with word counts
  - get word by id for edit screen
  - save word with multiple group ids
  - create group
  - delete word
  - load flashcards for all words
  - load flashcards for one group
- [ ] Keep save flow simple:
  - insert or update `WordCardEntity`
  - replace group cross-refs for that word
  - update `updatedAt`
- [ ] Keep repository API aligned with the approved MVP only. Do not add spaced repetition or examples yet.

### Task 3: Add navigation routes and shared dictionary UI models

**Files:**
- Modify: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/navigation/Destinations.kt`
- Modify: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/navigation/AppNavGraph.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/ui/dictionary/DictionaryListUiState.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/ui/dictionary/WordEditorUiState.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/ui/dictionary/FlashcardTrainingUiState.kt`

- [ ] Add new destinations:
  - `DICTIONARY_LIST`
  - `DICTIONARY_EDIT`
  - `DICTIONARY_TRAINING`
- [ ] Support navigation arguments for:
  - optional `wordId` on edit screen
  - optional `groupId` and `groupName` on training screen
- [ ] Keep lessons as the app start screen for now, and add entry into the dictionary from the lessons list screen rather than replacing the root flow.
- [ ] Add small focused UI state classes so each screen owns only the state it needs.

### Task 4: Add dictionary list screen with search, groups, and train actions

**Files:**
- Modify: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/ui/lessons/LessonsScreen.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/viewmodel/DictionaryViewModel.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/ui/dictionary/DictionaryScreen.kt`

- [ ] Add a visible entry action from the lessons screen into the dictionary flow.
- [ ] Implement `DictionaryViewModel` with:
  - query state
  - selected group state
  - combined list flow
  - groups flow
  - delete action
- [ ] Build `DictionaryScreen` with:
  - title `Мои слова`
  - add action
  - search field
  - primary button `Учить все слова`
  - group chips or compact group block
  - per-group `Учить` action
  - compact word rows with Bulgarian primary line and Russian secondary line
- [ ] Add empty states:
  - no words
  - no groups
- [ ] Keep the list screen visually aligned with the current lessons screen redesign: light background, soft blue accents, compact rows, no bottom navigation.

### Task 5: Add add/edit screen with group assignment and voice input

**Files:**
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/viewmodel/WordEditorViewModel.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/ui/dictionary/WordEditorScreen.kt`

- [ ] Implement `WordEditorViewModel` to load existing word data when `wordId` is present.
- [ ] Track:
  - Bulgarian text
  - Russian text
  - selected group ids
  - inline new-group dialog state
  - save button enabled state
- [ ] Build the screen with both text fields visible at the same time.
- [ ] Add microphone icons to both fields.
- [ ] Use `rememberLauncherForActivityResult` + `RecognizerIntent.ACTION_RECOGNIZE_SPEECH` so recognized text is inserted into the correct field.
- [ ] Add group chips and `+ Новая группа`.
- [ ] Keep `Дополнительно` collapsed and non-functional for future expansion, or omit its contents until needed.
- [ ] Use different primary button text for create and edit mode:
  - `Добавить слово`
  - `Сохранить изменения`
- [ ] Show lightweight validation:
  - both main fields required
  - trim whitespace before save

### Task 6: Add flashcard training screen and training state management

**Files:**
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/viewmodel/FlashcardTrainingViewModel.kt`
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/ui/dictionary/FlashcardTrainingScreen.kt`

- [ ] Implement loading for two modes:
  - all words
  - one selected group
- [ ] Keep the training model minimal:
  - current index
  - current card face
  - known count
  - unknown count
  - finished flag
- [ ] Build the UI exactly to the approved interaction:
  - back button
  - title `Тренировка`
  - progress text like `12 / 84`
  - small pill label with `Все слова` or group name
  - one large centered card
  - tap to flip
  - swipe up = know
  - swipe down = do not know
- [ ] Do not add save buttons, filter controls, or extra action bars to this screen.
- [ ] Add simple end-of-training state with counts and action to return to dictionary.

### Task 7: Add focused tests for the new dictionary subsystem

**Files:**
- Create: `app/src/test/java/com/carbit3333333/oiiglot_bulgary/PersonalDictionaryRepositoryTest.kt`
- Create: `app/src/test/java/com/carbit3333333/oiiglot_bulgary/DictionaryFilteringTest.kt`
- Modify: `app/build.gradle.kts`

- [ ] Add only the minimum new test dependencies needed by the chosen implementation.
- [ ] Cover repository behavior:
  - save word without groups
  - save word with multiple groups
  - edit word and replace selected groups
  - delete word
  - load flashcards for all words
  - load flashcards for one group
- [ ] Cover filtering behavior:
  - query matches Bulgarian word
  - query matches Russian translation
  - group filter narrows results
- [ ] Prefer JVM tests for mapping/filter logic. If Room forces Android instrumentation for a specific case, isolate that to one narrow database test instead of moving the whole test suite to androidTest.

### Task 8: Verify integration and polish

**Files:**
- Modify: `docs/superpowers/specs/2026-04-21-personal-dictionary-design.md` (only if the implemented behavior needs a small clarified note)

- [ ] Run unit tests for the dictionary and existing lesson tests.
- [ ] Run the full test suite if the incremental dictionary tests are green.
- [ ] Manually verify on device or emulator:
  - open dictionary from lessons screen
  - add word manually
  - use voice input for Bulgarian
  - use voice input for Russian
  - create a new group
  - assign one word to multiple groups
  - edit a word
  - delete a word
  - train all words
  - train one group
  - flip card and swipe both directions
- [ ] Keep the scope limited to the approved MVP. Defer auto-translate, examples, TTS playback, and spaced repetition.
