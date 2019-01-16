package com.inspur.tax.core;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.synonym.Synonym;
import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;
import com.hankcs.hanlp.dictionary.common.CommonSynonymDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.lucene.HanLPIndexAnalyzer;
import com.inspur.tax.configure.LuceneConfig;
import com.inspur.tax.mvc.service.QAService;
import com.inspur.tax.util.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * lucene索引创建保存相关
 */
@Component
public class QuestionsIndex {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final QAService qaService;
    private final LuceneConfig luceneConfig;
    private Segment segment = HanLP.newSegment();

    @Autowired
    public QuestionsIndex(QAService qaService, LuceneConfig luceneConfig) {
        this.qaService = qaService;
        this.luceneConfig = luceneConfig;
    }

    /**
     * 初始化,所以库
     */
    public void createIndex() {
        List list = qaService.queryUsers();
        for (int i = 0;i<list.size();i++) {
            FileUtils.clearPath(luceneConfig.getRoot()+"/"+list.get(i).toString());
        }
        for (int i = 0;i<list.size();i++) {
            IndexWriter writer;
            Directory directory;
            IndexWriterConfig iwc = new IndexWriterConfig(new HanLPIndexAnalyzer());
            //创建目录 directory
            try {
                directory = FSDirectory.open(FileSystems.getDefault().getPath(luceneConfig.getRoot()+"/"+list.get(i).toString()));
                writer = new IndexWriter(directory, iwc);
            } catch (IOException e) {
                logger.info("Lucene目录打开异常");
                return;
            }

            int start = 0;
            int pageSize = 1000;
            List<Map<String, String>> words;
            AtomicInteger count = new AtomicInteger();
            do {
                words = qaService.queryQuestions(start, pageSize,list.get(i).toString());
                for (Map<String, String> word : words) {
                    if (word != null && !word.isEmpty()) {
                        Document doc = new Document();//创建document 添加field field是document的子元素
                        //System.out.println(word.get("QUESTION"));
                        doc.add(new Field("questions", word.get("QUESTION"), TextField.TYPE_STORED));
                        doc.add(new Field(luceneConfig.getIndexKey(), addSynonymItems(word.get("QUESTION")), TextField.TYPE_STORED));
                        doc.add(new Field("key", word.get("KW_ID"), TextField.TYPE_STORED));
                        doc.add(new Field("user_id", word.get("USER_ID"), TextField.TYPE_STORED));
                        try {
                            writer.addDocument(doc);
                        } catch (IOException e) {
                            logger.info("写入所以出错QUESTION：" + word.get("QUESTION") + "\nID:" + word.get("KW_ID"));
                        }
                        count.getAndIncrement();
                    }
                }
                //logger.info("成功加载问题库：" + count.doubleValue());
                start += pageSize;
            } while (!words.isEmpty());
            try {
                writer.close();
                directory.close();
            } catch (IOException e) {
                logger.info("Lucene目录和writer关闭异常");
            }
        }

    }

    private String addSynonymItems(String word) {
        List<Term> termList = segment.seg(word);
        StringBuffer result = new StringBuffer();
        for (Term term : termList) {
            result.append(term.word);
            CommonSynonymDictionary.SynonymItem item = CoreSynonymDictionary.get(term.word);
            if (item != null && item.type == Synonym.Type.EQUAL && item.synonymList != null && !item.synonymList.isEmpty()) {
                for (Synonym synonym : item.synonymList) {
                    result.append(synonym.realWord);
                }
            }
        }
        return result.toString();
    }

}
