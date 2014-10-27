### Задача №1 "Поиск шаблонов в тексте"
Студент 393 группы Федоров Андрей.

### Структура репозитория

- папка *src* - содержит исходный код задания.
	* *phoenix.templatesearcher.api*
	        Здесь лежат базовые интерфейсы.
	* *phoenix.templatesearcher.matchers*
		    Здесь лежат классы, обеспечивающие поиск.
	* *phoenix.templatesearcher.support*
		    Здесь лежат вспомогательные классы.
	* *phoenix.templatesearcher.algo*
	        Здесь лежат реализации алгоритмов (префикс-функция, бор).
	* *phoenix.templatesearcher.exception*
	        Здесь лежат собственные классы исключений.
- папка *test* - содержит исходный код тестов к заданию.
	* *phoenix.templatesearcher.test.algo*
	        Здесь лежат тесты на реализации алгоритмов (префикс-функция, бор).
	* *phoenix.templatesearcher.test.matchers.parameterized*
            Здесь лежат параметризованные тесты JUnit, которые выполняются многоразово для случайных данных. Данные генерируются в самих же тестах в формате JUnit Parameterized runner.
	* *phoenix.templatesearcher.test.matchers.once*
		    Здесь лежат тесты на некоторые особенности matchers, которые можно проверить один раз
	* *phoenix.templatesearcher.test.support*
		    Здесь лежат вспомогательные классы для тестов.

### Что сделано

На текущий момент реализованы пункты 3.1, 3.2, 3.3, 4.1, 4.2, 4.3 задания вместе с тестами к ним.