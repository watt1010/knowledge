package com.watt.data.jdbc;

import com.watt.configure.MybatisConfig;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class MySqlSessionFactoryBean {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private MySqlDataSource dataSource;
    private MybatisConfig mybatisConfig;

    @Autowired
    public MySqlSessionFactoryBean(MySqlDataSource dataSource, MybatisConfig mybatisConfig) {
        logger.info("MySqlSessionFactoryBean OK");
        this.dataSource = dataSource;
        this.mybatisConfig = mybatisConfig;
    }

    @Bean
    public SqlSessionFactoryBean getSqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource.getDataSource());
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfig.getMybatisXml()));
        return sqlSessionFactoryBean;
    }
}
