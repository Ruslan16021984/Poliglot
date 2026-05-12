import json
from pathlib import Path


ASSET_PATH = Path("app/src/main/assets/textbook_dictionary_words.json")


ADDITIONAL_WORDS = {
    7: [
        ("топло", "тепло", "тепло", "adverb", ["weather"]),
        ("студено", "холодно", "холодно", "adverb", ["weather"]),
        ("вали", "идет дождь", "йде дощ", "verb", ["weather"]),
        ("духа", "дует", "дує", "verb", ["weather"]),
        ("облачно", "облачно", "хмарно", "adverb", ["weather"]),
        ("ясно", "ясно", "ясно", "adverb", ["weather"]),
        ("дъждовно", "дождливо", "дощово", "adverb", ["weather"]),
        ("слънчево", "солнечно", "сонячно", "adverb", ["weather"]),
        ("месец", "месяц", "місяць", "noun", ["time"]),
        ("часът", "час", "година", "noun", ["time"]),
    ],
    8: [
        ("риза", "рубашка", "сорочка", "noun", ["clothes"]),
        ("пола", "юбка", "спідниця", "noun", ["clothes"]),
        ("рокля", "платье", "сукня", "noun", ["clothes"]),
        ("панталон", "брюки", "штани", "noun", ["clothes"]),
        ("обувки", "обувь", "взуття", "noun", ["clothes"]),
        ("чорапи", "носки", "шкарпетки", "noun", ["clothes"]),
        ("шапка", "шапка", "шапка", "noun", ["clothes"]),
        ("яке", "куртка", "куртка", "noun", ["clothes"]),
        ("палто", "пальто", "пальто", "noun", ["clothes"]),
        ("стар", "старый", "старий", "adjective", ["quality"]),
    ],
    9: [
        ("къща", "дом", "будинок", "noun", ["home"]),
        ("стая", "комната", "кімната", "noun", ["home"]),
        ("маса", "стол", "стіл", "noun", ["furniture"]),
        ("кухня", "кухня", "кухня", "noun", ["home"]),
        ("легло", "кровать", "ліжко", "noun", ["furniture"]),
        ("диван", "диван", "диван", "noun", ["furniture"]),
        ("хол", "гостиная", "вітальня", "noun", ["home"]),
        ("лампа", "лампа", "лампа", "noun", ["furniture"]),
        ("килим", "ковер", "килим", "noun", ["furniture"]),
        ("апартамент", "квартира", "квартира", "noun", ["home"]),
    ],
    10: [
        ("гара", "вокзал", "вокзал", "noun", ["transport"]),
        ("спирка", "остановка", "зупинка", "noun", ["transport"]),
        ("метро", "метро", "метро", "noun", ["transport"]),
        ("автобус", "автобус", "автобус", "noun", ["transport"]),
        ("билет", "билет", "квиток", "noun", ["transport"]),
        ("каса", "касса", "каса", "noun", ["transport"]),
        ("летище", "аэропорт", "аеропорт", "noun", ["transport"]),
        ("перон", "платформа", "платформа", "noun", ["transport"]),
        ("близо", "близко", "близько", "adverb", ["location"]),
        ("далеч", "далеко", "далеко", "adverb", ["location"]),
    ],
    11: [
        ("сутрин", "утром", "вранці", "adverb", ["routine"]),
        ("закусвам", "завтракаю", "снідаю", "verb", ["routine"]),
        ("обядвам", "обедаю", "обідаю", "verb", ["routine"]),
        ("вечерям", "ужинаю", "вечеряю", "verb", ["routine"]),
        ("лягам", "ложусь", "лягаю", "verb", ["routine"]),
        ("рано", "рано", "рано", "adverb", ["routine"]),
        ("късно", "поздно", "пізно", "adverb", ["routine"]),
        ("зъби", "зубы", "зуби", "noun", ["routine"]),
        ("книга", "книга", "книга", "noun", ["routine"]),
        ("музика", "музыка", "музика", "noun", ["routine"]),
    ],
}


def main() -> None:
    data = json.loads(ASSET_PATH.read_text(encoding="utf-8"))
    lesson_themes = {
        item["lessonBook"]: item["theme"]
        for item in data["lessonCatalog"]
    }
    existing = {item["bg"] for item in data["words"]}

    for lesson_book, items in ADDITIONAL_WORDS.items():
        theme = lesson_themes[lesson_book]
        for bg, ru, uk, part_of_speech, tags in items:
            if bg in existing:
                continue
            data["words"].append(
                {
                    "bg": bg,
                    "ru": ru,
                    "uk": uk,
                    "lessonBook": lesson_book,
                    "theme": theme,
                    "partOfSpeech": part_of_speech,
                    "type": "word",
                    "source": "textbook",
                    "tags": tags,
                    "appLessons": [lesson_book],
                    "isHighFrequency": True,
                }
            )
            existing.add(bg)

    ASSET_PATH.write_text(
        json.dumps(data, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )


if __name__ == "__main__":
    main()
