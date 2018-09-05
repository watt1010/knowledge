import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.nio.file.FileSystems;

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
    @Test
    public void test_index() throws Exception {
        //创建目录 directory
        //Directory directory = new RAMDirectory();
        Directory directory = FSDirectory.open(FileSystems.getDefault().getPath("d:/lucene/"));
        //创建indexWriter 将索引写入
        IndexWriterConfig iwc = new IndexWriterConfig();
        IndexWriter writer = new IndexWriter(directory, iwc);
        //创建document的对象

        File f = new File("d:/lucene/sample");
        for (File file : f.listFiles()) {
            System.out.println(file.getName());
            Document doc = new Document();
            //doc.add(new Field("content" , "你好", TextField.TYPE_STORED));
            doc.add(new Field("contents", new FileReader(file), TextField.TYPE_NOT_STORED));
            doc.add(new Field("filename", file.getName(), TextField.TYPE_STORED));
            doc.add(new Field("fullpath", file.getCanonicalPath(), TextField.TYPE_STORED));
            writer.addDocument(doc);
        }
        //创建document 添加field field是document的子元素
        writer.close();
        //将index写入
        directory.close();
    }

    @Test
    public void test_search() throws Exception {
        //创建directory对象
        Directory directory = FSDirectory.open(FileSystems.getDefault().getPath("d:/lucene/"));
        //创建indexReader
        DirectoryReader reader = DirectoryReader.open(directory);
        //根据IndexReader对象创建IndexSearcher
        IndexSearcher searcher = new IndexSearcher(reader);
        //创建搜索的query
        QueryParser parser = new QueryParser("contents", new StandardAnalyzer());
        //查询包含com文本的数据
        Query query = parser.parse("我问了福利卡好地方");
        TopDocs result = searcher.search(query, 10);
        System.out.println(result.scoreDocs.length);
        for (ScoreDoc doc : result.scoreDocs) {
            Document document = searcher.doc(doc.doc);
            System.out.println(document.getField("contents"));
        }
        reader.close();
        directory.close();
    }

    public void test_index2() {
        new Test2().test_index();
    }


}
