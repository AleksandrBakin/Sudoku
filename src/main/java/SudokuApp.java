import enums.SudokuLevel;
import enums.SudokuSize;

import java.util.Date;

public class SudokuApp {
    public static void main(String[] args) {
        Date start = new Date();

        Field field = new Field(SudokuSize.SMALL, SudokuLevel.EASY);
        System.out.println(field);

        long runtime = new Date().getTime() - start.getTime();
        System.out.println("Runtime: " + runtime + " ms");
//        int[][] mySudoku = new int[][] {
////              {1,2,3,4,5,6,7,8,9}, just example os str
//                {0,0,4,8,6,0,0,3,0},
//                {0,0,1,0,0,0,0,9,0},
//                {8,0,0,0,0,9,0,6,0},
//                {5,0,0,2,0,6,0,0,1},
//                {0,2,7,0,0,1,0,0,0},
//                {0,0,0,0,4,3,0,0,6},
//                {0,5,0,0,0,0,0,0,0},
//                {0,0,9,0,0,0,4,0,0},
//                {0,0,0,4,0,0,0,1,5}
//        };
//        new Field(SudokuSize.SMALL).solveThis(mySudoku);



    }
}
