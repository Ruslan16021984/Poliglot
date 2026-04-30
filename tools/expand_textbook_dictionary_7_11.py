from __future__ import annotations

import json
from pathlib import Path


ASSET = Path(r"D:\OIiglot_Bulgary\app\src\main\assets\textbook_dictionary_words.json")


def word(
    bg: str,
    ru: str,
    uk: str,
    lesson: int,
    theme: str,
    part: str,
    tags: list[str],
    app_lessons: list[int],
    high: bool = True,
    notes: str | None = None,
) -> dict:
    data = {
        "bg": bg,
        "ru": ru,
        "uk": uk,
        "lessonBook": lesson,
        "theme": theme,
        "partOfSpeech": part,
        "type": "word",
        "source": "textbook",
        "tags": tags,
        "appLessons": app_lessons,
        "isHighFrequency": high,
    }
    if notes:
        data["notes"] = notes
    return data


def phrase(
    bg: str,
    ru: str,
    uk: str,
    lesson: int,
    theme: str,
    tags: list[str],
    app_lessons: list[int],
    high: bool = True,
    notes: str | None = None,
) -> dict:
    data = {
        "bg": bg,
        "ru": ru,
        "uk": uk,
        "lessonBook": lesson,
        "theme": theme,
        "type": "phrase",
        "source": "textbook",
        "tags": tags,
        "appLessons": app_lessons,
        "isHighFrequency": high,
    }
    if notes:
        data["notes"] = notes
    return data


LESSON_7_THEME = "Погода, дни недели, месяцы, время"
LESSON_8_THEME = "Одежда и цвета"
LESSON_9_THEME = "Дом, комнаты, мебель"
LESSON_10_THEME = "Транспорт и дорога"
LESSON_11_THEME = "Ежедневные дела и распорядок"


