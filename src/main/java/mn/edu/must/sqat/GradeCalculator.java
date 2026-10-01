package mn.edu.must.sqat;

/**
 * Оюутны оноогоор нийлбэр оноо болон үсгэн дүн тооцно.
 * Үнэлгээний бүтэц: ирц 10, лаб+бие даалт 40, сорил1 10, сорил2 10, шалгалт 30 (нийт 100).
 */
public class GradeCalculator {

    public static final double MAX_ATTENDANCE = 10;
    public static final double MAX_LAB = 40;
    public static final double MAX_QUIZ1 = 10;
    public static final double MAX_QUIZ2 = 10;
    public static final double MAX_EXAM = 30;

    // 90+ -> A, 80-89 -> B, 70-79 -> C, 60-69 -> D, <60 -> F
    // score нь 0-100 хязгаараас гарвал IllegalArgumentException шиднэ
    public String letterGrade(double score) {
        if (Double.isNaN(score) || score < 0 || score > 100) {
            throw new IllegalArgumentException("Оноо 0-100 хооронд байх ёстой: " + score);
        }
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    // Ирц(10), лаб+бие даалт(40), сорил1(10), сорил2(10), шалгалт(30)-ийн
    // оноонуудаас нийлбэр оноог тооцно. Аль нэг нь СӨРӨГ эсвэл дээд хязгаараасаа
    // хэтэрсэн бол IllegalArgumentException шиднэ.
    public double totalScore(double att, double lab, double quiz1, double quiz2, double exam) {
        check("ирц", att, MAX_ATTENDANCE);
        check("лаб", lab, MAX_LAB);
        check("сорил1", quiz1, MAX_QUIZ1);
        check("сорил2", quiz2, MAX_QUIZ2);
        check("шалгалт", exam, MAX_EXAM);
        return att + lab + quiz1 + quiz2 + exam;
    }

    private static void check(String name, double value, double max) {
        if (Double.isNaN(value) || value < 0 || value > max) {
            throw new IllegalArgumentException(
                    name + " оноо 0-" + (int) max + " хооронд байх ёстой: " + value);
        }
    }
}
