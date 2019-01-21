package com.watt.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "db.mybatis")
public class MybatisConfig {
    private String mybatisXml;

    public String getMybatisXml() {
        return mybatisXml;
    }

    public void setMybatisXml(String mybatisXml) {
        this.mybatisXml = mybatisXml;
    }
}
