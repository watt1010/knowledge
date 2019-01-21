package com.watt.core.nlp.cosinesimlarity;

import java.util.HashMap;
import java.util.Map;

public class AtomSegment {
    public AtomSegment() {
    }

    public static String atomSegment(String sentence) {
        String atomSegResult = "";
        Map<Integer, String> wsWordMap = IDExtract.getLetters(sentence);
        Map<Integer, String> mWordMap = IDExtract.getNumbers(sentence);
        Map<Integer, String> wordsMap = new HashMap();
        wordsMap.putAll(wsWordMap);
        wordsMap.putAll(mWordMap);
        int senLength = sentence.length();

        for(int i = 0; i < senLength; ++i) {
            String word_i = (String)wordsMap.get(i);
            if (word_i == null) {
                word_i = sentence.charAt(i) + "";
                wordsMap.put(i, word_i);
            } else {
                i += word_i.length() - 1;
            }

            atomSegResult = atomSegResult + " " + word_i;
        }

        return atomSegResult;
    }

    public static void main(String[] args) {
        String sentence = "呼吸道感染email出生于2017年7月28号。";
        String sentence2 = "1.234呼吸道感染email:inspur@inspur.com出生于2017年7月28号。";
        String sentence3 = "email地址";
        String result = atomSegment(sentence3);
        System.out.println(result);
    }
}
