package com.watt.core.dictionary;

import com.hankcs.hanlp.seg.common.Term;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全、简称词典核心类
 */
public class CoreAbbreviationDictionary {
    private static Map<String, String> abbreviation = new HashMap<>();

    public static String getAbbreviation(String abbr) {
        return abbreviation.get(abbr);
    }

    public static void addAbbreviation(String abbr, String full) {
        abbreviation.put(abbr, full);
    }

    /**
     * 将简称全部转为全称
     */
    public static List<Term> convertAbbreviationToFull(List<Term> terms) {
        terms.forEach(term -> {
            String full = getAbbreviation(term.word);
            if (full != null) {
                term.word = full;
            }
        });
        return terms;
    }
}