WORDS = [
    word("топло", "тепло", "тепло", 7, LESSON_7_THEME, "adjective", ["weather"], [7], True),
    word("студено", "холодно", "холодно", 7, LESSON_7_THEME, "adjective", ["weather"], [7], True),
    word("слънчево", "солнечно", "сонячно", 7, LESSON_7_THEME, "adjective", ["weather"], [7], True),
    word("дъжд", "дождь", "дощ", 7, LESSON_7_THEME, "noun", ["weather"], [7], True),
    word("сняг", "снег", "сніг", 7, LESSON_7_THEME, "noun", ["weather"], [7], True),
    word("вятър", "ветер", "вітер", 7, LESSON_7_THEME, "noun", ["weather"], [7], True),
    word("понеделник", "понедельник", "понеділок", 7, LESSON_7_THEME, "noun", ["weekday"], [7], True),
    word("вторник", "вторник", "вівторок", 7, LESSON_7_THEME, "noun", ["weekday"], [7], True),
    word("сряда", "среда", "середа", 7, LESSON_7_THEME, "noun", ["weekday"], [7], True),
    word("четвъртък", "четверг", "четвер", 7, LESSON_7_THEME, "noun", ["weekday"], [7], True),
    word("петък", "пятница", "п'ятниця", 7, LESSON_7_THEME, "noun", ["weekday"], [7], True),
    word("събота", "суббота", "субота", 7, LESSON_7_THEME, "noun", ["weekday"], [7], True),
    word("неделя", "воскресенье", "неділя", 7, LESSON_7_THEME, "noun", ["weekday"], [7], True),
    word("януари", "январь", "січень", 7, LESSON_7_THEME, "noun", ["month"], [7], True),
    word("април", "апрель", "квітень", 7, LESSON_7_THEME, "noun", ["month"], [7], True),
    word("август", "август", "серпень", 7, LESSON_7_THEME, "noun", ["month"], [7], True),
    word("ноември", "ноябрь", "листопад", 7, LESSON_7_THEME, "noun", ["month"], [7], True),
    word("час", "час", "година", 7, LESSON_7_THEME, "noun", ["time"], [7, 11], True),
    word("сутрин", "утром", "вранці", 7, LESSON_7_THEME, "adverb", ["time-of-day"], [7, 11], True),
    word("вечер", "вечером", "увечері", 7, LESSON_7_THEME, "adverb", ["time-of-day"], [7, 11], True),
    word("следобед", "после обеда", "після обіду", 7, LESSON_7_THEME, "adverb", ["time-of-day"], [7, 11], True),
    word("нощем", "ночью", "уночі", 7, LESSON_7_THEME, "adverb", ["time-of-day"], [7, 11], True),

    word("риза", "рубашка", "сорочка", 8, LESSON_8_THEME, "noun", ["clothes"], [8], True),
    word("пола", "юбка", "спідниця", 8, LESSON_8_THEME, "noun", ["clothes"], [8], True),
    word("рокля", "платье", "сукня", 8, LESSON_8_THEME, "noun", ["clothes"], [8], True),
    word("шапка", "шапка", "шапка", 8, LESSON_8_THEME, "noun", ["clothes"], [8], True),
    word("яке", "куртка", "куртка", 8, LESSON_8_THEME, "noun", ["clothes"], [8], True),
    word("палто", "пальто", "пальто", 8, LESSON_8_THEME, "noun", ["clothes"], [8], True),
    word("панталон", "брюки", "штани", 8, LESSON_8_THEME, "noun", ["clothes"], [8], True),
    word("обувки", "туфли", "туфлі", 8, LESSON_8_THEME, "noun", ["clothes"], [8], True),
    word("шал", "шарф", "шарф", 8, LESSON_8_THEME, "noun", ["clothes"], [8], True),
    word("сако", "пиджак", "піджак", 8, LESSON_8_THEME, "noun", ["clothes"], [8], False),
    word("пуловер", "свитер", "светр", 8, LESSON_8_THEME, "noun", ["clothes"], [8], False),
    word("ботуши", "сапоги", "чоботи", 8, LESSON_8_THEME, "noun", ["clothes"], [8], False),
    word("бял", "белый", "білий", 8, LESSON_8_THEME, "adjective", ["color"], [8], True),
    word("черен", "чёрный", "чорний", 8, LESSON_8_THEME, "adjective", ["color"], [8], True),
    word("червен", "красный", "червоний", 8, LESSON_8_THEME, "adjective", ["color"], [8], True),
    word("син", "синий", "синій", 8, LESSON_8_THEME, "adjective", ["color"], [8], True),
    word("зелен", "зелёный", "зелений", 8, LESSON_8_THEME, "adjective", ["color"], [8], True),
    word("жълт", "жёлтый", "жовтий", 8, LESSON_8_THEME, "adjective", ["color"], [8], False),
    word("кафяв", "коричневый", "коричневий", 8, LESSON_8_THEME, "adjective", ["color"], [8], False),

    word("къща", "дом", "будинок", 9, LESSON_9_THEME, "noun", ["home"], [9], True),
    word("апартамент", "квартира", "квартира", 9, LESSON_9_THEME, "noun", ["home"], [9], True),
    word("кухня", "кухня", "кухня", 9, LESSON_9_THEME, "noun", ["room"], [9], True),
    word("баня", "ванная", "ванна", 9, LESSON_9_THEME, "noun", ["room"], [9], True),
    word("стая", "комната", "кімната", 9, LESSON_9_THEME, "noun", ["room"], [9], True),
    word("хол", "гостиная", "вітальня", 9, LESSON_9_THEME, "noun", ["room"], [9], True),
    word("спалня", "спальня", "спальня", 9, LESSON_9_THEME, "noun", ["room"], [9], True),
    word("коридор", "коридор", "коридор", 9, LESSON_9_THEME, "noun", ["room"], [9], True),
    word("маса", "стол", "стіл", 9, LESSON_9_THEME, "noun", ["furniture"], [9], True),
    word("стол", "стул", "стілець", 9, LESSON_9_THEME, "noun", ["furniture"], [9], True),
    word("легло", "кровать", "ліжко", 9, LESSON_9_THEME, "noun", ["furniture"], [9], True),
    word("диван", "диван", "диван", 9, LESSON_9_THEME, "noun", ["furniture"], [9], True),
    word("шкаф", "шкаф", "шафа", 9, LESSON_9_THEME, "noun", ["furniture"], [9], True),
    word("лампа", "лампа", "лампа", 9, LESSON_9_THEME, "noun", ["furniture"], [9], True),
    word("килим", "ковёр", "килим", 9, LESSON_9_THEME, "noun", ["furniture"], [9], False),
    word("полица", "полка", "полиця", 9, LESSON_9_THEME, "noun", ["furniture"], [9], False),
    word("огледало", "зеркало", "дзеркало", 9, LESSON_9_THEME, "noun", ["furniture"], [9], False),

    word("автобус", "автобус", "автобус", 10, LESSON_10_THEME, "noun", ["transport"], [10], True),
    word("трамвай", "трамвай", "трамвай", 10, LESSON_10_THEME, "noun", ["transport"], [10], True),
    word("влак", "поезд", "потяг", 10, LESSON_10_THEME, "noun", ["transport"], [10], True),
    word("метро", "метро", "метро", 10, LESSON_10_THEME, "noun", ["transport"], [10], True),
    word("такси", "такси", "таксі", 10, LESSON_10_THEME, "noun", ["transport"], [10], True),
    word("спирка", "остановка", "зупинка", 10, LESSON_10_THEME, "noun", ["transport-place"], [10], True),
    word("гара", "вокзал", "вокзал", 10, LESSON_10_THEME, "noun", ["transport-place"], [10], True),
    word("летище", "аэропорт", "аеропорт", 10, LESSON_10_THEME, "noun", ["transport-place"], [10], True),
    word("билет", "билет", "квиток", 10, LESSON_10_THEME, "noun", ["ticket"], [10], True),
    word("карта", "карта", "карта", 10, LESSON_10_THEME, "noun", ["ticket"], [10], True),
    word("самолет", "самолёт", "літак", 10, LESSON_10_THEME, "noun", ["transport"], [10], False),
    word("каса", "касса", "каса", 10, LESSON_10_THEME, "noun", ["transport-place"], [10], False),
    word("перон", "перрон", "перон", 10, LESSON_10_THEME, "noun", ["transport-place"], [10], False),
    word("куфар", "чемодан", "валіза", 10, LESSON_10_THEME, "noun", ["travel"], [10], False),
    word("пътувам", "ехать", "їхати", 10, LESSON_10_THEME, "verb", ["transport"], [10], True),

    word("ставам", "вставать", "вставати", 11, LESSON_11_THEME, "verb", ["routine"], [11], True),
    word("лягам", "ложиться", "лягати", 11, LESSON_11_THEME, "verb", ["routine"], [11], True),
    word("излизам", "выходить", "виходити", 11, LESSON_11_THEME, "verb", ["routine"], [11], True),
    word("вечерям", "ужинать", "вечеряти", 11, LESSON_11_THEME, "verb", ["routine"], [11], True),
    word("връщам се", "возвращаться", "повертатися", 11, LESSON_11_THEME, "verb", ["routine"], [11], True),
    word("отивам", "идти", "йти", 11, LESSON_11_THEME, "verb", ["routine"], [11], True),
    word("денем", "днём", "удень", 11, LESSON_11_THEME, "adverb", ["time-of-day"], [11], False),
    word("сутрин", "утром", "вранці", 11, LESSON_11_THEME, "adverb", ["time-of-day"], [11], True),
    word("следобед", "после обеда", "після обіду", 11, LESSON_11_THEME, "adverb", ["time-of-day"], [11], True),
    word("вечер", "вечером", "увечері", 11, LESSON_11_THEME, "adverb", ["time-of-day"], [11], True),
    word("нощем", "ночью", "уночі", 11, LESSON_11_THEME, "adverb", ["time-of-day"], [11], False),
    word("зъби", "зубы", "зуби", 11, LESSON_11_THEME, "noun", ["routine"], [11], False),
    word("магазин", "магазин", "магазин", 11, LESSON_11_THEME, "noun", ["routine-place"], [5, 11], True),
]


