package enums;

public enum SudokuSize {
    SMALL(3),
    MEDIUM(4),
    LARGE(5);

    final int areaSize;
    final int fieldSize;

    SudokuSize(int areaSize) {
        this.areaSize = areaSize;
        this.fieldSize = areaSize * areaSize;
    }

    public int getAreaSize() {
        return areaSize;
    }

    public int getFieldSize() {
        return fieldSize;
    }
}
