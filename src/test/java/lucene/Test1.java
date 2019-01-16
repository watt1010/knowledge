package lucene;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;
import com.hankcs.hanlp.dictionary.common.CommonSynonymDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;
import com.hankcs.lucene.HanLPAnalyzer;
import com.inspur.tax.core.nlp.cosinesimlarity.SimilarityAnalyze;
import com.inspur.tax.core.nlp.cosinesimlarity.SimilarityAnalyzeUnfamiliarWords;
import com.inspur.tax.util.FileUtils;
import com.inspur.tax.util.NLPUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.List;

public class Test1 {
    @Test
    public void test1() throws Exception {
        Analyzer analyzer = new StandardAnalyzer();

        // Store the index in memory:
        Directory directory = new RAMDirectory();
        // To store an index on disk, use this instead:
        //Directory directory = FSDirectory.open("/tmp/testindex");
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        Document doc = new Document();
        String text = "This is the text to be indexed.";
        doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
        iwriter.addDocument(doc);
        iwriter.close();

        // Now search the index:
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        // Parse a simple query that searches for "text":
        QueryParser parser = new QueryParser("fieldname", analyzer);
        Query query = parser.parse("text");
        TopDocs result = isearcher.search(query, 10);
        //TopDocs hits = isearcher.search(query, 0, new Sort()).scoreDocs;
        System.out.println(result.scoreDocs.length);
        // Iterate through the results:
        for (int i = 0; i < result.scoreDocs.length; i++) {
            //Document hitDoc = isearcher.doc(hits[i].doc);
            System.out.println(result.scoreDocs[i].score + "," + result.scoreDocs[i].doc);
        }
        ireader.close();
        directory.close();
    }

    /**
     * 建立索引
     */
//    @Test
//    public void test_index() throws Exception {
//        //创建目录 directory
//        //Directory directory = new RAMDirectory();
//        Directory directory = FSDirectory.open(FileSystems.getDefault().getPath("d:/lucene/"));
//        //创建indexWriter 将索引写入
//        IndexWriterConfig iwc = new IndexWriterConfig();
//        IndexWriter writer = new IndexWriter(directory, iwc);
//        //创建document的对象
//
//        File f = new File("d:/lucene/sample");
//        for (File file : f.listFiles()) {
//            System.out.println(file.getName());
//            Document doc = new Document();
//            //doc.add(new Field("content" , "你好", TextField.TYPE_STORED));
//            doc.add(new Field("contents", new FileReader(file), TextField.TYPE_NOT_STORED));
//            doc.add(new Field("filename", file.getName(), TextField.TYPE_STORED));
//            doc.add(new Field("fullpath", file.getCanonicalPath(), TextField.TYPE_STORED));
//            writer.addDocument(doc);
//        }
//        //创建document 添加field field是document的子元素
//        writer.close();
//        //将index写入
//        directory.close();
//    }
    @Test
    public void test_search() throws Exception {
        //创建directory对象
        Directory directory = FSDirectory.open(FileSystems.getDefault().getPath("d:/lucene/"));
        //创建indexReader
        DirectoryReader reader = DirectoryReader.open(directory);
        //根据IndexReader对象创建IndexSearcher
        IndexSearcher searcher = new IndexSearcher(reader);
        //创建搜索的query
        // 多条件必备神器
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        Query query = new QueryParser("questionWithSynonyms", new HanLPAnalyzer()).parse("发票代开企业所得税的税收优惠政策");
        Query query2 = new QueryParser("user_id", new KeywordAnalyzer()).parse("demo");
        builder.add(query, BooleanClause.Occur.FILTER);
        builder.add(query2, BooleanClause.Occur.FILTER);
        TopDocs result = searcher.search(builder.build(), 100);
        System.out.println(result.scoreDocs.length);
        for (ScoreDoc doc : result.scoreDocs) {
            Document document = searcher.doc(doc.doc);
            System.out.println(document.get("questions"));
            System.out.println(document.get("user_id"));
        }
        reader.close();
        directory.close();
    }
    @Test
    public void test_index2() throws Exception{
        BufferedReader bw = new BufferedReader(new InputStreamReader(new FileInputStream("d:/read.txt"), "GBK"));
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("d:/write.txt"), "GBK"));
        String line = null;
        while ((line = bw.readLine()) != null) {
            line = line+"');";
            br.newLine();
            br.write(line);
        }
        br.close();
        bw.close();
    }
