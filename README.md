### Задача №1 "Поиск шаблонов в тексте"
Студент 393 группы Федоров Андрей.

### Структура репозитория

папка src - содержит исходный код задания
	phoenix.templatesearcher.api
		Здесь лежат базовые интерфейсы
	phoenix.templatesearcher.matchers
		Здесь лежат классы, обеспечивающие поиск
	phoenix.templatesearcher.support
		Здесь лежат вспомогательные классы

папка test - содержит исходный код тестов к заданию
	phoenix.templatesearcher.test
		Здесь лежат параметризованные тесты JUnit, которые выполняются многоразово для случайных данных. Данные генерируются в самих же тестах в формате JUnit Parameterized runner.
	phoenix.templatesearcher.test.once
		Здесь лежат тесты на некоторые особенности, которые можно проверить один раз
	phoenix.templatesearcher.test.support
		Здесь лежат вспомогательные классы для тестов

### Что сделано

На текущий момент реализованы пункты 3.1 и 3.2 задания: NaiveTemplateMatcher, SingleTemplateMatcher вместе с тестами (суффиксы -Test, -TestOnce)
