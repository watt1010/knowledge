package com.inspur.tax.mvc.controller;

import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;
import com.hankcs.hanlp.dictionary.common.CommonSynonymDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.inspur.tax.core.dictionary.CoreAbbreviationDictionary;
import com.inspur.tax.core.nlp.cosinesimlarity.SimilarityAnalyze;
import com.inspur.tax.mvc.beans.CheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DictionaryController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private QAController qaController;

    @Autowired
    public DictionaryController(QAController qaController) {
        this.qaController = qaController;
    }

    /**
     * 为了避免一词多义现象，对向量值的影响。 同义词保存和修改前校验如下：
     * 1.是否同义词组中的至少一个词存在于向量词表中；
     * 2.所有的词在词林词典中不得重复
     */
    @RequestMapping("/checkSynonymHaveVectors")
    public CheckResult checkSynonymHaveVectors(@RequestParam(name = "synonyms") String synonyms) {
        String[] synonymArray = synonyms.trim().split("--");
        SimilarityAnalyze similarityAnalyze = qaController.getSimilarAnalyze();
        for (String synonym : synonymArray) {
            if (similarityAnalyze.getVec().getWordVector(synonym) != null) {
                break;
            }
            return new CheckResult("001", "经检查所有的的同义词在向量表中都不存在", "");
        }
        for (String synonym : synonymArray) {
            CommonSynonymDictionary.SynonymItem item = CoreSynonymDictionary.get(synonym);
            if (item != null && item.synonymList != null && !item.synonymList.isEmpty()) {
                return new CheckResult("002", "经检查“" + synonym + "”已经在词林词典中，请不要重复添加", "");
            }
        }
        return new CheckResult("000", "success", "");
    }

    /**
     * 检查全称、简称词
     * 是否包含在向量词表中，在词表中不允许维护到词典中
     * 全称必须是可分词的数据
     * 简称在词典中不得重复，是唯一的值
     * 提示维护到同义词词典中。
     */
    @RequestMapping("/checkAbbreviation")
    public CheckResult checkAbbreviation(@RequestParam(name = "abbreviation") String abbreviation, @RequestParam(name = "full") String full) {
        SimilarityAnalyze similarityAnalyze = qaController.getSimilarAnalyze();
        if (similarityAnalyze.getVec().getWordVector(abbreviation) != null) {
            return new CheckResult("001", "简称词存在于词向量词表中，建议使用同义词的方式维护到系统中", "");
        }
        if (similarityAnalyze.getVec().getWordVector(full) != null) {
            return new CheckResult("002", "全称词存在于词向量词表中，建议使用同义词的方式维护到系统中", "");
        }
        if (CoreAbbreviationDictionary.getAbbreviation(abbreviation) != null) {
            return new CheckResult("003", "简称已经存在，不得重复，请直接编辑", "");
        }
        List<Term> terms = qaController.getSegment().seg(full);
        logger.info("检查全称、简称词，全称共分词为:" + terms);
        if (terms.size() <= 1) {
            return new CheckResult("004", "全称词必须能够分词若干个颗粒度，不能是一个单词", "");
        }
        return new CheckResult("000", "success", "");
    }
}
