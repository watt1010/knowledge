package com.inspur.tax.core.dictionary;

import com.hankcs.hanlp.seg.common.Term;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CoreStopWordsDictionary {
    private static Set<String> stopWords = new HashSet<>();

    public static void addStopWord(String word) {
        stopWords.add(word);
    }

    private static boolean contains(String word) {
        return stopWords.contains(word);
    }

    /**
     * 去掉所有的停用词
     *
     * @return 去掉停用词后保留原始数据
     */
    public static List<Term> removeStopWords(List<Term> terms) {
        List<Term> result = new ArrayList<>();
        terms.forEach(term -> {
            if (!contains(term.word)) {
                result.add(term);
            }
        });
        return result;
    }

}
