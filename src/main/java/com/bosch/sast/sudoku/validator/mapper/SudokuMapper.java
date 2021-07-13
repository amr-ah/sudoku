package com.bosch.sast.sudoku.validator.mapper;

import com.bosch.sast.sudoku.validator.model.Board;
import org.springframework.stereotype.Component;
import com.bosch.sast.sudoku.validator.dto.NewBoardDTO;
import com.bosch.sast.sudoku.validator.dto.BoardDTO;
import com.bosch.sast.sudoku.validator.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * As H2 DB doesn't really allow storing the "grid" IE the 2d arrays we will
 * need some trickery to map between the DTOs and entities.
 * Any ideas how to make it smarter?
 */

@Component
public class SudokuMapper {
    /**
     * @return Board
     * maps from a newBoardDTO to a board with a 1D List
     */
    public Board MapToBoard(NewBoardDTO newBoardDTO) {
        List<Integer> cells = new ArrayList<>();
        try{
            for (int row = 0; row < Constants.BOARD_SIZE; row++) {
                for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                    cells.add(newBoardDTO.getGrid()[row][col]);
                }
            }
            Board board = new Board();
            board.setCells(cells);
            return board;
        }catch (ArrayIndexOutOfBoundsException e){
            return null;
        }

    }

    /**
     * @return BoardDTO
     * maps from a board with a flat 1D list to a matrix
     */
    public BoardDTO MapToBoardDTO(Board board) {
        int[][] grid = new int[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        int row = -1;
        int col = -1;
        for (int i = 0; i < Constants.BOARD_SIZE * Constants.BOARD_SIZE; i++) {
            if (i % Constants.BOARD_SIZE == 0) {
                row++;
                col = -1;
            }
            col++;
            grid[row][col] = board.getCells().get(i);
        }
        return boardDTO.setGrid(grid);
    }

}
