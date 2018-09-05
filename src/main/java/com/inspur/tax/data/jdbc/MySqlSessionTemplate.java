package com.inspur.tax.data.jdbc;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySqlSessionTemplate {
    private static SqlSessionTemplate sqlSessionTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MySqlSessionFactoryBean mySqlSessionFactoryBean;

    @Autowired
    public MySqlSessionTemplate(MySqlSessionFactoryBean mySqlSessionFactoryBean) {
        logger.info("MySqlSessionTemplate OK");
        this.mySqlSessionFactoryBean = mySqlSessionFactoryBean;
    }

    @Bean
    public SqlSessionTemplate getSqlSessionTemplate() {
        try {
            if (sqlSessionTemplate == null) {
                sqlSessionTemplate = new SqlSessionTemplate(mySqlSessionFactoryBean.getSqlSessionFactoryBean().getObject());
            }
            return sqlSessionTemplate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
