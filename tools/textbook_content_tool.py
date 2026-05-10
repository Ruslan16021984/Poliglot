from __future__ import annotations

import argparse
import importlib.util
import json
import re
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parent.parent
ASSETS = ROOT / "app" / "src" / "main" / "assets"
JSON_ASSETS = sorted(ASSETS.glob("*.json"))
MOJIBAKE_PATTERNS = ("Р Р‹", "Р РЋ", "Р Р‚", "РІР‚", "Гђ", "Г‘", "\ufffd")
QUESTION_MARK_STRING_PATTERN = re.compile(r'"[^"\n]*\?{3,}[^"\n]*"')


def _load_module(script_name: str):
    script_path = Path(__file__).resolve().parent / script_name
    spec = importlib.util.spec_from_file_location(script_name.replace(".py", ""), script_path)
    if spec is None or spec.loader is None:
        raise RuntimeError(f"Cannot load {script_name}")
    module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(module)
    return module


def run_expand_lessons() -> None:
    module = _load_module("expand_textbook_lessons_3_11.py")
    module.main()


def run_expand_dictionary() -> None:
    module = _load_module("expand_textbook_dictionary_7_11.py")
    module.main()


def check_assets() -> int:
    bad_files: list[str] = []
    question_mark_files: list[str] = []
    for path in JSON_ASSETS:
        text = path.read_text(encoding="utf-8")
        if any(pattern in text for pattern in MOJIBAKE_PATTERNS):
            bad_files.append(path.name)
        if QUESTION_MARK_STRING_PATTERN.search(text):
            question_mark_files.append(path.name)

    if bad_files:
        print("Found suspicious mojibake patterns:")
        for file_name in bad_files:
            print(f"- {file_name}")
        return 1

    if question_mark_files:
        print("Found suspicious placeholder-like question mark strings:")
        for file_name in question_mark_files:
            print(f"- {file_name}")
        return 1

    print("All JSON assets look clean.")
    return 0


def print_stats() -> None:
    exercise_files = sorted(ASSETS.glob("textbook_exercises_lesson*.json"))
    for path in exercise_files:
        payload = json.loads(path.read_text(encoding="utf-8"))
        lesson_id = payload["lessonApp"]
        unique_ru = len({item["ru"] for item in payload["items"]})
        print(f"lesson {lesson_id}: items={len(payload['items'])}, unique_ru={unique_ru}")

    dictionary = json.loads((ASSETS / "textbook_dictionary_words.json").read_text(encoding="utf-8"))
    print(
        "dictionary:",
        f"words={len(dictionary['words'])}",
        f"phrases={len(dictionary['phrases'])}",
        f"wordLessons={sorted({entry['lessonBook'] for entry in dictionary['words']})}",
        f"phraseLessons={sorted({entry['lessonBook'] for entry in dictionary['phrases']})}",
    )


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description="Safe UTF-8 tooling for textbook JSON content.")
    subparsers = parser.add_subparsers(dest="command", required=True)

    subparsers.add_parser("check", help="Check all JSON assets for suspicious mojibake patterns.")
    subparsers.add_parser("stats", help="Print textbook lesson and dictionary content stats.")
    subparsers.add_parser("expand-lessons", help="Regenerate textbook lessons 3..11 to 50 fixed phrases.")
    subparsers.add_parser("expand-dictionary", help="Expand textbook dictionary with lessons 7..11.")

    return parser


def main(argv: list[str] | None = None) -> int:
    parser = build_parser()
    args = parser.parse_args(argv)

    if args.command == "check":
        return check_assets()
    if args.command == "stats":
        print_stats()
        return 0
    if args.command == "expand-lessons":
        run_expand_lessons()
        return check_assets()
    if args.command == "expand-dictionary":
        run_expand_dictionary()
        return check_assets()

    parser.error(f"Unsupported command: {args.command}")
    return 2


if __name__ == "__main__":
    sys.exit(main())
