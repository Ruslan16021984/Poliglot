# Project Agent Prompt

Use this file as a starting brief for any AI agent that needs to work on this repository.

## What This Project Is

This repository contains an Android language-learning application for studying Bulgarian.

- App name: `Полиглот Болгария`
- Platform: Android
- Target learners:
  - Russian-speaking users learning Bulgarian
  - Ukrainian-speaking users learning Bulgarian
- Main learning level right now: `A1`

The app is designed as a practical everyday Bulgarian trainer, not as an abstract grammar sandbox.

## Core Product Idea

The app teaches Bulgarian through:

- structured lessons
- short theory blocks
- fixed phrase exercises
- a built-in dictionary
- personal saved words
- flashcard-style review

The key principle is:

- **no random sentence generation**
- all lesson content is curated and stored in JSON

## Current Course Structure

The app currently follows a textbook-aligned 11-lesson A1 route:

1. Greeting and introduction
2. Food and breakfast
3. Restaurant
4. Shopping and food
5. City, address, and directions
6. Family
7. Weather and time
8. Clothes and colors
9. Home and furniture
10. Transport
11. Daily routine

## Critical Content Rules

When working on lessons, do **not** introduce random phrase generation.

The content model is:

- every lesson uses fixed JSON phrases
- every phrase has localized fields
- runtime only loads, shuffles options, checks answers, and tracks progress

Important rules:

- keep `100` exercises per lesson at runtime
- keep about `50` unique fixed phrases per lesson pool
- use textbook vocabulary as much as possible
- keep Bulgarian as source language for exercises
- keep Russian and Ukrainian translations in JSON

## Where Content Lives

Main lesson exercise files:

- `app/src/main/assets/textbook_exercises_lesson1.json`
- ...
- `app/src/main/assets/textbook_exercises_lesson11.json`

Main theory files:

- `app/src/main/assets/lessons_ru.json`
- `app/src/main/assets/lessons_uk.json`

Dictionary files:

- `app/src/main/assets/textbook_dictionary_words.json`
- `app/src/main/assets/course_dictionary_words.json`

## Important Architecture Notes

The app already moved away from old grammar-driven JSON and now uses textbook-driven lesson assets.

Main product assumptions:

- the lesson route should match the textbook route
- grammar supports the topic, not the other way around
- content should feel coherent across:
  - lesson theory
  - lesson exercises
  - dictionary
  - flashcards

## Release and Monetization Notes

The app includes Google Play Billing for full-course unlock.

Known billing product id:

- `full_course_access`

Package / app id for publication:

- `com.carbit3333333.poliglotbulgaria.a1`

Current development convention:

- during internal testing, release-hidden testing tools may be temporarily enabled through `BuildConfig`
- before final production release, those testing tools must be turned off

If you change release behavior, verify current values in:

- `app/build.gradle.kts`

## Progress and Scoring Rules

Lesson score is based on the full lesson length:

- formula: `correct / 100 * 5`
- passing score: `4.5`

This means:

- `90 / 100` correct = `4.5`
- a lesson may finish early once the passing threshold is reached

## Known Safety Rule: UTF-8 Matters

This project has previously had corrupted localized content because of unsafe rewrites.

Do **not** mass-edit localized JSON with risky shell rewrites.

Preferred approach:

- small edits: patch directly
- large edits: use a UTF-8-safe script
- always re-check assets after edits

Use:

- `python .\tools\textbook_content_tool.py check`

That tool is intended to catch:

- mojibake-like corruption
- suspicious placeholder-style `????` text in localized JSON

## What To Be Careful About

When editing this project:

- do not reintroduce random sentence generation
- do not break UTF-8 in JSON
- do not move lesson topics away from textbook alignment
- do not silently mix lesson topics from different textbook lessons
- do not expose testing-only UI in production release unless explicitly intended

## Good First Steps For An Agent

Before making changes:

1. inspect the relevant lesson JSON or theory JSON
2. check whether the request affects textbook alignment
3. preserve Russian and Ukrainian localization
4. run the content check after edits

If the task touches release or billing:

1. inspect `app/build.gradle.kts`
2. inspect `AndroidManifest.xml`
3. inspect billing-related viewmodels and data layer

## Short Agent Summary

This is an Android A1 Bulgarian learning app for Russian-speaking and Ukrainian-speaking learners. It uses textbook-aligned lessons, fixed JSON-based phrase exercises, localized theory, a dictionary, flashcards, lesson progress tracking, score-based unlocking, and a one-time purchase for full access. Do not use random sentence generation. Prefer UTF-8-safe editing of localized content. Preserve lesson-to-textbook alignment and keep release/testing flags intentional.
