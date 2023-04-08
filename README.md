# Решённое тестовое задание на вакансию Junior Java Developer компании Deeplay

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
