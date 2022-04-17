package com.pconiq.assignment.stock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class EnvVariables {
    
    @Value("${spring.datasource.url:}")
    private String dataSourceUrl;
    
    @Value("${spring.datasource.username:}")
    private String dbUserName ;

    @Value("${spring.datasource.password:}")
    private String dbPassword;
    
    @Value("${executeInitialScripts:}")
    private boolean executeInitialScripts;
    
    @Value("${stockSize:}")
    private int stockSize;

}