PHRASES = [
    phrase("Днес е топло.", "Сегодня тепло.", "Сьогодні тепло.", 7, LESSON_7_THEME, ["weather", "statement"], [7]),
    phrase("Днес е студено.", "Сегодня холодно.", "Сьогодні холодно.", 7, LESSON_7_THEME, ["weather", "statement"], [7]),
    phrase("Топло ли е?", "Тепло?", "Тепло?", 7, LESSON_7_THEME, ["weather", "question"], [7]),
    phrase("Утре ще има дъжд.", "Завтра будет дождь.", "Завтра буде дощ.", 7, LESSON_7_THEME, ["weather", "future"], [7]),
    phrase("Днес е понеделник.", "Сегодня понедельник.", "Сьогодні понеділок.", 7, LESSON_7_THEME, ["weekday", "statement"], [7]),
    phrase("Сега е януари.", "Сейчас январь.", "Зараз січень.", 7, LESSON_7_THEME, ["month", "statement"], [7]),
    phrase("Колко е часът?", "Сколько сейчас времени?", "Котра година?", 7, LESSON_7_THEME, ["time", "question"], [7]),
    phrase("Сега е три часа.", "Сейчас три часа.", "Зараз третя година.", 7, LESSON_7_THEME, ["time", "statement"], [7]),
    phrase("Работя сутрин.", "Я работаю утром.", "Я працюю вранці.", 7, LESSON_7_THEME, ["time-of-day", "statement"], [7, 11]),
    phrase("Почивам в неделя.", "Я отдыхаю в воскресенье.", "Я відпочиваю в неділю.", 7, LESSON_7_THEME, ["weekday", "statement"], [7]),

    phrase("Това е риза.", "Это рубашка.", "Це сорочка.", 8, LESSON_8_THEME, ["clothes", "statement"], [8]),
    phrase("Това е рокля.", "Это платье.", "Це сукня.", 8, LESSON_8_THEME, ["clothes", "statement"], [8]),
    phrase("Това са обувки.", "Это туфли.", "Це туфлі.", 8, LESSON_8_THEME, ["clothes", "statement"], [8]),
    phrase("Ризата е бяла.", "Рубашка белая.", "Сорочка біла.", 8, LESSON_8_THEME, ["color", "statement"], [8]),
    phrase("Полата е черна.", "Юбка черная.", "Спідниця чорна.", 8, LESSON_8_THEME, ["color", "statement"], [8]),
    phrase("Якето е ново.", "Куртка новая.", "Куртка нова.", 8, LESSON_8_THEME, ["clothes", "statement"], [8]),
    phrase("Обувките са кафяви.", "Туфли коричневые.", "Туфлі коричневі.", 8, LESSON_8_THEME, ["color", "statement"], [8]),
    phrase("Това пола ли е?", "Это юбка?", "Це спідниця?", 8, LESSON_8_THEME, ["clothes", "question"], [8]),
    phrase("Харесваш ли тази рокля?", "Тебе нравится это платье?", "Тобі подобається ця сукня?", 8, LESSON_8_THEME, ["choice", "question"], [8]),
    phrase("Харесваш ли тази риза?", "Тебе нравится эта рубашка?", "Тобі подобається ця сорочка?", 8, LESSON_8_THEME, ["choice", "question"], [8]),

    phrase("Това е къща.", "Это дом.", "Це будинок.", 9, LESSON_9_THEME, ["home", "statement"], [9]),
    phrase("Това е апартамент.", "Это квартира.", "Це квартира.", 9, LESSON_9_THEME, ["home", "statement"], [9]),
    phrase("Това е кухня.", "Это кухня.", "Це кухня.", 9, LESSON_9_THEME, ["room", "statement"], [9]),
    phrase("Това е баня.", "Это ванная.", "Це ванна.", 9, LESSON_9_THEME, ["room", "statement"], [9]),
    phrase("Столът е до масата.", "Стул рядом со столом.", "Стілець поруч зі столом.", 9, LESSON_9_THEME, ["location", "statement"], [9]),
    phrase("Лампата е на масата.", "Лампа на столе.", "Лампа на столі.", 9, LESSON_9_THEME, ["location", "statement"], [9]),
    phrase("Къде е диванът?", "Где диван?", "Де диван?", 9, LESSON_9_THEME, ["location", "question"], [9]),
    phrase("Къщата е светла.", "Дом светлый.", "Будинок світлий.", 9, LESSON_9_THEME, ["home", "statement"], [9]),
    phrase("В къщата има две стаи.", "В доме есть две комнаты.", "У будинку є дві кімнати.", 9, LESSON_9_THEME, ["home", "statement"], [9]),
    phrase("Колко стаи има?", "Сколько комнат есть?", "Скільки кімнат є?", 9, LESSON_9_THEME, ["home", "question"], [9]),

    phrase("Това е автобус.", "Это автобус.", "Це автобус.", 10, LESSON_10_THEME, ["transport", "statement"], [10]),
    phrase("Това е трамвай.", "Это трамвай.", "Це трамвай.", 10, LESSON_10_THEME, ["transport", "statement"], [10]),
    phrase("Това е влак.", "Это поезд.", "Це потяг.", 10, LESSON_10_THEME, ["transport", "statement"], [10]),
    phrase("Това е спирка.", "Это остановка.", "Це зупинка.", 10, LESSON_10_THEME, ["transport-place", "statement"], [10]),
    phrase("Къде е спирката?", "Где остановка?", "Де зупинка?", 10, LESSON_10_THEME, ["transport-place", "question"], [10]),
    phrase("Пътувам с автобус.", "Я еду на автобусе.", "Я їду автобусом.", 10, LESSON_10_THEME, ["transport", "statement"], [10]),
    phrase("Пътувам с метро.", "Я еду на метро.", "Я їду на метро.", 10, LESSON_10_THEME, ["transport", "statement"], [10]),
    phrase("Искам билет за влак.", "Я хочу билет на поезд.", "Я хочу квиток на потяг.", 10, LESSON_10_THEME, ["ticket", "statement"], [10]),
    phrase("Да, имам билет.", "Да, у меня есть билет.", "Так, у мене є квиток.", 10, LESSON_10_THEME, ["ticket", "answer"], [10]),
    phrase("Не, нямам билет.", "Нет, у меня нет билета.", "Ні, у мене немає квитка.", 10, LESSON_10_THEME, ["ticket", "answer"], [10]),

    phrase("Сутрин ставам рано.", "Утром я встаю рано.", "Уранці я встаю рано.", 11, LESSON_11_THEME, ["routine", "morning"], [11]),
    phrase("Сутрин пия кафе.", "Утром я пью кофе.", "Уранці я п'ю каву.", 11, LESSON_11_THEME, ["routine", "morning"], [11]),
    phrase("Сутрин закусвам.", "Утром я завтракаю.", "Уранці я снідаю.", 11, LESSON_11_THEME, ["routine", "morning"], [11]),
    phrase("Денем работя в офиса.", "Днём я работаю в офисе.", "Удень я працюю в офісі.", 11, LESSON_11_THEME, ["routine", "day"], [11]),
    phrase("Вечер се прибирам вкъщи.", "Вечером я возвращаюсь домой.", "Увечері я повертаюся додому.", 11, LESSON_11_THEME, ["routine", "evening"], [11]),
    phrase("Вечер гледам телевизия.", "Вечером я смотрю телевизор.", "Увечері я дивлюся телевізор.", 11, LESSON_11_THEME, ["routine", "evening"], [11]),
    phrase("Нощем спя добре.", "Ночью я сплю хорошо.", "Уночі я сплю добре.", 11, LESSON_11_THEME, ["routine", "night"], [11]),
    phrase("Кога ставаш?", "Когда ты встаёшь?", "Коли ти встаєш?", 11, LESSON_11_THEME, ["routine", "question"], [11]),
    phrase("Ставам в седем часа.", "Я встаю в семь часов.", "Я встаю о сьомій годині.", 11, LESSON_11_THEME, ["routine", "answer"], [11]),
    phrase("Лягаш ли рано?", "Ты ложишься рано?", "Ти лягаєш рано?", 11, LESSON_11_THEME, ["routine", "question"], [11]),
]


def main() -> None:
    data = json.loads(ASSET.read_text(encoding="utf-8"))

    existing_words = {(entry["lessonBook"], entry["bg"], entry["type"]) for entry in data["words"]}
    for entry in WORDS:
        key = (entry["lessonBook"], entry["bg"], entry["type"])
        if key not in existing_words:
            data["words"].append(entry)
            existing_words.add(key)

    existing_phrases = {(entry["lessonBook"], entry["bg"]) for entry in data["phrases"]}
    for entry in PHRASES:
        key = (entry["lessonBook"], entry["bg"])
        if key not in existing_phrases:
            data["phrases"].append(entry)
            existing_phrases.add(key)

    ASSET.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print("expanded textbook dictionary for lessons 7..11")


if __name__ == "__main__":
    main()
