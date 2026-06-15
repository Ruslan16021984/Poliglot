# Full English Localization Design

## Goal

Add English as a complete third application language alongside Russian and Ukrainian.
When English is selected manually or detected as the supported system language, every
user-facing part of the application must use English:

- interface strings;
- lesson titles, subtitles, and theory;
- exercise source text, instructions, and hints;
- course dictionary translations and flashcards;
- personal dictionary labels and English translations;
- billing and error messages.

Bulgarian remains the language being learned.

## Language Selection

Add `AppLanguage.English` with the `en` locale tag. The language selector contains:

- System;
- Russian;
- Ukrainian;
- English.

System mode resolves Ukrainian for `uk`, Russian for `ru`, English for `en`, and uses
English as the fallback for every other system language. Explicitly selected languages
always override the system language.

## Android Interface Resources

Create:

- `res/values-en/strings.xml`;
- `res/values-en/billing_strings.xml`.

The existing default `values/` resources remain Russian to avoid regressions. English
resources must contain every translatable key from the default resources. Automated
tests compare resource keys and reject missing English strings.

## Lesson Theory

Create `assets/lessons_en.json` with all 11 lessons. It mirrors the IDs and structure of
`lessons_ru.json` and `lessons_uk.json`, but contains English titles, subtitles, and
theory explanations.

`LessonRepository` resolves the lesson asset from the active language:

- Russian: `lessons_ru.json`;
- Ukrainian: `lessons_uk.json`;
- English: `lessons_en.json`.

Changing the language invalidates cached lessons and reloads the selected asset.

## Exercises

Extend every textbook exercise item with:

- `en`: required English source translation;
- `hintEn`: optional English hint.

Extend `LessonExerciseLocale` with English. In English mode:

- source text uses `en`;
- instruction uses an English instruction string;
- hint uses `hintEn`, with a safe English fallback if it is absent;
- Bulgarian answer words and distractors remain unchanged.

All 11 exercise files must contain exactly 100 exercises with non-empty English
translations. Existing RU/UK fields remain unchanged.

## Course Dictionary

Extend source dictionary words with `en` and generated course words with
`enTranslation`.

The runtime dictionary model exposes a locale-aware display translation instead of
directly rendering `ruTranslation`. Search checks Bulgarian and every available
translation so changing language does not make saved words undiscoverable.

Flashcards use the selected application language:

- Bulgarian to selected language;
- selected language to Bulgarian.

Labels and direction names also use the selected language.

## Personal Dictionary Migration

Existing user-created dictionary entries currently store only `ruTranslation`. Preserve
that column and its data.

Add optional `ukTranslation` and `enTranslation` columns through a Room migration.
Existing entries remain usable:

- Russian mode displays `ruTranslation`;
- Ukrainian mode prefers `ukTranslation`, then falls back to `ruTranslation`;
- English mode prefers `enTranslation`, then falls back to `ruTranslation`.

The word editor shows the translation field for the currently selected application
language. Saving English or Ukrainian translations must not overwrite the stored Russian
translation.

## Voice Input

Bulgarian voice input continues to use `bg-BG`. Translation-field voice input uses the
active application language:

- `ru-RU`;
- `uk-UA`;
- `en-US`.

## Content Generation And UTF-8 Safety

Use project-local Python migration scripts with explicit UTF-8 reads and writes.
Localized JSON must be serialized with `ensure_ascii=False`.

Generation scripts become the reusable source of truth for adding and validating the
English fields. Existing UTF-8 and mojibake checks remain mandatory after every content
rewrite.

## Validation

Add automated checks for:

- English resource keys matching default resource keys;
- `lessons_en.json` containing all 11 valid lessons;
- every exercise containing non-empty `en`;
- every course dictionary word containing non-empty `enTranslation`;
- English locale selection and system-language fallback;
- lesson and restored-session relocalization into English;
- dictionary display, search, flashcards, and personal-word fallback behavior;
- Room migration preserving existing Russian translations;
- no suspicious mojibake in English, Russian, Ukrainian, or Bulgarian assets.

Run the full unit-test suite and release Kotlin compilation before preparing the next
market build.

## Compatibility

Russian and Ukrainian behavior must remain unchanged. Existing progress, lesson session
state, billing access, personal dictionary words, and difficult-word markers must survive
the update.

English localization does not change lesson ordering, scoring, unlocking rules, or the
fixed 100-exercise pools.
