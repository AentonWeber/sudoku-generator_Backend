package at.codersbay.sudoku.board;

public class SudokuBoardDTO {

    int[][] board;
    Long id;


    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