//    @Test
//    public void testload(){
//        Segment segment = HanLP.newSegment().enableTranslatedNameRecognize(true)
//                .enablePlaceRecognize(true)
//                .enableNameRecognize(true)
//                .enableOrganizationRecognize(true);
//        SimilarityAnalyze similarityAnalyzeUnfamiliarWords = new SimilarityAnalyze();
//        similarityAnalyzeUnfamiliarWords.loadGoogleModel("D:\\ideaProject\\knowledge-core\\data\\vectors.txt");
//        String s1 = "增值税的税率";
//        String s2 = "增值税税率";
//        List<Term> st1 = NotionalTokenizer.segment(s1);
//        List<Term> st2 = NotionalTokenizer.segment(s2);
//        System.out.println(st1);
//        System.out.println(st2);
//        System.out.println(similarityAnalyzeUnfamiliarWords.sentenceSimilarity(st1, st2));
//    }

    @Test
    public void testIo() {
        List<File> files = FileUtils.listFiles("d:/corpus");
        files.forEach(file -> {
            System.out.println(file.getPath());
        });
    }

    @Test
    public void testSegWithoutDic() {
        String path = "d:/corpus";
        List<File> files = FileUtils.listFiles(path);
        files.forEach(file -> {
            try {
                NLPUtils.textPreprocessing(file, file.getPath() + ".seg.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
//    @Test
//    public void testSimiliar(){
//        Similarity similarity = new Similarity();
//        similarity.loadGoogleModel("D:\\ideaProject\\knowledge-core\\data\\wiki_chinese_word2vec(Google)_200d.bin");
//        System.out.println(similarity.wordSimilarity("父亲","母亲"));
//
//    }
    @Test
    public void testMysimilar(){
        SimilarityAnalyzeUnfamiliarWords similarityAnalyzeUnfamiliarWords = new SimilarityAnalyzeUnfamiliarWords();
        similarityAnalyzeUnfamiliarWords.loadGoogleModel("D:/ideaProject/knowledge-core/data/vectors.txt");
        //similarityAnalyzeUnfamiliarWords.loadCommonModel("D:\\pythonProject\\TrainWord\\model\\java300\\word2vec_wv_add_java_2018-10-09.model");
        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("父亲","母亲"));
        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("父亲","爸爸"));
        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("母亲","妈妈"));
        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("小微","企业"));
        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("增值税","所得税"));
        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("增值税","发票"));
        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("国税局","发票"));
        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("电脑","笔记本"));
    }
//    @Test
//    public void testSimilarInspur(){
//        SimilarityAnalyze similarityAnalyzeUnfamiliarWords = new SimilarityAnalyze();
//        similarityAnalyzeUnfamiliarWords.loadGoogleModel("D:\\ideaProject\\knowledge-core\\data\\wiki_chinese_word2vec(Google)_200d.bin");
//        //similarityAnalyzeUnfamiliarWords.loadCommonModel("D:\\pythonProject\\TrainWord\\model\\java300\\word2vec_wv_add_java_2018-10-09.model");
//        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("山东","山东省"));
////        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("父亲","爸爸"));
////        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("母亲","妈妈"));
////        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("小微","企业"));
////        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("增值税","所得税"));
////        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("增值税","发票"));
////        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("国税局","发票"));
////        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("电脑","笔记本"));
////        List<Term> seg_question = HanLP.segment("浪潮发展历程");
////        List<Term> seg_question2 = HanLP.segment("浪潮发展进程");
////        System.out.println(seg_question);
////        System.out.println(seg_question2);
////        System.out.println(similarityAnalyzeUnfamiliarWords.sentenceSimilarity(seg_question,seg_question2));
////        System.out.println(similarityAnalyzeUnfamiliarWords.getSimilarWords("父亲",10));
//    }
    @Test
    public void testSynonm(){
        CommonSynonymDictionary.SynonymItem item = CoreSynonymDictionary.get("申请");
        item.synonymList.forEach(synonym -> {
            System.out.println(synonym.realWord);
        });
    }
    @Test
    public void segTest(){
        System.out.println(NotionalTokenizer.segment("骑自行车，一把把把手把手住了啊"));
    }

}
