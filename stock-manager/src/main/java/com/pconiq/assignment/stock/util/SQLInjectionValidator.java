package com.pconiq.assignment.stock.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

/**
 * Utility class perform validations against SQL injections
 */
public class SQLInjectionValidator {
    
    org.slf4j.Logger logger = LoggerFactory.getLogger(SQLInjectionValidator.class.getName());

    private SQLInjectionValidator() {

    }

    public static final Pattern pattern = Pattern.compile(Constants.SQL_INJECTION_REGEX,
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    /**
     * Validate the field value for SQL injection
     *
     * @param fieldValue String field to be validated
     * @return  true if field is SQL injected else returns false if the field is null or SQL Injected
     * or SQL Injected
     */
    public static boolean isFieldSQLInjected(String fieldValue) {
        if (StringUtils.isNotBlank(fieldValue) && pattern.matcher(fieldValue).find()) {
            return true;
        }
        return false;
    }
}
