# Personal Dictionary Design

Date: 2026-04-21
Project: OIiglot_Bulgary
Status: Draft for review

## Goal

Add a personal dictionary feature inside the app so the user can:

- manually add Bulgarian words and Russian translations
- optionally use voice input for both fields
- assign one word to multiple groups
- browse words in a compact list
- train all words or only one selected group using flashcards

This is a user-managed dictionary, not a built-in static course vocabulary set.

## Why This Feature

The current app teaches through lessons, but the user also needs a fast personal vocabulary tool:

- add only the words they personally need
- keep words grouped by real-life topics like restaurant, travel, road, home
- review words independently from lesson structure

This should become a lightweight companion tool inside the app, not a heavy translator workflow.

## Product Scope

### Included in MVP

- screen `Мои слова`
- screen `Добавить слово`
- screen `Тренировка`
- manual word entry
- voice input for Bulgarian field
- voice input for Russian field
- create groups
- assign one word to multiple groups
- compact word list
- train all words
- train words by one selected group
- edit word
- delete word

### Not Included in MVP

- auto-translation
- auto-generated example sentence
- import/export
- spaced repetition algorithm
- advanced statistics
- AI assistant
- favorites

## Core UX Principles

- word creation must be fast
- manual input stays the default path
- voice input is an accelerator, not a replacement
- list view must stay compact
- the feature should feel calm and practical, not overloaded
- flashcard training should focus on one clear action at a time

## Information Architecture

Three main screens:

1. `Мои слова`
2. `Добавить слово` / `Редактировать слово`
3. `Тренировка`

## Screen Design

### 1. My Words Screen

Purpose:

- central entrypoint to the personal dictionary
- search words
- browse all words
- browse groups
- start training

Main elements:

- top app bar with title `Мои слова`
- action button `+ Добавить`
- search field with placeholder like `Поиск по слову или переводу...`
- primary CTA `Учить все слова`
- groups section
- word list section

Groups behavior:

- groups are shown as chips for fast filtering
- groups are also shown as a list with word count
- each group has action `Учить`

Word list behavior:

- compact rows only
- Bulgarian word is primary line
- Russian translation is secondary line
- tap row opens edit screen
- delete action is available from row actions or long press

### 2. Add / Edit Word Screen

Purpose:

- create a card quickly
- assign it to one or more groups

Main elements:

- top app bar with back button and title
- field `Болгарское слово`
- microphone button for Bulgarian field
- field `Русский перевод`
- microphone button for Russian field
- groups block
- chips for selected groups
- action `+ Новая группа`
- collapsed block `Дополнительно`
- primary CTA `Добавить слово` for create mode
- primary CTA `Сохранить изменения` for edit mode

Input rules:

- both text fields are visible at the same time
- user can type manually in either field
- voice recognition inserts text into the selected field
- user can edit recognition result before saving

Additional block:

- collapsed by default
- reserved for future fields such as example sentence, note, or tag metadata
- not required in MVP

### 3. Flashcard Training Screen

Purpose:

- simple swipe-based review of words

Visual structure:

- top app bar with back button, title `Тренировка`, and progress like `12 / 84`
- small pill label with current source: `Все слова` or selected group name
- one large centered flashcard
- hint text inside card on front side: `Нажми, чтобы перевернуть карточку`
- only two gesture hints below card:
  - `↑ Знаю`
  - `↓ Не знаю`

Behavior:

- front side shows Bulgarian word
- tap flips card
- back side shows Russian translation
- swipe up marks card as known and moves to next
- swipe down marks card as unknown and moves to next

Important:

- no save button on this screen
- no extra controls around the card in MVP

## Data Model

Recommended local storage model:

### WordCard

- `id`
- `bgWord`
- `ruTranslation`
- `createdAt`
- `updatedAt`

### WordGroup

- `id`
- `name`

### WordCardGroupCrossRef

- `wordId`
- `groupId`

Reason:

- one word can belong to multiple groups
- groups can exist independently
- model is flexible enough for future filtering and statistics

## Storage Recommendation

Use Room for local storage.

Reason:

- relational structure is needed
- many-to-many mapping is needed
- search is needed
- editing and filtering are needed
- this is a better fit than JSON or DataStore for dictionary content

DataStore can still remain for app-level preferences and progress, but not as the main storage for this dictionary feature.

## Voice Input

Voice input is included in MVP.

Expected behavior:

- microphone icon on Bulgarian field launches Android speech recognition
- microphone icon on Russian field launches Android speech recognition
- recognized text is inserted into the related field
- user keeps control and can manually edit result

Constraints:

- recognition quality depends on Android system speech recognition
- Bulgarian recognition quality may vary by device and installed language packs
- this must be treated as assistive input, not guaranteed perfect automation

## Training Modes

Included in MVP:

- train all words
- train one selected group

Not included yet:

- train by multiple groups at once
- train only unknown words
- train only recent words

## Empty States

### No Words Yet

Show:

- friendly empty state
- message like `Пока здесь нет слов`
- action `Добавить первое слово`

### No Groups Yet

Show:

- `Группы пока не созданы`
- action `+ Новая группа`

## Editing and Deletion

Editing:

- tap a word row opens edit screen with existing values

Deletion:

- user can delete a word from list actions
- use lightweight confirmation dialog before deletion

Deletion of a group:

- not required in MVP
- can be added later once group management grows

## Visual Direction

The dictionary should visually match the app but feel calmer and more tool-like than lesson screens.

Direction:

- light background
- soft blue accents
- rounded cards and controls
- compact rows in the word list
- clear primary CTA buttons
- no translator-style clutter

## MVP Success Criteria

The first release is successful if a user can:

1. Open the dictionary
2. Add a Bulgarian word and Russian translation quickly
3. Optionally use voice input for either field
4. Add the word to one or more groups
5. See the word in a compact list
6. Start training for all words
7. Start training for a chosen group
8. Flip flashcards and mark known/unknown with gestures

## Future Extensions

Planned later, not part of this implementation:

- auto-translation
- auto-example generation
- pronunciation playback
- import/export
- repetition scheduling
- word statistics
- assistant mode for explanations
