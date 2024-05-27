package at.codersbay.sudoku.board;

class SudokuResponse {
    private boolean correct;

    public SudokuResponse(boolean correct) {
        this.correct = correct;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}