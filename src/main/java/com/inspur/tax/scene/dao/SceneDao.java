package com.inspur.tax.scene.dao;

import com.inspur.tax.data.jdbc.MySqlSessionTemplate;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SceneDao {
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    public SceneDao(MySqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate.getSqlSessionTemplate();
    }


    public Map isScene(Map id) {
        return  sqlSessionTemplate.selectOne("SceneDao.isScene",id);
    }

    public Map queryNodeSpeech(Map map) {
        return sqlSessionTemplate.selectOne("SceneDao.queryNodeSpeech",map);
    }

    public List<Map> queryEnSon(Map map) {
        return sqlSessionTemplate.selectList("SceneDao.queryEnSon",map);
    }

    public Map queryAttValue(Map map) {
        return sqlSessionTemplate.selectOne("SceneDao.queryAttValue",map);
    }

    public List<Map> querySame(Map map) {
        return  sqlSessionTemplate.selectList("SceneDao.querySame",map);
    }

    public List<String> querySpeechWord(Map en_name) {
        return sqlSessionTemplate.selectList("SceneDao.querySpeechWord",en_name);
    }

    public List queryHaveSon(Map map) {
        return sqlSessionTemplate.selectList("SceneDao.queryHaveSon",map);
    }
}
