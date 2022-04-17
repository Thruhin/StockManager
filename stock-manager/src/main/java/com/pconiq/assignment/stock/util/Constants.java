package com.pconiq.assignment.stock.util;

public class Constants {
    
    public static final String POSTGRES_DRIVER = "org.postgresql.Driver";
    
    public static final int POSTGRES_INITIAL_SIZE = 5;
    public static final int POSTGRES_MAX_ACTIVE = 50;
    public static final int POSTGRES_MIN_IDLE = 5;
    public static final int POSTGRES_MAX_IDLE = 10;
    
    public static final String POSTGRES_VALIDATION_QUERY = "SELECT 1";
    
    public static final String TABLE_STOCK = "STOCK";
    public static final String TABLE_STOCK_PRICE_HISTORY = "STOCK_PRICE_HISTORY";
    
    public static final Integer DEFAULT_SKIP_VALUE = 0;
    public static final Integer DEFAULT_TOP_VALUE = 200;
    
    public static final String REGEX_VALID_CHARS_URL = "^[0-9a-zA-Z]+([a-zA-Z0-9\\-_\\'\\s.&=\\/$:#]+[0-9a-zA-Z])*$";
    public static final String DESC_REGEX = "^((?!<)(?!>).)*$";
    public static final String REGEX_VALID_NAME = "^[a-zA-Z0-9\\-_\\'\\s]+$";
    public static final String SQL_INJECTION_REGEX = "('(''|[^'])*')|(;)|(=)|(\\()|(\\))|(\\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})\\b)";
    
    public static final String APPLICATION_JSON = "application/json";
    
    //Status Codes
    public static final String STOCKS_FETCH_SUCCESS = "S-100";
    public static final String STOCK_FETCH_SUCCESS = "S-101";
    public static final String STOCK_PRICE_HISTORY_FETCH_SUCCESS = "S-102";
    public static final String STOCK_CREATE_SUCCESS = "S-103";
    public static final String STOCK_UPDATE_SUCCESS = "S-104";
    public static final String STOCK_DELETE_SUCCESS = "S-105";
    
    public static final String STOCKS_FETCH_FAILED = "S-201";
    public static final String STOCK_WITH_ID_FETCH_FAILED = "S-202";
    public static final String STOCK_WITH_ID_FETCH_FAILED_WITH_EXCEPTION = "S-203";
    public static final String STOCK_PRICE_HISTORY_FAILED = "S-204";
    public static final String STOCK_CREATE_FAILED = "S-205";
    public static final String STOCK_UPDATE_FAILED = "S-206";
    public static final String STOCK_DELETE_FAILED = "S-105";
    
    //public static final String STOCKS_FETCH_FAILED = "Failed to get the stocks";
    //public static final String STOCKS_FETCH_FAILED = "Failed to get the stocks";
    //public static final String STOCKS_FETCH_FAILED = "Failed to get the stocks";
    
    
    
    
    
}
