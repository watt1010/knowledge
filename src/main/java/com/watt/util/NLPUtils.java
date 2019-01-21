package com.watt.util;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class NLPUtils {
    /**
     * 训练语料文件预处理
     * 1.分词
     * 2.繁转简
     * 3.去停用词
     *
     * @param file   目标文件
     * @param target 存储到的目标文件
     */
    public static void textPreprocessing(File file, String target) throws IOException {
        BufferedReader reader = FileUtils.getFileReader(file);
        BufferedWriter writer = FileUtils.getBufferedWriter(target);
        String wordLine = null;
        while ((wordLine = reader.readLine()) != null) {
            wordLine = HanLP.tw2s(wordLine);
            List<Term> termList = NotionalTokenizer.segment(wordLine);
            String line = convertTermtoString(termList);
            writer.newLine();
            writer.write(line);
        }
        writer.flush();
        reader.close();
        writer.close();
    }

    /**
     * 将分词数据拼接成字符串
     */
    public static String convertTermtoString(List<Term> termList, String segChar) {
        StringBuffer buffer = new StringBuffer();
        termList.forEach(term -> {
            buffer.append(term.word).append(segChar);
        });
        return buffer.toString().trim();
    }

    /**
     * 将分词数据拼接成字符串
     */
    public static String convertTermtoString(List<Term> termList) {
        return convertTermtoString(termList," ");
    }

}
