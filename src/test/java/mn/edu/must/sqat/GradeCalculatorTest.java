package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GradeCalculator — нэгжийн тестүүд")
class GradeCalculatorTest {

    private static final double EPS = 1e-9;

    private GradeCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new GradeCalculator();                       // Arrange (бүх тестэд нийтлэг)
    }

    // ---------- letterGrade: ердийн утгууд ----------

    @Test
    @DisplayName("Ердийн утгууд: 95→A, 85→B, 75→C, 65→D, 30→F")
    void typicalScoresMapToEachGrade() {
        // Arrange: calc (setUp)
        // Act + Assert: assertAll — нэг нь унасан ч бусдыг нь шалгана
        assertAll(
                () -> assertEquals("A", calc.letterGrade(95)),
                () -> assertEquals("B", calc.letterGrade(85)),
                () -> assertEquals("C", calc.letterGrade(75)),
                () -> assertEquals("D", calc.letterGrade(65)),
                () -> assertEquals("F", calc.letterGrade(30)));
    }

    // ---------- letterGrade: хязгаарын утгууд ----------

    @Test
    @DisplayName("90 оноо яг A дүн байх ёстой (хязгаарын тохиолдол)")
    void ninetyIsExactlyA() {
        double score = 90.0;                                // Arrange
        String grade = calc.letterGrade(score);             // Act
        assertEquals("A", grade);                           // Assert
    }

    @Test
    @DisplayName("89.99 оноо B дүн — A-гийн хязгаарын яг доор")
    void justBelowNinetyIsB() {
        double score = 89.99;                               // Arrange
        String grade = calc.letterGrade(score);             // Act
        assertEquals("B", grade);                           // Assert
    }

    @Test
    @DisplayName("60 оноо яг D дүн — тэнцэх доод хязгаар")
    void sixtyIsExactlyD() {
        double score = 60.0;                                // Arrange
        String grade = calc.letterGrade(score);             // Act
        assertEquals("D", grade);                           // Assert
    }

    @Test
    @DisplayName("59.99 оноо F дүн — тэнцэх хязгаарын яг доор")
    void justBelowSixtyIsF() {
        double score = 59.99;                               // Arrange
        String grade = calc.letterGrade(score);             // Act
        assertEquals("F", grade);                           // Assert
    }

    @Test
    @DisplayName("0 оноо F дүн — хүчинтэй мужийн доод зах")
    void zeroIsF() {
        double score = 0.0;                                 // Arrange
        String grade = calc.letterGrade(score);             // Act
        assertEquals("F", grade);                           // Assert
    }

    @Test
    @DisplayName("100 оноо A дүн — хүчинтэй мужийн дээд зах")
    void hundredIsA() {
        double score = 100.0;                               // Arrange
        String grade = calc.letterGrade(score);             // Act
        assertEquals("A", grade);                           // Assert
    }

    // ---------- letterGrade: буруу оролт ----------

    @Test
    @DisplayName("-1 оноо IllegalArgumentException шиднэ")
    void negativeScoreThrows() {
        double score = -1;                                  // Arrange
        assertThrows(IllegalArgumentException.class,        // Act + Assert
                () -> calc.letterGrade(score));
    }

    @Test
    @DisplayName("101 оноо IllegalArgumentException шиднэ")
    void scoreAboveHundredThrows() {
        double score = 101;                                 // Arrange
        assertThrows(IllegalArgumentException.class,        // Act + Assert
                () -> calc.letterGrade(score));
    }

    @Test
    @DisplayName("100.01 ба NaN оноо IllegalArgumentException шиднэ")
    void slightlyAboveHundredAndNaNThrow() {
        // Arrange: 100-гийн яг дээд хязгаар ба тоо биш утга
        assertAll(                                          // Act + Assert
                () -> assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(100.01)),
                () -> assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(Double.NaN)));
    }

    // ---------- totalScore ----------

    @Test
    @DisplayName("Бүх хэсэг дээд оноотой (10, 40, 10, 10, 30) бол нийлбэр 100")
    void allMaximumsSumToHundred() {
        double att = 10, lab = 40, q1 = 10, q2 = 10, exam = 30;    // Arrange
        double total = calc.totalScore(att, lab, q1, q2, exam);    // Act
        assertEquals(100.0, total, EPS);                           // Assert
    }

    @Test
    @DisplayName("Бүх хэсэг 0 бол нийлбэр 0 (доод хязгаар)")
    void allZerosSumToZero() {
        double total = calc.totalScore(0, 0, 0, 0, 0);             // Arrange + Act
        assertEquals(0.0, total, EPS);                             // Assert
    }

    @Test
    @DisplayName("Ирц сөрөг (att = -5) бол IllegalArgumentException")
    void negativeAttendanceThrows() {
        double att = -5;                                           // Arrange
        assertThrows(IllegalArgumentException.class,               // Act + Assert
                () -> calc.totalScore(att, 40, 10, 10, 30));
    }

    @Test
    @DisplayName("Лаб дээд хязгаараас хэтэрсэн (lab = 41) бол IllegalArgumentException")
    void labAboveMaxThrows() {
        double lab = 41;                                           // Arrange
        assertThrows(IllegalArgumentException.class,               // Act + Assert
                () -> calc.totalScore(10, lab, 10, 10, 30));
    }

    @Test
    @DisplayName("Сорил1, сорил2, шалгалт тус бүр хязгаараас хэтэрвэл exception")
    void eachOtherComponentAboveMaxThrows() {
        // Arrange: бусад нь хүчинтэй, зөвхөн нэг хэсэг +0.01-ээр хэтэрнэ
        assertAll(                                                 // Act + Assert
                () -> assertThrows(IllegalArgumentException.class, () -> calc.totalScore(10.01, 40, 10, 10, 30)),
                () -> assertThrows(IllegalArgumentException.class, () -> calc.totalScore(10, 40, 10.01, 10, 30)),
                () -> assertThrows(IllegalArgumentException.class, () -> calc.totalScore(10, 40, 10, 10.01, 30)),
                () -> assertThrows(IllegalArgumentException.class, () -> calc.totalScore(10, 40, 10, 10, 30.01)));
    }

    @Test
    @DisplayName("Бутархай оноонуудын нийлбэр яг 90 бол A дүн авна (float нийлбэрийн алдаа)")
    void fractionalPartsSummingToNinetyGiveA() {
        // Arrange: 8.8 + 35.3 + 9.3 + 9.4 + 27.2 = 90.0 (математикийн хувьд)
        double att = 8.8, lab = 35.3, q1 = 9.3, q2 = 9.4, exam = 27.2;
        // Act
        double total = calc.totalScore(att, lab, q1, q2, exam);
        String grade = calc.letterGrade(total);
        // Assert: оюутан яг 90 авсан тул A байх ёстой
        assertAll(
                () -> assertEquals(90.0, total, 0.0),
                () -> assertEquals("A", grade));
    }
}
