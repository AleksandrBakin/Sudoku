package enums;

public enum SudokuLevel {
    EASY(0, 0.55),
    MEDIUM(0, 0.61),
    HARD(1, 0.67),
    SUPER_HARD(1, 0.73),
    EXTREME(2, 0.79);

    final int emptyLines;
    final double emptyCellsPercent;

    SudokuLevel(int emptyLines, double emptyCellsPercent){
        this.emptyLines = emptyLines;
        this.emptyCellsPercent = emptyCellsPercent;
    }

    public int getEmptyLines() {
        return emptyLines;
    }

    public double getEmptyCellsPercent() {
        return emptyCellsPercent;
    }
}
