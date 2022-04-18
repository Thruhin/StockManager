package com.pconiq.assignment.stock.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "postgresEntityManagerFactory",
    transactionManagerRef = "postgresTransactionManager",
    basePackages = {"com.pconiq.assignment.stock.repo.repository"})
@EnableCaching
@EnableTransactionManagement
//@DependsOn("globalSchemaConfiguration")
public class HibernateConfig {
    
    @Autowired
    private DataSource dataSource;
    
    @Bean
    public JpaVendorAdapter globalJPAVendorAdapter() {
      return new HibernateJpaVendorAdapter();
    }

    @Bean(name = "postgresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean globalPostgresEntityManagerFactory()
         {
      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
      em.setJpaVendorAdapter(globalJPAVendorAdapter());
      em.setPackagesToScan("com.pconiq.assignment.stock.repo.entity");
      em.setDataSource(dataSource);
      return em;
    }

    @Bean(name = "postgresTransactionManager")
    public synchronized PlatformTransactionManager postgresTransactionManager()
        {
      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(globalPostgresEntityManagerFactory().getObject());
      transactionManager.setDataSource(dataSource);
      return transactionManager;
    }

}
