## Textbook Content Workflow

To reduce encoding bugs in textbook content, follow these rules:

1. Small changes:
Use `apply_patch` directly on the target JSON file.

2. Large changes:
Use only the safe UTF-8 tool:

```powershell
python .\tools\textbook_content_tool.py check
python .\tools\textbook_content_tool.py stats
python .\tools\textbook_content_tool.py expand-lessons
python .\tools\textbook_content_tool.py expand-dictionary
```

3. New interface/content language:
Use separate localized assets instead of runtime fallback text.

Lesson theory and metadata:

```powershell
python .\tools\generate_lessons_en_clean.py
```

Exercise translations:

```powershell
python .\tools\create_exercise_translation_overlay.py 4 en
```

Then fill `textbook_exercises_lesson4.en.json` with real translations by `item.id`.
For future languages, repeat the same overlay pattern with `ar`, `de`, and so on.

4. Do not mass-rewrite textbook JSON through ad-hoc PowerShell pipelines.
Avoid `Get-Content | ... | Set-Content` for large Cyrillic assets.

5. Before considering content work done:

```powershell
python .\tools\textbook_content_tool.py check
.\gradlew.bat testDebugUnitTest --tests "*LessonJsonAssetsTest" --tests "*LessonSessionRepositoryTest" --tests "*AssetEncodingGuardTest"
```

6. If mojibake appears:
- stop editing the affected file
- restore the last clean git revision
- rerun the safe tool instead of manually re-encoding text
