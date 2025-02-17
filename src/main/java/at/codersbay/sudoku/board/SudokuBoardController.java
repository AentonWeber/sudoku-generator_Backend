package at.codersbay.sudoku.board;


import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@CrossOrigin
@RestController
public class SudokuBoardController {

    private static final Logger LOG = Logger.getLogger(SudokuBoardController.class.getName());


    private final SudokuBoardService sudokuBoardService;

    public SudokuBoardController(SudokuBoardService sudokuBoardService) {

        this.sudokuBoardService = sudokuBoardService;
    }

    @GetMapping("/generate")
    public ResponseEntity<SudokuBoardDTO> generateSudokuBoard() {
        LOG.info("generate");

        SudokuBoardDTO sudokuBoard = sudokuBoardService.createSudokuBoard(9);
        return ResponseEntity.ok(sudokuBoard);
    }


    @GetMapping("/take")
    public ResponseEntity<SudokuBoardDTO> takeFromDB(@Param("id") Long id) {
        LOG.info("take");
        SudokuBoardDTO board = sudokuBoardService.findBoardById(id);
        if (board == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(board);
    }

    @GetMapping("/idList")
    public ResponseEntity<List<Long>> idListFromDB(@Param("id") Long id) {
        LOG.info("idList");
        List<Long> boardsIds = sudokuBoardService.getAllBoardIds();
        return ResponseEntity.ok(boardsIds);
    }

    @PostMapping("/check")
    public ResponseEntity<SudokuResponse> checkSudoku(@RequestBody SudokuBoardDTO request) {
        LOG.info("checkSudoku");
        boolean isCorrect = sudokuBoardService.isValidSudoku(request.getBoard());
        return ResponseEntity.ok(new SudokuResponse(isCorrect));
    }
}
