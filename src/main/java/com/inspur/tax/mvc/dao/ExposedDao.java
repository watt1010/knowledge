package com.inspur.tax.mvc.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ExposedDao {

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    public List<Map> queryTop5Qu(Map map) {
        return sqlSessionTemplate.selectList("ExposedDao.queryTop5Qu",map);
    }
}
