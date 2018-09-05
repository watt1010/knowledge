import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.FileSystems;

public class Test2 {
    private String[] ids = {"1", "2", "3", "4", "5", "6",};
    private String[] emails = {"1", "2", "3", "4", "5", "6",};
    private String[] contens = {"nihao", "java", "hhh", "ggg", "ggg3", "4452",};
    private int[] atatachs = {1, 2, 3, 4, 3, 5};
    private String[] names = {"zhangsan", "李四", "watt", "hugo", "peanut", "faker"};
    private Directory directory = null;

    public Test2() {
        try {
            directory = FSDirectory.open(FileSystems.getDefault().getPath("d:/lucene"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test_index() {
        IndexWriter writer = null;
        IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
        Document document = new Document();
        for (String id : ids) {
            document.add(new Field("id", id, StringField.TYPE_STORED));
        }
        try {
            writer = new IndexWriter(directory, iwc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
