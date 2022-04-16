package com.pconiq.assignment.stock.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pconiq.assignment.stock.exception.SqlValidationException;
import com.pconiq.assignment.stock.model.restobjects.BaseResponse;
import com.pconiq.assignment.stock.util.Constants;
import com.pconiq.assignment.stock.util.SQLInjectionValidator;

/**
 * 
 * A Filter class that checks if the request is malicious prone to sql injection
 *
 */
public class RequestValidationFilter implements Filter{
    
    Logger logger = (Logger) LoggerFactory.logger(RequestValidationFilter.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        logger.debug("Request validation filter called");
        MultiReadHttpServletRequest httpServletRequest = new MultiReadHttpServletRequest((HttpServletRequest) request);
        try {
            validateRequestParams(httpServletRequest);
        } catch (SqlValidationException exception) {
            generateErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
            return;
        }
        chain.doFilter(httpServletRequest, response);
    }

    /**
     * This method fetches all teh request parameters and checks for SqlInjection patterns
     * @param request
     * @throws SqlValidationException
     */
    private void validateRequestParams(MultiReadHttpServletRequest request) throws SqlValidationException {
        Map<String, String[]> requestParams = request.getParameterMap();
        String parameters = " ";
        for (String[] param : requestParams.values()) {
            parameters = parameters.concat(Arrays.toString(param));
        }
        if (SQLInjectionValidator.isFieldSQLInjected(parameters)) {
            throw new SqlValidationException("Invalid request: Request params contains possible SQL injection characters");
        }
    }
    
    private void generateErrorResponse(ServletResponse response, int status, String message) throws IOException {
        ((HttpServletResponse) response).setStatus(status);
        BaseResponse genericResponse = new BaseResponse(Integer.toString(status), message);
        response.setContentType(Constants.APPLICATION_JSON);
        response.getWriter().write(objectMapper.writeValueAsString(genericResponse));
        response.getWriter().flush();
        response.getWriter().close();
    }
    
    

}
