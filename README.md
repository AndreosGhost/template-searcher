### Задача №1 "Поиск шаблонов в тексте"
Студент 393 группы Федоров Андрей.

### Структура репозитория

<ul>
<li>
папка src - содержит исходный код задания
	<ul>
	<li>
	phoenix.templatesearcher.api
		Здесь лежат базовые интерфейсы
	</li>
	<li>
	phoenix.templatesearcher.matchers
		Здесь лежат классы, обеспечивающие поиск
	</li>
	<li>
	phoenix.templatesearcher.support
		Здесь лежат вспомогательные классы
	</li>
	</ul>
</li>
<li>
папка test - содержит исходный код тестов к заданию
	<ul>
	<li>
	phoenix.templatesearcher.test
		Здесь лежат параметризованные тесты JUnit, которые выполняются многоразово для случайных данных. Данные генерируются в самих же тестах в формате JUnit Parameterized runner.
	</li>
	<li>
	phoenix.templatesearcher.test.once
		Здесь лежат тесты на некоторые особенности, которые можно проверить один раз
	</li>
	<li>
	phoenix.templatesearcher.test.support
		Здесь лежат вспомогательные классы для тестов
	</li>
	</ul>
</li>
</ul>

### Что сделано

На текущий момент реализованы пункты 3.1 и 3.2 задания: NaiveTemplateMatcher, SingleTemplateMatcher вместе с тестами (суффиксы -Test, -TestOnce)

