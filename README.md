# Лаборатори №4 — Нэгжийн тестийн эхлэл: JUnit 5

**Оюутан:** Эрдэнэбаяр
**Оюутны код:** B232270136
**Хичээл:** F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест (2026)

---

## 1. Ажлын орчин

```
$ java -version
openjdk version "17.0.20.1" 2026-08-18
OpenJDK Runtime Environment Temurin-17.0.20.1+1 (build 17.0.20.1+1)
OpenJDK 64-Bit Server VM Temurin-17.0.20.1+1 (build 17.0.20.1+1, mixed mode, sharing)

$ mvn -version
Apache Maven 3.9.9 (8e8579a9e76f7d015ee5ec7bfcdc97d260186937)
Maven home: C:\Users\THE_FEDzUP\tools\apache-maven-3.9.9
Java version: 17.0.20.1, vendor: Eclipse Adoptium, runtime: C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot
Default locale: en_US, platform encoding: Cp1252
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

| Зүйл | Утга |
|---|---|
| JDK | Eclipse Temurin OpenJDK 17.0.20.1 (**JDK** — `javac 17.0.20.1` байгаа, JRE биш) |
| Build | Apache Maven 3.9.9 |
| Тест хүрээ | JUnit Jupiter 5.10.2, maven-surefire-plugin 3.2.5 |
| ҮС | Windows 11 Pro (26200), Git Bash |

> **Орчны тайлбар.** Заавар Ubuntu-д зориулагдсан; энэ машин дээр WSL байхгүй тул Windows дээр
> өөрөө тохируулсан: JDK-г `winget install EclipseAdoptium.Temurin.17.JDK`-ээр, Maven-ийг
> Apache-ийн албан ёсны binary zip-ээс суулгав. Командууд Git Bash дээр Linux-тай ижилхэн
> (`mvn test 2>&1 | tee ...`) ажилласан.

## 2. Төсөл үүсгэсэн нь

```bash
mvn archetype:generate -DgroupId=mn.edu.must.sqat   -DartifactId=lab04-junit -DarchetypeArtifactId=maven-archetype-quickstart   -DarchetypeVersion=1.4 -DinteractiveMode=false
```

Үүссэн `lab04-junit` төслийг репозиторийн үндсэн хавтас руу шилжүүлсэн (`pom.xml`, `src/`,
`results/`, `README.md` бүгд нэг түвшинд). `pom.xml`-д зааврын дагуу:

- `junit:junit:4.11` хамаарлыг устгаж `org.junit.jupiter:junit-jupiter:5.10.2` (test scope) нэмэв;
- `<pluginManagement>` доторх surefire **2.22.1 → 3.2.5** болгов;
- `maven.compiler.source/target 1.7` хоёр мөрийг устгаж `<maven.compiler.release>17</maven.compiler.release>` бичив;
- archetype-ийн `App.java`, `AppTest.java` (JUnit 4) файлуудыг устгав.

## 3. Репозиторийн бүтэц

```
pom.xml
src/main/java/mn/edu/must/sqat/GradeCalculator.java      тестлэгдэх класс
src/test/java/mn/edu/must/sqat/GradeCalculatorTest.java  20 тест метод
results/mvn-test.txt            ногоон гаралт — Tests run: 50, Failures: 0, BUILD SUCCESS
results/mvn-test-mutant.txt     >= 90 → > 90 мутаци — Failures: 3, BUILD FAILURE
results/mutant.patch            мутацийн яг өөрчлөлт (git diff)
results/mvn-test-float-bug.txt  float согогийг илрүүлсэн (засахаас өмнөх) гаралт
ai/                             нэмэлт даалгавар: AI-ийн тест ба мутацийн харьцуулалт
```

## 4. GradeCalculator

- `letterGrade(double score)`: `>= 90` A, `>= 80` B, `>= 70` C, `>= 60` D, бусад F;
  `score < 0`, `score > 100` эсвэл `NaN` бол `IllegalArgumentException`.
- `totalScore(att, lab, quiz1, quiz2, exam)` — **5 параметр**, дээд хязгаар 10 / 40 / 10 / 10 / 30;
  аль нэг нь сөрөг, хязгаараасаа хэтэрсэн эсвэл `NaN` бол `IllegalArgumentException`.
  Нийлбэрийг 0.01 хүртэл бөөрөнхийлнө (доорх 6-р хэсгийг үз).

## 5. Тестүүд ба тоо

| | Тоо |
|---|---|
| `@Test` метод | 16 |
| `@ParameterizedTest` метод | 4 (letterGrade: 2, totalScore: 2) |
| **Нийт тестийн метод** | **20** |
| **`results/mvn-test.txt`-ийн сүүлийн `Tests run:`** | **50** (Failures: 0, Errors: 0, Skipped: 0, BUILD SUCCESS) |

50 = 16 энгийн тест + `letterGradeBoundaries` 11 мөр + `letterGradeOutOfRangeThrows` 6 утга +
`totalScoreValidSums` 7 мөр + `totalScoreInvalidPartThrows` 10 мөр (Surefire parameterized мөр
бүрийг тусад нь тоолдог).

Тест бүр Arrange–Act–Assert бүтэцтэй (коммент-оор тэмдэглэсэн), бүгд монгол `@DisplayName`-тэй.
Хамрагдсан утгууд:

- **Ердийн:** 95→A, 85→B, 75→C, 65→D, 30→F
- **Хязгаар:** 90→A, 89.99→B, 60→D, 59.99→F, 0→F, 100→A (+ 80, 79.99, 70, 69.99 parameterized-д)
- **letterGrade буруу оролт (`assertThrows`):** -1, 101, 100.01, NaN, -100, -0.01, 1000
- **totalScore зөв:** (10,40,10,10,30)→100, бүгд 0→0, бутархай нийлбэрүүд
- **totalScore буруу оролт (`assertThrows`):** att = -5, lab = 41, хэсэг бүрийн сөрөг ба хэтэрсэн утга

## 6. Хамгийн сонирхолтой тест — float нийлбэрийн согог

`fractionalPartsSummingToNinetyGiveA` тест нь 8.8 + 35.3 + 9.3 + 9.4 + 27.2 гэсэн, математикийн
хувьд яг 90 болох оноонуудыг `totalScore`-д өгөөд үр дүнг нь `letterGrade`-д дамжуулдаг.
Анхны хэрэгжүүлэлт энгийн `att + lab + quiz1 + quiz2 + exam` байсан бөгөөд тест
`expected: <90.0> but was: <89.99999999999999>` ба `expected: <A> but was: <B>` гэж унасан
(`results/mvn-test-float-bug.txt`, Tests run: 16, Failures: 1). Өөрөөр хэлбэл яг 90 оноо авсан
оюутан double-ийн хоёртын бутархайн алдаанаас болж B дүн авах байсан — энэ бол хязгаарын
утга дээр нуугдсан жинхэнэ согог. Үүнийг нийлбэрийг `Math.round(sum * 100.0) / 100.0`-аар
0.01 хүртэл бөөрөнхийлж зассан (оноог хоёр оронгийн нарийвчлалтай бүртгэдэг гэж үзэв), дараа
нь бүх тест ногоон болсон. Энэ тест мөн мутацийн шатанд `>= 90 → > 90` мутантыг давхар
илрүүлсэн. Сургамж нь: `letterGrade(90.0)` гэж шууд тест бичихэд ногоон байсан ч, бодит
урсгал болох "нийлбэр → дүн" гинжин хэлхээг хязгаар дээр нь шалгаагүй бол согог нуугдсаар
үлдэх байсан.

## 7. Мутаци (санаатай унагаах)

`letterGrade` доторх `if (score >= 90) return "A";` мөрийг `if (score > 90) return "A";`
болгож (`results/mutant.patch`) `mvn test 2>&1 | tee results/mvn-test-mutant.txt` ажиллуулав:

```
[ERROR] Tests run: 50, Failures: 3, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
```

Унасан 3 тест (бүгд `expected: <A> but was: <B>`):

| Тест | Шалтгаан |
|---|---|
| `ninetyIsExactlyA` — "90 оноо яг A дүн байх ёстой (хязгаарын тохиолдол)" | 90.0 нь `> 90` нөхцөлийг хангахгүй тул B буцаасан |
| `letterGradeBoundaries` [3] — `letterGrade(90) = A` мөр | мөн адил 90 хязгаар |
| `fractionalPartsSummingToNinetyGiveA` | нийлбэр 90.0 → мутант B буцаасан |

Дараа нь `>= 90`-ийг буцааж засаад `mvn test 2>&1 | tee results/mvn-test.txt`-ийг дахин
ажиллуулахад **Tests run: 50, Failures: 0, BUILD SUCCESS** болсон (`results/mvn-test.txt` нь
энэ сүүлийн ногоон гаралт).
