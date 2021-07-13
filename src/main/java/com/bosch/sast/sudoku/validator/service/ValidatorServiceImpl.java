package com.bosch.sast.sudoku.validator.service;

import com.bosch.sast.sudoku.validator.Constants;
import com.bosch.sast.sudoku.validator.dto.BoardDTO;
import com.bosch.sast.sudoku.validator.dto.NewBoardDTO;
import com.bosch.sast.sudoku.validator.mapper.SudokuMapper;
import com.bosch.sast.sudoku.validator.model.Board;
import com.bosch.sast.sudoku.validator.model.exception.BoardNotFoundException;
import com.bosch.sast.sudoku.validator.repository.SudokuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidatorServiceImpl implements ValidatorService {
    private final SudokuRepository sudokuRepository;
    private final SudokuMapper sudokuMapper;

    @Autowired
    public ValidatorServiceImpl(SudokuRepository sudokuRepository, SudokuMapper sudokuMapper) {
        this.sudokuRepository = sudokuRepository;
        this.sudokuMapper = sudokuMapper;
    }

    @Override
    public boolean isValidSudoku(long boardId) {
        BoardDTO boardDTO = getBoard(boardId);
        return isValidSudoku(boardDTO.getGrid());
    }

    @Override
    /**
     *  the use of a bitVector was because of the assumption that the board size is constant 9x9 grid
     */
    public boolean isValidSudoku(int[][] grid) {
        int[] rows = new int[Constants.BOARD_SIZE];
        int[] columns = new int[Constants.BOARD_SIZE];
        int[][] blocks = new int[3][3];

        for (int r = 0; r < Constants.BOARD_SIZE; r++) {
            for (int c = 0; c < Constants.BOARD_SIZE; c++) {
                if (grid[r][c] != 0) {

                    if (grid[r][c] < 0 || grid[r][c] > Constants.BOARD_SIZE) {
                        return false;
                    }
                    if (((rows[r] >>> grid[r][c]) & 1) != 0) {
                        return false;
                    }
                    if (((columns[c] >>> grid[r][c]) & 1) != 0) {
                        return false;
                    }
                    if (((blocks[r / 3][c / 3] >>> grid[r][c]) & 1) != 0) {
                        return false;
                    }
                }
                rows[r] |= 1 << grid[r][c];
                columns[c] |= 1 << grid[r][c];
                blocks[r / 3][c / 3] |= 1 << grid[r][c];
            }
        }
        return true;
    }

    @Override
    public BoardDTO saveBoard(NewBoardDTO newBoard) {
        Board board = sudokuMapper.MapToBoard(newBoard);
        if (board != null) {
            board = sudokuRepository.save(board);
            return sudokuMapper.MapToBoardDTO(board);
        } else {
            return null;
        }
    }

    @Override
    public BoardDTO getBoard(long id) {
        Board board = sudokuRepository.findById(id).orElseThrow(() -> new BoardNotFoundException(id));
        return sudokuMapper.MapToBoardDTO(board);
    }
}
