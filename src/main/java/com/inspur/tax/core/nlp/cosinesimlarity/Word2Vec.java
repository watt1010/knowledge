package com.inspur.tax.core.nlp.cosinesimlarity;

import com.inspur.tax.core.nlp.cosinesimlarity.entry.WordEntry;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Word2Vec {

    private HashMap<String, float[]> wordMap = new HashMap<>();

    private int words;
    private int size;
    private int topNSize = 40;

    /**
     * 加载模型
     *
     * @param path 模型的路径
     */
    public void loadGoogleModel(String path) throws IOException {
        DataInputStream dis = null;
        BufferedInputStream bis = null;
        double len = 0;
        float vector = 0;
        bis = new BufferedInputStream(new FileInputStream(path));
        dis = new DataInputStream(bis);
        // //读取词数
        words = Integer.parseInt(readString(dis));
        // //大小
        size = Integer.parseInt(readString(dis));
        String word;
        float[] vectors = null;
        for (int i = 0; i < words; i++) {
            word = readString(dis);
            vectors = new float[size];
            len = 0;
            for (int j = 0; j < size; j++) {
                vector = readFloat(dis);
                len += vector * vector;
                vectors[j] = (float) vector;
            }
            len = Math.sqrt(len);

            for (int j = 0; j < size; j++) {
                vectors[j] /= len;
            }

            wordMap.put(word, vectors);
            dis.read();
        }
        bis.close();
        dis.close();
    }

    /**
     * 加载模型
     *
     * @param path 模型的路径
     * @throws IOException 文件找不到时会抛出异常
     */
    public void loadCommonModel(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(path))));
        String wordLine = null;
        //将第一行的文本省略掉，第一行分别是词数量和纬度，是不需要记录到加载内容中的
        reader.readLine();
        while ((wordLine = reader.readLine()) != null) {
            String[] split = wordLine.trim().split("\\s+");
            String key = "";
            float[] value = new float[split.length - 1];
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    key = split[0];
                    //System.out.println("key:" + key);
                } else {
                    value[i - 1] = Float.parseFloat(split[i]);
                }
            }
            wordMap.put(key, value);
        }
    }

    public void loadJavaModel(String path) throws IOException {
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
            words = dis.readInt();
            size = dis.readInt();

            float vector = 0;

            String key = null;
            float[] value = null;
            for (int i = 0; i < words; i++) {
                double len = 0;
                key = dis.readUTF();
                value = new float[size];
                for (int j = 0; j < size; j++) {
                    vector = dis.readFloat();
                    len += vector * vector;
                    value[j] = vector;
                }

                len = Math.sqrt(len);

                for (int j = 0; j < size; j++) {
                    value[j] /= len;
                }
                wordMap.put(key, value);
            }

        }
    }

    private static final int MAX_SIZE = 50;

    /**
     * 近义词
     */
    public TreeSet<WordEntry> analogy(String word0, String word1, String word2) {
        float[] wv0 = getWordVector(word0);
        float[] wv1 = getWordVector(word1);
        float[] wv2 = getWordVector(word2);

        if (wv1 == null || wv2 == null || wv0 == null) {
            return null;
        }
        float[] wordVector = new float[size];
        for (int i = 0; i < size; i++) {
            wordVector[i] = wv1[i] - wv0[i] + wv2[i];
        }
        float[] tempVector;
        String name;
        List<WordEntry> wordEntrys = new ArrayList<WordEntry>(topNSize);
        for (Entry<String, float[]> entry : wordMap.entrySet()) {
            name = entry.getKey();
            if (name.equals(word0) || name.equals(word1) || name.equals(word2)) {
                continue;
            }
            float dist = 0;
            tempVector = entry.getValue();
            for (int i = 0; i < wordVector.length; i++) {
                dist += wordVector[i] * tempVector[i];
            }
            insertTopN(name, dist, wordEntrys);
        }
        return new TreeSet<WordEntry>(wordEntrys);
    }

    private void insertTopN(String name, float score, List<WordEntry> wordsEntrys) {
        // TODO Auto-generated method stub
        if (wordsEntrys.size() < topNSize) {
            wordsEntrys.add(new WordEntry(name, score));
            return;
        }
        float min = Float.MAX_VALUE;
        int minOffe = 0;
        for (int i = 0; i < topNSize; i++) {
            WordEntry wordEntry = wordsEntrys.get(i);
            if (min > wordEntry.score) {
                min = wordEntry.score;
                minOffe = i;
            }
        }

        if (score > min) {
            wordsEntrys.set(minOffe, new WordEntry(name, score));
        }

    }

    public Set<WordEntry> distance(String queryWord) {

        float[] center = wordMap.get(queryWord);
        if (center == null) {
            return Collections.emptySet();
        }

        int resultSize = wordMap.size() < topNSize ? wordMap.size() : topNSize;
        TreeSet<WordEntry> result = new TreeSet<WordEntry>();

        double min = Float.MIN_VALUE;
        for (Entry<String, float[]> entry : wordMap.entrySet()) {
            float[] vector = entry.getValue();
            float dist = 0;
            for (int i = 0; i < vector.length; i++) {
                dist += center[i] * vector[i];
            }

            if (dist > min) {
                result.add(new WordEntry(entry.getKey(), dist));
                if (resultSize < result.size()) {
                    result.pollLast();
                }
                min = result.last().score;
            }
        }
        result.pollFirst();

        return result;
    }

    public Set<WordEntry> distance(List<String> words) {

        float[] center = null;
        for (String word : words) {
            center = sum(center, wordMap.get(word));
        }

        if (center == null) {
            return Collections.emptySet();
        }

        int resultSize = wordMap.size() < topNSize ? wordMap.size() : topNSize;
        TreeSet<WordEntry> result = new TreeSet<WordEntry>();

        double min = Float.MIN_VALUE;
        for (Entry<String, float[]> entry : wordMap.entrySet()) {
            float[] vector = entry.getValue();
            float dist = 0;
            for (int i = 0; i < vector.length; i++) {
                dist += center[i] * vector[i];
            }

            if (dist > min) {
                result.add(new WordEntry(entry.getKey(), dist));
                if (resultSize < result.size()) {
                    result.pollLast();
                }
                min = result.last().score;
            }
        }
        result.pollFirst();

        return result;
    }

    private float[] sum(float[] center, float[] fs) {

        if (center == null && fs == null) {
            return null;
        }

        if (fs == null) {
            return center;
        }

        if (center == null) {
            return fs;
        }

        for (int i = 0; i < fs.length; i++) {
            center[i] += fs[i];
        }

        return center;
    }

    /**
     * 得到词向量
     */
    public float[] getWordVector(String word) {
        return wordMap.get(word);
    }

    /**
     * 设置词向量
     */
    public void setWordVector(String word, float[] value) {
        wordMap.put(word, value);
    }

    public static float readFloat(InputStream is) throws IOException {
        byte[] bytes = new byte[4];
        is.read(bytes);
        return getFloat(bytes);
    }

    /**
     * 读取一个float
     *
     * @param b
     * @return
     */
    public static float getFloat(byte[] b) {
        int accum = 0;
        accum = accum | (b[0] & 0xff) << 0;
        accum = accum | (b[1] & 0xff) << 8;
        accum = accum | (b[2] & 0xff) << 16;
        accum = accum | (b[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }

    /**
     * 读取一个字符串
     *
     * @param dis
     * @return
     * @throws IOException
     */
    private static String readString(DataInputStream dis) throws IOException {
        // TODO Auto-generated method stub
        byte[] bytes = new byte[MAX_SIZE];
        byte b = dis.readByte();
        int i = -1;
        StringBuilder sb = new StringBuilder();
        while (b != 32 && b != 10) {
            i++;
            bytes[i] = b;
            b = dis.readByte();
            if (i == 49) {
                sb.append(new String(bytes));
                i = -1;
                bytes = new byte[MAX_SIZE];
            }
        }
        String s = new String(bytes, 0, i + 1, "UTF-8");
        //System.out.println(s);
        sb.append(s);
        return sb.toString();
    }

    public int getTopNSize() {
        return topNSize;
    }

    public void setTopNSize(int topNSize) {
        this.topNSize = topNSize;
    }

    public HashMap<String, float[]> getWordMap() {
        return wordMap;
    }

    public int getWords() {
        return words;
    }

    public int getSize() {
        return size;
    }

}
