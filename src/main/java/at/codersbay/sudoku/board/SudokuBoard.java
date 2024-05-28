package at.codersbay.sudoku.board;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name="sudokuboard")
public class SudokuBoard {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String board;

    public Long getId() {
        return id;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }
}
