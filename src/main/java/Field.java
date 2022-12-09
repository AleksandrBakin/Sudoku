import enums.SudokuCell;
import enums.SudokuLevel;
import enums.SudokuSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field {
    private int[][] solutionField;
    private int[][] tmpField;
    private int[][] gameField;
    private boolean[][] rowFlags;
    private boolean[][] colFlags;
    private boolean[][] boxFlags;

    private List<SudokuCell> uncheckedCells;
    private final int size;
    private final int areaSize;

    private final Random random = new Random();

    // Coordinates of field
    // { (0,0) (0,1) (0,2) | (0,3) (0,4) (0,5) | (0,6) (0,7) (0,8) }
    // { (1,0) (1,1) (1,2) | (1,3) (1,4) (1,5) | (1,6) (1,7) (1,8) }
    // { (2,0) (2,1) (2,2) | (2,3) (2,4) (2,5) | (2,6) (2,7) (2,8) }
    //   - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // { (3,0) (3,1) (3,2) | (3,3) (3,4) (3,5) | (3,6) (3,7) (3,8) }
    // { (4,0) (4,1) (4,2) | (4,3) (4,4) (4,5) | (4,6) (4,7) (4,8) }
    // { (5,0) (5,1) (5,2) | (5,3) (5,4) (5,5) | (5,6) (5,7) (5,8) }
    //   -----------------------------------------------------------
    // { (6,0) (6,1) (6,2) | (6,3) (6,4) (6,5) | (6,6) (6,7) (6,8) }
    // { (7,0) (7,1) (7,2) | (7,3) (7,4) (7,5) | (7,6) (7,7) (7,8) }
    // { (8,0) (8,1) (8,2) | (8,3) (8,4) (8,5) | (8,6) (8,7) (8,8) }

    // Difficulty levels - ???
    // Easy - 37-43% (45 - 50 %) completed, N = 0 , 9 x 9: 38,
    // Medium - 32 - 37% (40 - 45 %) completed, N = 1 , 9 x 9: 30,
    // Hard - 25 - 32% (35 - 40 %) completed N = 2
    // SuperHard (30 - 35 %)
    // Extreme (25 - 30 %)
    // N - max number of empty str


    public Field(SudokuSize sudokuSize, SudokuLevel level) {
        this.size = sudokuSize.getFieldSize();
        this.areaSize = sudokuSize.getAreaSize();
        this.tmpField = new int[this.size][this.size];

        genRandFirstBox();
        initializeFlags();

        solve(0, 0);

        solutionField = copyOfField(tmpField);

        genGameField(level);
    }

    Field (SudokuSize sudokuSize) {
        this.size = sudokuSize.getFieldSize();
        this.areaSize = sudokuSize.getAreaSize();
        this.tmpField = new int[this.size][this.size];
    }

    private void genRandFirstBox() {
        List<Integer> availableNums = new ArrayList<>();
        for (int i = 1; i <= size; i++) { availableNums.add(i); }
        for (int i = 0; i < areaSize; i++) {
            for (int j = 0; j < areaSize; j++) {
                tmpField[i][j] = availableNums.get(random.nextInt(availableNums.size()));
                availableNums.remove((Integer) tmpField[i][j]);
            }
        }
    }

    private boolean solve(int nextI, int nextJ) {
        if(nextI == size) {
            nextI = 0;
            if(++nextJ == size) { return true; } // totally complete
        }
        if(tmpField[nextI][nextJ] != 0) {
            return solve(nextI + 1, nextJ);
        }

        // values min = 1,  max = size, start = rand,
        // example size = 4, arr = 2,3,4,1 / 4,1,2,3 for more rand in field gen
        int startValue = random.nextInt(size);
        int[] values = new int[size];
        for (int i = 0; i < size; i++) { values[i] = ((startValue + i) % size) + 1; }

        for (int i = 0; i < size; i++) {
            int value = values[i];
            if(isValid(value, nextI, nextJ)){
                tmpField[nextI][nextJ] = value;
                setFlags(nextI, nextJ, value, true);

                if(solve(nextI + 1, nextJ)) {
                    return true;
                }

                setFlags(nextI, nextJ, value, false);
            }
        }
        tmpField[nextI][nextJ] = 0;
        return false;
    }

    private boolean isValid(int val, int row, int col) {
        boolean rowIsAvailable = !rowFlags[row][val-1];
        boolean calIsAvailable = !colFlags[col][val-1];
        boolean boxIsAvailable = !boxFlags[((row / areaSize) * areaSize) + (col / areaSize)][val-1];
        return rowIsAvailable && calIsAvailable && boxIsAvailable;
    }

    public int[][] getSolutionField() {
        return solutionField;
    }

    public int[][] getGameField() {
        return gameField;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(solutionField != null) {
            sb.append("Solution field:\n").append(fieldToString(solutionField));
            sb.append("\n\n");
        }
        if(gameField != null) {
            sb.append("Game field:\n").append(fieldToString(gameField));
            sb.append("\n\n");
        }
        if(tmpField != null) {
            sb.append("Service field:\n").append(fieldToString(tmpField));
        }

        return sb.toString();
    }

    private String fieldToString(int[][] field) {
        StringBuilder sb = new StringBuilder();

        int maxNumLen = String.valueOf(size).length();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(j % areaSize == 0) {
                    if(j != 0) {
                        sb.append(" ");
                    }
                    sb.append("[");
                }
                if(maxNumLen == 2) {
                    sb.append(field[i][j] < 10 ? "0" + field[i][j] : field[i][j]);
                } else {
                    sb.append(field[i][j]);
                }
                if(j % areaSize == areaSize - 1) {
                    sb.append("]");
                } else {
                    sb.append(" ");
                }
            }
            if(i != size - 1) {
                sb.append("\n");
                if(i % areaSize == areaSize - 1) { sb.append("\n"); }
            }
        }
        return sb.toString();
    }
    private int[][] copyOfField(int[][] field) {
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(field[i], 0, result[i], 0, size);
        }
        return result;
    }

    private void genGameField(SudokuLevel level) {
        gameField = copyOfField(tmpField);
        int cellsToDelete = (int)(level.getEmptyCellsPercent() * size * size);
        int linesToDelete = level.getEmptyLines();
        initializeUncheckedCells();
        while (cellsToDelete > 0) {
            if(uncheckedCells.size() == 0) { break; }
            SudokuCell cellToCheck = getRandCell();
            int row = cellToCheck.getRow();
            int col = cellToCheck.getCol();
            boolean lastInLine = lastInLine(row,col);
            if(lastInLine && linesToDelete == 0) { continue; }
            int val = gameField[row][col];
            boolean hasAnotherSolution = false;
            for (int i = 1; i <= size ; i++) {
                if (i == val) { continue; }
                tmpField = copyOfField(gameField);
                tmpField[row][col] = 0;
                initializeFlags();
                if(isValid(i, row, col)) {
                    tmpField[row][col] = i;
                    setFlags(row,col,i,true);
                    hasAnotherSolution = solve(0, 0);
                    if (hasAnotherSolution) { break; }
                }
            }
            if(hasAnotherSolution) { continue; }
            gameField[row][col] = 0;
            cellsToDelete--;
            if(lastInLine) { linesToDelete--; }
        }
    }

    private boolean lastInLine(int row, int col){
        boolean lastInRow = true;
        boolean lastInCal = true;
        for (int j = 0; j < size; j++) { // going threw the row
            if(tmpField[row][j] != 0) {
                if(j != col) {
                    lastInRow = false;
                    break;
                }
            }
        }
        for (int i = 0; i < size; i++) { // going threw the column
            if(tmpField[i][col] != 0) {
                if(i != row) {
                    lastInCal = false;
                    break;
                }
            }
        }
        return lastInCal || lastInRow;
    }
    private SudokuCell getRandCell() {
        SudokuCell randCell = uncheckedCells.get(random.nextInt(uncheckedCells.size()));
        uncheckedCells.remove(randCell);
        return randCell;
    }
    private void initializeUncheckedCells(){
        uncheckedCells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                uncheckedCells.add(new SudokuCell(i,j));
            }
        }
    }

    private void setFlags(int i, int j, int val, boolean status) {
        rowFlags[i][val - 1] = status;
        colFlags[j][val - 1] = status;
        boxFlags[((i / areaSize) * areaSize) + (j / areaSize)][val - 1] = status;
    }

    private void initializeFlags() {
        rowFlags = new boolean[this.size][this.size];
        colFlags = new boolean[this.size][this.size];
        boxFlags = new boolean[this.size][this.size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(tmpField[i][j] != 0) {
                    setFlags(i,j, tmpField[i][j], true);
                }
            }
        }
    }

    public void solveThis(int[][] field) {
        tmpField = copyOfField(field);
        initializeFlags();
        solve(0,0);
        System.out.println(fieldToString(tmpField));
    }
}
