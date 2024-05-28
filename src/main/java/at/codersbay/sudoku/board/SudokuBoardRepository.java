package at.codersbay.sudoku.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SudokuBoardRepository extends JpaRepository<SudokuBoard, Long> {

}
