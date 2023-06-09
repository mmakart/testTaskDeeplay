# Решённое тестовое задание на вакансию Junior Java Developer компании Deeplay

## Что делает программа

Программа подсчитывает количество объектов по заданным в файле критериям. При запуске она считывает 3 файла:

1. Файл со списком возможных свойств и их возможных значений.
2. Файл с критериями поиска в Дизъюнктивной Нормальной Форме (см. файл criteria.txt).
3. Файл со списком объектов (вернее их свойств) в формате csv, где разделитель - запятая. Данные в строке должны располагаться в том же порядке, что и в файле в пункте 1.

## Инструкция по запуску

Необходимо запустить следующие команды в терминале:

```
mvn package
java -jar target/testTaskDeeplay-0.0.1-SNAPSHOT.jar animalPropertySet.txt criteria.txt animals.txt
```

### Ожидаемый результат вывода:

```
dietType.herbivore: 1
dietType.herbivore | dietType.carnivore & height.low: 2
dietType.omnivore & !height.tall: 2
!height.low & height.low: 0
!weight.heavy & dietType.omnivore: 2
```

Это означает, что количество травоядных животных равно 1, количество травоядных или плотоядных с низким весом - 2, количество всеядных не высоких животных - 2, количество низких и не низких одновременно - 0 (что ожидаемо) и количество не тяжёлых всеядных животных - 2.

Менять можно любые из 3 входных текстовых файлов по своему усмотрению.
