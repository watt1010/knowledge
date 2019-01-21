package com.watt.core.nlp.cosinesimlarity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Word2Vec {

    private static final int MAX_SIZE = 50;
    private HashMap<String, float[]> wordMap = new HashMap<>();
    private int words;
    private int size;

    private static float readFloat(InputStream is) throws IOException {
        byte[] bytes = new byte[4];
        is.read(bytes);
        return getFloat(bytes);
    }

    /**
     * 读取一个float
     */
    private static float getFloat(byte[] b) {
        int accum = 0;
        accum = accum | (b[0] & 0xff) << 0;
        accum = accum | (b[1] & 0xff) << 8;
        accum = accum | (b[2] & 0xff) << 16;
        accum = accum | (b[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }

    /**
     * 读取一个字符串
     */
    private static String readString(DataInputStream dis) throws IOException {
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
        String s = new String(bytes, 0, i + 1, StandardCharsets.UTF_8);
        sb.append(s);
        return sb.toString();
    }

    /**
     * 加载模型
     *
     * @param path 模型的路径
     */
    void loadGoogleModel(String path) throws IOException {
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
    void loadCommonModel(String path) throws IOException {
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
                } else {
                    value[i - 1] = Float.parseFloat(split[i]);
                }
            }
            wordMap.put(key, value);
        }
    }

    void loadJavaModel(String path) throws IOException {
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

    public HashMap<String, float[]> getWordMap() {
        return wordMap;
    }

    public int getSize() {
        return size;
    }

}
