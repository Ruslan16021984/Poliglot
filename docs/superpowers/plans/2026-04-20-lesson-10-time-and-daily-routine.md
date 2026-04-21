# Lesson 10 Time And Daily Routine Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add Lesson 10 as a practical A1 lesson about time expressions and daily routine, with JSON-backed content, generated exercises, and test coverage.

**Architecture:** Extend the lesson assets with Lesson 10 data, add a dedicated generator that assembles fixed and parameterized routine phrases, then wire the new lesson into the lesson repository flow. Keep the lesson hybrid: theory and phrase data in JSON, exercise assembly in Kotlin.

**Tech Stack:** Kotlin, kotlinx.serialization JSON assets, existing lesson session factory/generator architecture, JUnit tests, Gradle.

---

### Task 1: Add Lesson 10 asset shapes and content

**Files:**
- Modify: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/data/lesson_session/LessonSessionAssets.kt`
- Modify: `app/src/main/assets/lessons.json`
- Modify: `app/src/main/assets/lesson_session_content.json`

- [ ] Add Lesson 10 asset models and fields to `LessonSessionAssets`.
- [ ] Add Lesson 10 theory to `lessons.json`.
- [ ] Add Lesson 10 phrase data and fallback defaults to `lesson_session_content.json` and `LessonSessionAssets.kt`.

### Task 2: Implement Lesson 10 generator and wiring

**Files:**
- Create: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/data/lesson_session/generators/Lesson10RealGenerator.kt`
- Modify: `app/src/main/java/com/carbit3333333/oiiglot_bulgary/data/lesson_session/LessonSessionFactory.kt`

- [ ] Implement a generator that builds 60 practical time-routine exercises.
- [ ] Support day phrases, part-of-day phrases, before/after phrases, exact-time phrases, and interval phrases.
- [ ] Wire lesson 10 into the factory with a proper lesson title.

### Task 3: Add regression tests

**Files:**
- Modify: `app/src/test/java/com/carbit3333333/oiiglot_bulgary/LessonJsonAssetsTest.kt`
- Modify: `app/src/test/java/com/carbit3333333/oiiglot_bulgary/LessonSessionRepositoryTest.kt`

- [ ] Extend asset-decoding tests for lesson 10 counts and required content.
- [ ] Extend session tests to verify valid exercises and key time patterns.

### Task 4: Verify

**Files:**
- None

- [ ] Run `.\gradlew.bat test`
- [ ] Confirm lesson 10 loads, generates valid exercises, and does not regress existing lessons.
