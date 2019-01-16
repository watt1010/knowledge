package com.inspur.tax.data.jdbc;

import com.inspur.tax.configure.DbConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

@Configuration
public class MySqlDataSource {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private DbConfig dbConfig;

    @Autowired
    public MySqlDataSource(DbConfig dbConfig) {
        logger.info("MySqlDataSource OK");
        this.dbConfig = dbConfig;
    }

    @Bean
    public ComboPooledDataSource getDataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(dbConfig.getDriverClass());
            dataSource.setJdbcUrl(dbConfig.getJdbcUrl());
            dataSource.setUser(dbConfig.getUser());
            dataSource.setPassword(dbConfig.getPassword());
            dataSource.setMinPoolSize(1);
            dataSource.setMaxPoolSize(2);
            dataSource.setInitialPoolSize(1);
            dataSource.setMaxIdleTime(180);
            dataSource.setAcquireRetryAttempts(30);
            return dataSource;
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return null;
    }
}

