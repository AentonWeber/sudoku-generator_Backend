package at.codersbay.sudoku.board;

import com.google.gson.Gson;

public class SudokuBoardMapper {

    public static SudokuBoard convertBoardToEntity(int[][] board) {
        Gson gson = new Gson();
        String boardJson = gson.toJson(board);
        SudokuBoard sudokuBoard = new SudokuBoard();

        sudokuBoard.setBoard(boardJson);

        return sudokuBoard;
    }


    public static SudokuBoardDTO mapEntityToDTO(SudokuBoard sudokuBoard) {
        SudokuBoardDTO sudokuBoardDTO = new SudokuBoardDTO();
        sudokuBoardDTO.setId(sudokuBoard.getId());
        Gson gson = new Gson();
        sudokuBoardDTO.setBoard(gson.fromJson(sudokuBoard.getBoard(), int[][].class));

        return sudokuBoardDTO;
    }

}
