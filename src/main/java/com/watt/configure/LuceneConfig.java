package com.watt.configure;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.FileSystems;

@Configuration
@ConfigurationProperties(prefix = "lucene")
public class LuceneConfig {
    private static Directory directory;
    private String root;
    private static DirectoryReader reader;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String indexKey;
    private String vectorPath;

    private Directory getDirectory() {
            try {
                directory = FSDirectory.open(FileSystems.getDefault().getPath(root));
            } catch (IOException e) {
                logger.error("directory对象打开失败");
                return null;
            }
        return directory;
    }

    public IndexSearcher getIndexSearcher() {
        Directory directory = getDirectory();
        if (directory == null) {
            return null;
        }
        try {
                reader = DirectoryReader.open(directory);
                DirectoryReader tr = DirectoryReader.openIfChanged(reader);
                if (tr != null) {
                    reader.close();
                    reader = tr;
                }

            return new IndexSearcher(reader);
        } catch (IOException e) {
            logger.error("indexReader打开失败，不能继续");
        }
        return null;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getIndexKey() {
        return indexKey;
    }

    public void setIndexKey(String indexKey) {
        this.indexKey = indexKey;
    }

    public String getVectorPath() {
        return vectorPath;
    }

    public void setVectorPath(String vectorPath) {
        this.vectorPath = vectorPath;
    }
}
