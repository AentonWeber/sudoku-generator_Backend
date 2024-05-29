package at.codersbay.sudoku.board;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SudokuBoardService {


    private ArrayList<Set<Integer>> rows;
    private ArrayList<Set<Integer>> columns;
    private ArrayList<Set<Integer>> boxes;
    private int size;
    private int[][] board;
    private Set<Integer> availableNumbers;
    private int squareRootSize;

    private final SudokuBoardRepository sudokuBoardRepository;

    public SudokuBoardService(SudokuBoardRepository sudokuBoardRepository) {
        this.sudokuBoardRepository = sudokuBoardRepository;
    }

    public SudokuBoardDTO createSudokuBoard(int size) {
        this.size = size;
        this.squareRootSize = (int) Math.sqrt(size);
        if (squareRootSize * squareRootSize != size) {
            squareRootSize++;
        }

        board = new int[size][size];
        rows = new ArrayList<>();
        columns = new ArrayList<>();
        boxes = new ArrayList<>();
        availableNumbers = new HashSet<>();
        int checkNumbers = (size / 3) * (size / 3);

        for (int i = 0; i < checkNumbers; i++) {
            availableNumbers.add(i + 1);
        }

        for (int i = 0; i < size; i++) {
            rows.add(new HashSet<>());
            columns.add(new HashSet<>());
        }

        for (int i = 0; i < squareRootSize; i++) {
            for (int j = 0; j < squareRootSize; j++) {
                boxes.add(new HashSet<>());
            }
        }

        preFill();
        restFill(0, squareRootSize);
        digHoleNumber();

        SudokuBoard sudokuBoard = SudokuBoardMapper.convertBoardToEntity(board);
        sudokuBoard = sudokuBoardRepository.save(sudokuBoard);

        return SudokuBoardMapper.mapEntityToDTO(sudokuBoard);
    }

    private int boxPosition(int row, int column) {
        int boxRow = row / 3;
        int boxColumn = column / 3;
        return boxRow * 3 + boxColumn;
    }

    private boolean isValid(int row, int column, int num) {
        Set<Integer> result = new HashSet<>(availableNumbers);
        result.removeAll(rows.get(row));
        result.removeAll(columns.get(column));
        result.removeAll(boxes.get(boxPosition(row, column)));
        return result.contains(num);
    }

    private void setNumber(int row, int column, int num) {
        if (isValid(row, column, num)) {
            saveNumber(row, column, num);
            board[row][column] = num;
        }
    }

    private void saveNumber(int row, int column, int num) {
        rows.get(row).add(num);
        columns.get(column).add(num);
        boxes.get(boxPosition(row, column)).add(num);
    }

    private void deleteNumber(int row, int column, int num) {
        rows.get(row).remove(num);
        columns.get(column).remove(num);
        boxes.get(boxPosition(row, column)).remove(num);
    }

    private void preFill() {
        int grid = size / 3;
        fillSection(0, 0);
        fillSection(grid, grid);
        fillSection(grid * 2, grid * 2);
    }

    private void fillSection(int startRow, int startColumn) {
        List<Integer> numbers = new ArrayList<>(availableNumbers);
        Collections.shuffle(numbers);

        int index = 0;

        for (int i = startRow; i < startRow + size / 3; i++) {
            for (int j = startColumn; j < startColumn + size / 3; j++) {
                if (index < numbers.size()) {
                    setNumber(i, j, numbers.get(index));
                    index++;
                }
            }

        }

    }

    private boolean restFill(int row, int col) {

        if (col >= size && row < size - 1) {
            row = row + 1;
            col = 0;
        }
        if (row >= size && col >= size) {
            return true;
        }

        if (row < squareRootSize) {
            if (col < squareRootSize) {
                col = squareRootSize;
            }
        } else if (row < size - squareRootSize) {
            if (col == (row / squareRootSize) * squareRootSize) {
                col = col + squareRootSize;
            }
        } else {
            if (col == size - squareRootSize) {
                row = row + 1;
                col = 0;
                if (row >= size) {
                    return true;
                }
            }
        }

        for (int num = 1; num <= availableNumbers.size(); num++) {
            if (isValid(row, col, num)) {
                board[row][col] = num;
                saveNumber(row, col, num);
                if (restFill(row, col + 1)) {
                    return true;
                }
                board[row][col] = 0;
                deleteNumber(row, col, num);
            }
        }
        return false;
    }

    private void digHoleNumber() {
        Random r = new Random();
        boolean[][] grid = new boolean[size][size];

        int zeroFields = 0;
        int notPossible = 0;

        int fieldsVisited = 0;


        while (zeroFields < 40) {
            int row = r.nextInt(size);
            int col = r.nextInt(size);

            if (!grid[row][col]) {
                if (possibleDigHole(row, col, board[row][col])) {
                    board[row][col] = 0;
                    zeroFields++;
                } else {
                    notPossible++;
                }
                grid[row][col] = true;
                fieldsVisited++;
            }

            if (notPossible > 500 || fieldsVisited == size * size) {
                resetBoard();
                zeroFields = 0;
                notPossible = 0;
                fieldsVisited = 0;
                grid = new boolean[size][size];
            }
        }
    }

    private boolean possibleDigHole(int row, int col, int savedNumber) {
        int count = 0;
        if (board[row][col] == 0) {
            return false;
        }
        deleteNumber(row, col, savedNumber);
        for (int i = 1; i <= 9; i++) {

            if (isValid(row, col, i)) {
                count++;
                if (count > 1) {
                    setNumber(row, col, savedNumber);
                    return false;
                }
            }
        }
        return true;
    }

    public void resetBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
            }
        }
        rows.clear();
        columns.clear();
        boxes.clear();
        for (int i = 0; i < size; i++) {
            rows.add(new HashSet<>());
            columns.add(new HashSet<>());
        }
        for (int i = 0; i < squareRootSize; i++) {
            for (int j = 0; j < squareRootSize; j++) {
                boxes.add(new HashSet<>());
            }
        }
        preFill();
        restFill(0, squareRootSize);
    }

    public SudokuBoardDTO findBoardById(long id) {
        Optional<SudokuBoard> boardOptional = sudokuBoardRepository.findById(id);
        return boardOptional.map(SudokuBoardMapper::mapEntityToDTO).orElse(null);
    }

    public List<Long> getAllBoardIds() {
        return sudokuBoardRepository.findAll().stream()
                .map(SudokuBoard::getId)
                .collect(Collectors.toList());
    }

    public boolean isValidSudoku(int[][] board) {

        for (int i = 0; i < 9; i++) {
            boolean[] rowCheck = new boolean[9];
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0 || rowCheck[board[i][j] - 1]) {
                    return false;
                }
                rowCheck[board[i][j] - 1] = true;
            }
        }


        for (int i = 0; i < 9; i++) {
            boolean[] colCheck = new boolean[9];
            for (int j = 0; j < 9; j++) {
                if (board[j][i] == 0 || colCheck[board[j][i] - 1]) {
                    return false;
                }
                colCheck[board[j][i] - 1] = true;
            }
        }


        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) {
                boolean[] blockCheck = new boolean[9];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int current = board[row + i][col + j];
                        if (current == 0 || blockCheck[current - 1]) {
                            return false;
                        }
                        blockCheck[current - 1] = true;
                    }
                }
            }
        }

        return true;
    }
}