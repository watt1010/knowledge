package com.watt.mvc.service;

import com.watt.mvc.dao.QADao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QAService {
    private QADao qaDao;

    @Autowired
    public QAService(QADao qaDao) {
        this.qaDao = qaDao;
    }

    /**
     * 查询所有的问题列表，根据start 和 end 作为大数据下的分页
     */
    public List<Map<String, String>> queryQuestions(int start, int end) {
        Map param = new HashMap<>();
        param.put("start", start);
        param.put("end", end);
        return qaDao.queryQuestions(param);
    }

    /**
     * 查询所有的词典列表
     */
    public List<Map<String, String>> queryDictionaryAll() {
        return qaDao.queryDictionaryAll();
    }

    /**
     * 添加税务专用名词词典
     *
     * @param word 词条或语料
     * @return int
     */
    public int addTaxDictionaryWord(String word) {
        return qaDao.addTaxDictionaryWord(word);
    }

    /**
     * 查询同义词列表
     */
    public List<String> querySynonyms() {
        return qaDao.querySynonyms();
    }

    /**
     * 根据条件查询同义词词典
     */
    public List<Map<String, String>> querySynonymsAll(String type) {
        Map<String, String> param = new HashMap<>();
        param.put("type", type);
        return qaDao.querySynonymsAll(param);
    }

    /**
     * 根据问题的id查询答案
     */
    public Map<String, String> queryAnswer(String key) {
        Map<String, String> map = new HashMap<>();
        map.put("key",key);
        List<Map<String, String>> result = qaDao.queryAnswer(map);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    /**
     * 查询全、简称词典
     */
    public List<Map<String, String>> queryAbbreviation(Map<String, String> param) {
        return qaDao.queryAbbreviation(param);
    }

    /**
     * 向日志表中插入一条数据，写一条日志
     */
    public int createLog(String question, String score, String kw_id, String channel_id, String user_id) {
        Map<String, String> param = new HashMap<>();
        param.put("question", question);
        param.put("score", score);
        param.put("kw_id", kw_id);
        param.put("channel_id", channel_id);
        param.put("user_id", user_id);
        return qaDao.createLog(param);
    }

    public List<String> queryStopWordsAll() {
        return qaDao.queryStopWordsAll();
    }

    public String queryMediaUrlByREF_ID(String key) {
        Map<String, String> param = new HashMap<>();
        param.put("REF_ID",key);
        Map map = qaDao.queryMediaUrlByREF_ID(param);
        return map.get("MEDIA_URL").toString();
    }
}
