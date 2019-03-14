import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.corpus.document.sentence.word.IWord;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.watt.util.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class wikiCorpus {

    /**
     * 文档总数：1020652
     *
     * @throws Exception error stop
     */
    @Test
    public void generateKeySet() throws Exception {
        BufferedReader reader = FileUtils.getFileReader(new File("D:\\corpus\\data\\zh_wiki_00"), Charset.forName("UTF-8"));
        MapCount<String> mapCount = new MapCount<>();
        StringBuilder wordLine = new StringBuilder();
        String temp = null;
        int count = 1;
        while ((temp = reader.readLine()) != null) {
            wordLine.append(temp);
            if (wordLine.indexOf("<doc") > -1 && wordLine.indexOf("</doc>") > -1) {
                int start = wordLine.indexOf("<doc");
                int end = wordLine.indexOf("</doc>") + 6;
                String s = wordLine.substring(start, end);
                wordLine.delete(start, end);

                Document doc = Jsoup.parse(s);
                Sentence sentence = NLPTokenizer.ANALYZER.analyze(HanLP.tw2s(doc.select("doc").text()));
                for (IWord iWord : Objects.requireNonNull(sentence).wordList) {//                    keySet.add(iWord.getValue());
                    int[] value = Optional.ofNullable(mapCount.get().get(iWord.getValue())).orElse(new int[3]);
                    if (value[2] != count) {
                        value[2] = count; //重置文档计数
                        value[1] = value[1] + 1; //文档数 +1
                        value[0] = value[0] + 1; //总词频数 +1
                    } else {
                        value[0] = value[0] + 1;
                    }
                    mapCount.get().put(iWord.getValue(), value);
                }
                System.out.println("count:" + count++);
//                if (count % 500 == 0) {
//                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("D:\\corpus\\data\\keyset\\" + UUID.randomUUID().toString())));
//                    out.writeObject(mapCount);
//                    out.flush();
//                    out.close();
//                }
            }

        }
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("D:\\corpus\\data\\keySetNLP")));
        out.writeObject(mapCount);
        out.flush();
        out.close();
        System.out.println("count:" + count);
        reader.close();
    }

    @Test
    public void generateKeySet2() throws Exception {
        BufferedReader reader = FileUtils.getFileReader(new File("D:\\corpus\\data\\zh_wiki_00"), Charset.forName("UTF-8"));
        MapCount<String> mapCount = new MapCount<>();
        StringBuilder wordLine = new StringBuilder();
        String temp = null;
        int count = 1;
        while ((temp = reader.readLine()) != null) {
            wordLine.append(temp);
            if (wordLine.indexOf("<doc") > -1 && wordLine.indexOf("</doc>") > -1) {
                int start = wordLine.indexOf("<doc");
                int end = wordLine.indexOf("</doc>") + 6;
                String s = wordLine.substring(start, end);
                wordLine.delete(start, end);

                Document doc = Jsoup.parse(s);
                List<Term> sentence = HanLP.segment(HanLP.tw2s(doc.select("doc").text()));
                for (Term iWord : Objects.requireNonNull(sentence)) {//                    keySet.add(iWord.getValue());
                    int[] value = Optional.ofNullable(mapCount.get().get(iWord.word)).orElse(new int[3]);
                    if (value[2] != count) {
                        value[2] = count; //重置文档计数
                        value[1] = value[1] + 1; //文档数 +1
                        value[0] = value[0] + 1; //总词频数 +1
                    } else {
                        value[0] = value[0] + 1;
                    }
                    mapCount.get().put(iWord.word, value);
                }
                System.out.println("count:" + count++);
//                if (count % 500 == 0) {
//                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("D:\\corpus\\data\\keyset\\" + UUID.randomUUID().toString())));
//                    out.writeObject(mapCount);
//                    out.flush();
//                    out.close();
//                }
            }

        }
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("D:\\corpus\\data\\keySet1")));
        out.writeObject(mapCount);
        out.flush();
        out.close();
        System.out.println("count:" + count);
        reader.close();
    }

    @Test
    public void caltfidf() throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("D:\\corpus\\data\\keySet1")));
        MapCount<String> mapCount = (MapCount<String>) in.readObject();
        Map<String, Double> tfidf = new HashMap<String, Double>();
        for (Map.Entry<String, int[]> one : mapCount.get().entrySet()) {
            tfidf.put(one.getKey(), one.getValue()[0] * Math.log(1020652.0 / one.getValue()[2]));
        }
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("D:\\corpus\\data\\tfidf")));
        out.writeObject(tfidf);
        out.flush();
        out.close();
    }

}
