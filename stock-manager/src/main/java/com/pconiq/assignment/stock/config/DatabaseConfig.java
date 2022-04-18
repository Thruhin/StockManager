package com.pconiq.assignment.stock.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.pconiq.assignment.stock.EnvVariables;
import com.pconiq.assignment.stock.util.Constants;

@Configuration
public class DatabaseConfig {
    
    @Autowired
    private EnvVariables env;
    
    
    /**
     * Creates a DataSource object and assigns it to Class variable that can be reused
     * @return DataSource
     */
    @Bean
    @Profile("DEFAULT")
    public DataSource createPostgresDataSource() {
        String url = env.getDataSourceUrl();
        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
        ds.setDriverClassName(Constants.POSTGRES_DRIVER);
        ds.setUrl(url);
        ds.setUsername(env.getDbUserName());
        ds.setPassword(env.getDbPassword());
        ds.setInitialSize(Constants.POSTGRES_INITIAL_SIZE);
        ds.setMaxActive(Constants.POSTGRES_MAX_ACTIVE);
        ds.setMinIdle(Constants.POSTGRES_MIN_IDLE);
        ds.setMaxIdle(Constants.POSTGRES_MAX_IDLE);
        ds.setValidationQuery(Constants.POSTGRES_VALIDATION_QUERY);
        ds.setValidationInterval(1000);
        ds.setTestWhileIdle(true);
        ds.setJmxEnabled(true);
        ds.setTestOnBorrow(true);
        return ds;
    }
    
}
