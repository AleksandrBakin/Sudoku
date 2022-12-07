import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field {
    private int[][] solutionField;
    private int[][] emptyField;
    int size = 9;
    int areaSize = (int) Math.sqrt(size);

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


    public Field() {
        this.solutionField = new int[this.size][this.size];
        genRandBox(0,0);
        this.solutionField = genField(this.solutionField);
    }

    private void genRandBox(int row, int col){
        List<Integer> availableNums = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            availableNums.add(i);
        }
        int[][] box = new int[areaSize][areaSize];
        for (int i = 0; i < areaSize; i++) {
            for (int j = 0; j < areaSize; j++) {
                box[i][j] = availableNums.get(random.nextInt(availableNums.size()));
                availableNums.remove((Integer) box[i][j]);
            }
        }

//        box = new int[][] { {1,2,3} , {4,5,6} ,{7,8,9} };  // test

        for (int i = 0; i < areaSize; i++) {
            for (int j = 0; j < areaSize; j++) {
                solutionField[row + i][col + j] = box[i][j];
            }
        }
    }

    private int[][] genField(int[][] field){
        int[] firstEmpty = findEmpty(field);
        if(firstEmpty == null) {
            return field;
        }
        int emptyI = firstEmpty[0];
        int emptyJ = firstEmpty[1];

        int startValue = random.nextInt(size);
        int[] values = new int[size];
        for (int i = 0; i < size; i++) { values[i] = ((startValue + i) % size) + 1; }
        for (int i = 0; i < size; i++) {
            int value = values[i];
            if(isValid(value, emptyI, emptyJ, field)){
                int[][] newField = copyOfField(field);
                newField[emptyI][emptyJ] = value;
                if((newField = genField(newField)) != null){
                    return newField;
                }
                // 
            }
        }
        return null;
    }

    private int[] findEmpty(int[][] field){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(field[i][j] == 0) {
                    return new int[] {i,j};
                }
            }
        }
        return null;
    }

    private boolean isValid(int val, int row, int col, int[][] field) {
        for (int i = 0; i < size; i++) {
            if(field[i][col] == val) { return false; }
        }
        for (int j = 0; j < size; j++) {
            if(field[row][j] == val) { return false; }
        }
        int baseI = row / areaSize * areaSize;
        int baseJ = col / areaSize * areaSize;
        for (int i = 0; i < areaSize; i++) {
            for (int j = 0; j < areaSize; j++) {
                if(field[baseI+i][baseJ+j] == val) { return false; }
            }
        }
        return true;
    }

    public int[][] getSolution() {
        return solutionField;
    }
    public int[][] getEmptyField() {
        return emptyField;
    }
    @Override
    public String toString() {
        int numSize = String.valueOf(size).length();
        StringBuilder sb = new StringBuilder();

        if (numSize == 1) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < areaSize; j++) {
                    sb.append("[");
                    for (int k = 0; k < areaSize; k++) {
                        sb.append(solutionField[i][(j*areaSize)+k]);
                        if(k != areaSize - 1) { sb.append(" "); }
                    }
                    sb.append("]  ");
                }
                sb.append("\n");
                if(i % areaSize == areaSize - 1) { sb.append("\n"); }
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < areaSize; j++) {
                    sb.append("[");
                    for (int k = 0; k < areaSize; k++) {
                        String tmp = String.valueOf(solutionField[i][(j*areaSize)+k]);
                        if(tmp.length() == numSize) { sb.append(tmp); }
                        else {
                            int delta = numSize - tmp.length();
                            sb.append("0".repeat(delta)).append(tmp);
                        }
                        if(k != areaSize - 1) { sb.append(" "); }
                    }
                    sb.append("]  ");
                }
                sb.append("\n");
                if(i % areaSize == areaSize - 1) { sb.append("\n"); }
            }
        }
        return sb.toString();
    }
    private int[][] copyOfField(int[][] field) {
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = field[i][j];
            }
        }
        return result;
    }
}
