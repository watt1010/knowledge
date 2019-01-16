package com.inspur.tax.scene.service;


import com.inspur.tax.scene.dao.SceneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SceneService {
    private  SceneDao sceneDao;

    @Autowired
    public SceneService(SceneDao sceneDao) {
        this.sceneDao = sceneDao;
    }

    public SceneService() {

    }

    public Map isScene(Map map) {
        return sceneDao.isScene(map);
    }

    public Map queryNodeSpeech(Map map) {
        return sceneDao.queryNodeSpeech(map);
    }

    public List<Map> queryEnSon(Map map) {
        return sceneDao.queryEnSon(map);
    }

    public Map queryAttValue(Map map) {
        return sceneDao.queryAttValue(map);
    }

    public List<Map> querySame(Map map) {
        return sceneDao.querySame(map);
    }

    public SceneDao getSceneDao() {
        return sceneDao;
    }

    public void setSceneDao(SceneDao sceneDao) {
        this.sceneDao = sceneDao;
    }

    public List<String> querySpeechWord(Map en_name) {return sceneDao.querySpeechWord(en_name);
    }

    public List queryHaveSon(Map map) {
        return sceneDao.queryHaveSon(map);
    }
}
