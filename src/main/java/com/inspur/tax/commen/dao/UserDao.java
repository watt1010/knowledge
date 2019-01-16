package com.inspur.tax.commen.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserDao {
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    public Map queryUserIdByApp_key(Map map) {
        return sqlSessionTemplate.selectOne("UserDao.queryUserIdByApp_key", map);
    }
}
