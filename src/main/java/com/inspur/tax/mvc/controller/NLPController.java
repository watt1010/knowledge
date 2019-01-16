package com.inspur.tax.mvc.controller;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.inspur.tax.mvc.beans.NLPResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
@RestController
public class NLPController {

    /**
     * HanLP命名实体识别、词性标注
     * @param request
     * @return
     */
    @RequestMapping("/nlp_ner")
    public NLPResult NLP_NER(HttpServletRequest request){
        String sentence = request.getParameter("sentence");
        Segment segment = HanLP.newSegment().enableOrganizationRecognize(true);
        List<Term> termList = segment.seg(sentence);
        List list = new ArrayList();
        for (int i = 0;i < termList.size();i++){
            Term term = termList.get(i);
            list.add(term.toString());
        }
        System.out.println(list);
        return new NLPResult(list);
    }

    /**
     * HanLP繁体转简体
     * @param request
     * @return
     */
    @RequestMapping("/nlp_c2s")
    public  NLPResult NLP_C2S(HttpServletRequest request){
        List list = new ArrayList();
        String sentence = request.getParameter("sentence");
        list.add(HanLP.convertToSimplifiedChinese(sentence));
        return new NLPResult(list);
    }

    /**
     * HanLP简体转繁体
     * @param request
     * @return
     */
    @RequestMapping("/nlp_s2c")
    public NLPResult NLP_S2C(HttpServletRequest request){
        List list = new ArrayList();
        String sentence = request.getParameter("sentence");
        list.add(HanLP.convertToTraditionalChinese(sentence));
        return new NLPResult(list);
    }

    /**
     * HanLP文字转拼音（标注声调）
     * @param request
     * @return
     */
    @RequestMapping("/nlp_pinyin_mark")
    public NLPResult pinyin(HttpServletRequest request){
        String sentence = request.getParameter("sentence");
        List<Pinyin> pinyinList = HanLP.convertToPinyinList(sentence);
        List list = new ArrayList();
        for (Pinyin pinyin : pinyinList)
        {
            list.add(pinyin.getPinyinWithToneMark());
        }
        return new NLPResult(list);
    }

    /**
     * HanLP文字转拼音（不标注声调）
     * @param request
     * @return
     */
    @RequestMapping("/nlp_pinyin_nomark")
    public NLPResult pinyin_nomark(HttpServletRequest request){
        String sentence = request.getParameter("sentence");
        List<Pinyin> pinyinList = HanLP.convertToPinyinList(sentence);
        List list = new ArrayList();
        for (Pinyin pinyin : pinyinList)
        {
            list.add(pinyin.getPinyinWithoutTone());
        }
        return new NLPResult(list);
    }


    /**
     * HanLP关键词提取
     * @param request
     * @return
     */
    @RequestMapping("/nlp_ekw")
    public NLPResult extractKeyword(HttpServletRequest request){
        String sentence = request.getParameter("sentence");
        int num = Integer.parseInt(request.getParameter("num"));
        List<String> keywordList = HanLP.extractKeyword(sentence, num);
        System.out.println(keywordList);
        return new NLPResult(keywordList);
    }
}
