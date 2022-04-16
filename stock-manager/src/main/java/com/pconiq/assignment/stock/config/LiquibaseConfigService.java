package com.pconiq.assignment.stock.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pconiq.assignment.stock.EnvVariables;
import com.pconiq.assignment.stock.util.Constants;

@Component
public class LiquibaseConfigService {
    
    @Autowired
    private EnvVariables env;
    
    public DataSource dataSource;
    
    /**
     * Creates a DataSource object and assigns it to Class variable that can be reused
     * @return DataSource
     */
    public DataSource createPostgresDataSource() {
        
        if(dataSource == null) {
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
            dataSource = ds;
        } 
        return dataSource;
    }

}
