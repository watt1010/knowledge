package com.inspur.tax.mvc.dao;

import com.inspur.tax.data.jdbc.MySqlSessionTemplate;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class QADao {
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    public QADao(MySqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate.getSqlSessionTemplate();
    }

    /**
     * 查询所有的问题列表，根据start 和 end作为大数据下的分页
     *
     */
    public List<Map<String, String>> queryQuestions(Map param) {
        return sqlSessionTemplate.selectList("QADao.queryQuestions", param);
    }

    /**
     * 查找答案
     * @param map kwid
     * @return 一个答案
     */
    public List<Map<String, String>> queryAnswer(Map map) {
        return sqlSessionTemplate.selectList("QADao.queryAnswer", map);
    }
    /**
     * 查询所有的词典列表
     */
    public List<Map<String, String>> queryDictionaryAll() {
        return sqlSessionTemplate.selectList("QADao.queryDictionaryAll", null);
    }

    /**
     * 根据条件查询同义词词典
     */
    public List<Map<String, String>> querySynonymsAll(Map<String, String> param) {
        return sqlSessionTemplate.selectList("QADao.querySynonymsAll", param);
    }
    /**
     * 添加税务专用名词词典
     *
     * @param word 词条或语料
     * @return int
     */
    public int addTaxDictionaryWord(String word) {
        return sqlSessionTemplate.insert("QADao.addTaxDictionaryWord", word);
    }

    /**
     * 向日志表中插入一条数据，写一条日志
     */
    public int createLog(Map<String, String> param) {
        return sqlSessionTemplate.insert("QADao.createLog", param);
    }
    /**
     * 查询同义词列表
     */
    public List<String> querySynonyms(){
        return sqlSessionTemplate.selectList("QADao.querySynonyms");
    }

    /**
     * 查询全、简称词典
     */
    public List<Map<String, String>> queryAbbreviation(Map<String, String> param) {
        return sqlSessionTemplate.selectList("QADao.queryAbbreviation", param);
    }

    /**
     * 查询所有停用词
     */
    public List<String> queryStopWordsAll() {
        return sqlSessionTemplate.selectList("QADao.queryStopWordsAll");
    }
    public List queryUsers() {
        return sqlSessionTemplate.selectList("QADao.queryUsers");
    }

    public Map queryMediaUrlByREF_ID(Map<String, String> param) {
        return sqlSessionTemplate.selectOne("QADao.queryMediaUrlByREF_ID",param);
    }

}
