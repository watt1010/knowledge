package com.inspur.tax.commen;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component("keyConfig")
@ConfigurationProperties
@PropertySource("classpath:/key.properties")
public class KeyConfig {

    private String media_host;
    private String pub_key;
    private String pri_key;


    public String getPub_key() {
        return pub_key;
    }

    public void setPub_key(String pub_key) {
        this.pub_key = pub_key;
    }


    public String getPri_key() {
        return pri_key;
    }

    public void setPri_key(String pri_key) {
        this.pri_key = pri_key;
    }


    public String getMedia_host() {
        return media_host;
    }

    public void setMedia_host(String media_host) {
        this.media_host = media_host;
    }


}
