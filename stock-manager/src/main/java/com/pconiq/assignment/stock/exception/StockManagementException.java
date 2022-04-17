package com.pconiq.assignment.stock.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockManagementException extends Exception{
    /**
     * Generated UUID.
     */
    private static final long serialVersionUID = 1L;


    private String errorCode;

    private String message;

    private HttpStatus statusCode;

    public StockManagementException(String erroCode, String message, HttpStatus statusCode) {
      this.errorCode = erroCode;
      this.message = message;
      this.statusCode = statusCode;
    }
    
    public StockManagementException(String message) {
      this.message = message;
    }

}
