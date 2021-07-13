package com.bosch.sast.sudoku.validator.model.exception;

import java.text.MessageFormat;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException(Long id){
        super(MessageFormat.format("Could not find board with id: {0}", id));
    }
}
