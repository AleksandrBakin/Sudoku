import java.util.Date;

public class SudokuApp {
    public static void main(String[] args) {
        Date start = new Date();

        Field field = new Field();
        System.out.println(field);

        long runtime = new Date().getTime() - start.getTime();
        System.out.println("Runtime: " + runtime + " ms");
    }
}
