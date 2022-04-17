package com.pconiq.assignment.stock.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlValidationException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 2051919583043386173L;
    
    protected String errorCode;
    
    public SqlValidationException(String message) {
        super(message);
    }

    public SqlValidationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
