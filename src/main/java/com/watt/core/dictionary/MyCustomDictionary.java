package com.watt.core.dictionary;

import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.watt.mvc.service.QAService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 词典相关的内容
 */
@Component
public class MyCustomDictionary {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final QAService qaService;

    @Autowired
    public MyCustomDictionary(QAService qaService) {
        this.qaService = qaService;
    }

    public void initDictionary() {
        List<Map<String, String>> words = qaService.queryDictionaryAll();
        words.forEach(word -> {
            if (word != null && !word.isEmpty()) {
                CustomDictionary.add(word.get("WORD"));
            }
        });
        logger.info("共加载自定义词典：" + words.size());
    }

    public void initCiLinSynonyms() {
        List<String> synonyms = qaService.querySynonyms();
        CoreSynonymDictionary.reload(synonyms);
        logger.info("共加载同义词词汇：" + synonyms.size());
    }

    public void initStopWords() {
        List<String> stopWords = qaService.queryStopWordsAll();
        stopWords.forEach(CoreStopWordsDictionary::addStopWord);
        logger.info("共加载停用词词典：" + stopWords.size());
    }

    /**
     * 初始化全、简称词典，将简称全部加入到词典中去
     */
    public void initAbbreviation() {
        List<Map<String, String>> abbreviations = qaService.queryAbbreviation(null);
        abbreviations.forEach(abbreviation -> {
            CustomDictionary.add(abbreviation.get("abbr_name"));
            CoreAbbreviationDictionary.addAbbreviation(abbreviation.get("abbr_name"), abbreviation.get("full_name"));
        });
        logger.info("共加载全、简称词典：" + abbreviations.size());
    }

    /**
     * 添加税务专用名词词典,永久生效
     *
     * @param word 词条或语料
     */
    public void addTaxDictionaryWord(String word) {
        qaService.addTaxDictionaryWord(word);
        CustomDictionary.add(word);
    }
}
