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
