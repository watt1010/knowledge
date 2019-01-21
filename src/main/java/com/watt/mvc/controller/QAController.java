package com.watt.mvc.controller;

import com.alibaba.fastjson.JSONArray;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;
import com.hankcs.lucene.HanLPAnalyzer;
import com.watt.configure.LuceneConfig;
import com.watt.core.QuestionsIndex;
import com.watt.core.dictionary.CoreAbbreviationDictionary;
import com.watt.core.dictionary.CoreStopWordsDictionary;
import com.watt.core.dictionary.MyCustomDictionary;
import com.watt.core.nlp.cosinesimlarity.SimilarityAnalyze;
import com.watt.core.nlp.cosinesimlarity.SimilarityAnalyzeUnfamiliarWords;
import com.watt.mvc.beans.CheckResult;
import com.watt.mvc.beans.PlatformResponse;
import com.watt.mvc.beans.QAAnalyzeResult;
import com.watt.mvc.service.QAService;
import com.watt.util.CommonUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 对话实现类
 */
@RestController
public class QAController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private LuceneConfig luceneConfig;
    private QuestionsIndex questionsIndex;
    private Segment segment;
    private SimilarityAnalyze similarAnalyze = new SimilarityAnalyzeUnfamiliarWords();
    private QAService qaService;
    private MyCustomDictionary myCustomDictionary;

    @Autowired
    public QAController(LuceneConfig luceneConfig, QuestionsIndex questionsIndex, QAService qaService, MyCustomDictionary myCustomDictionary) {
        this.luceneConfig = luceneConfig;
        this.questionsIndex = questionsIndex;
        this.qaService = qaService;
        this.myCustomDictionary = myCustomDictionary;
        //加载分词热词
        myCustomDictionary.initDictionary();
        //加载停用词词典
        myCustomDictionary.initStopWords();
        //加载全、简称词典将简称字段加入到分词热词中（全称不加入）
        myCustomDictionary.initAbbreviation();
        //将所有的向量加载
        initWordVectors();
        //1.词向量矫正 2.检索所有维护的词林同义词词典 3.并将所有的税务同义词加入到热词中
        initCilin();
        //初始化分词服务
        initSeg();
    }

    /**
     * 初始化分词服务
     */
    private void initSeg() {
        segment = HanLP.newSegment();
        NotionalTokenizer.SEGMENT = segment;
    }

    /**
     * 初始化加载词向量
     */
    private void initWordVectors() {
        similarAnalyze.loadGoogleModel(luceneConfig.getVectorPath());
        logger.info("词向量加载完成");
    }

    /**
     * 加载词林词典将词林同义词赋值到向量中
     */
    private void initCilin() {
        //查询type类型为同义词的所有词汇，去掉like的词语
        //通过同义词、和专业词词典来重新校准向量值
        List<Map<String, String>> lines = qaService.querySynonymsAll("=");
        lines.forEach(line -> {
            String[] synonyms = line.get("synonym").trim().split("\\s+");
            for (String synonym : synonyms) {
                float[] vector = similarAnalyze.getVec().getWordVector(synonym);
                if (vector != null) {
                    for (String synonym1 : synonyms) {
                        similarAnalyze.getVec().setWordVector(synonym1, vector);
                        //如果存在自定义的同义词需要在此处加入到用户自定义分词词典中去
                    }
                    break;
                }
            }
        });
        logger.info("同义词词林校准向量加载完成：" + lines.size());
    }

    /**
     * 对话接口提供方法，分发多轮还是问答
     */
    @RequestMapping("/getAnswer")
    public PlatformResponse query(HttpServletRequest request) throws IOException, ParseException {
        String question = request.getParameter("question").trim().replaceAll("\\s*", "");
        JSONArray resultArray = this.searchAndCalculate(question);
        if (resultArray.isEmpty()) {
            return new PlatformResponse();
        }
        //将分析的TOP最高的问题进行评估，返回一个评分最高的答案
        resultArray = CommonUtils.arrayCompare(resultArray);
        QAAnalyzeResult qaAnalyzeResult = resultArray.getObject(0, QAAnalyzeResult.class);
        Map<String, String> answer = qaService.queryAnswer(qaAnalyzeResult.getKey());
        String media_type = answer.get("MEDIA_TYPE");
        String media_url = media_type.equals("IMG") || media_type.equals("GT") ?
                qaService.queryMediaUrlByREF_ID(answer.get("REF_ID"))
                : null;
        //保存日志
        try {
            qaService.createLog(question, qaAnalyzeResult.getScore() + "", qaAnalyzeResult.getKey(), "manager", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PlatformResponse(qaAnalyzeResult.getMatch(),
                question, qaAnalyzeResult.getScore(), resultArray,
                answer.get("TEXT_ANS"), qaAnalyzeResult.getKey(), answer.get("MEDIA_TYPE"), answer.get("REF_ID"), "1", "", media_url);
    }


    /**
     * getAnswer公共代码
     */
    private JSONArray searchAndCalculate(String question) throws IOException, ParseException {
        //将目标问题进行分词，留着分析用
        List<Term> seg_question = CoreStopWordsDictionary.removeStopWords(CoreAbbreviationDictionary.convertAbbreviationToFull(segment.seg(question)));
        logger.info("全称转换后：" + seg_question);
        IndexSearcher searcher = luceneConfig.getIndexSearcher();
        Query query = new QueryParser(luceneConfig.getIndexKey(), new HanLPAnalyzer()).parse(question);
        TopDocs result = searcher.search(query, 100);
        JSONArray resultArray = new JSONArray();
        for (ScoreDoc doc : result.scoreDocs) {
            Document document = searcher.doc(doc.doc);
            String question2 = document.get("questions");
            List<Term> seg_question2 = segment.seg(question2);
            double score = similarAnalyze.sentenceSimilarity(seg_question, seg_question2);
            resultArray.add(new QAAnalyzeResult(score, document.get("key"), question2));
        }
        return resultArray;
    }

    /**
     * 初始化时创建索引
     */
    @RequestMapping("/createIndex")
    public CheckResult createIndex() {
        questionsIndex.createIndex();
        return new CheckResult("000", "success", "");
    }

    /**
     * 重新加载全简称词典
     */
    @RequestMapping("/reloadAbbreviation")
    public CheckResult reloadAbbreviation() {
        myCustomDictionary.initAbbreviation();
        return new CheckResult("000", "success", "");
    }

    /**
     * 相似性分析核心计算类
     */
    public SimilarityAnalyze getSimilarAnalyze() {
        return similarAnalyze;
    }

    /**
     * 获取统一的分词对象
     */
    public Segment getSegment() {
        return segment;
    }

}
