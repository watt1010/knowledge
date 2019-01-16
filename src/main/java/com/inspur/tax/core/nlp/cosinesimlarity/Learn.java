package com.inspur.tax.core.nlp.cosinesimlarity;

import com.inspur.tax.core.nlp.cosinesimlarity.datastructs.Haffman;
import com.inspur.tax.core.nlp.cosinesimlarity.datastructs.MapCount;
import com.inspur.tax.core.nlp.cosinesimlarity.entry.HiddenNeuron;
import com.inspur.tax.core.nlp.cosinesimlarity.entry.Neuron;
import com.inspur.tax.core.nlp.cosinesimlarity.entry.WordNeuron;
import com.inspur.tax.util.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Learn {

    private Map<String, Neuron> wordMap = new HashMap<>();
    /**
     * 训练多少个特征
     * 词向量的维度，默认为300
     */
    private int layerSize = SimilarityAnalyzeUnfamiliarWords.dimension;

    /**
     * 上下文窗口大小
     */
    private int window = 5;

    private double sample = 1e-3;
    private double alpha = 0.025;
    private double startingAlpha = alpha;

    private int EXP_TABLE_SIZE = 1000;
    private Boolean isCbow = false;
    private double[] expTable = new double[EXP_TABLE_SIZE];
    private int trainWordsCount = 0;
    private int MAX_EXP = 6;

    public Learn(Boolean isCbow, Integer layerSize, Integer window, Double alpha,
                 Double sample) {
        createExpTable();
        if (isCbow != null) {
            this.isCbow = isCbow;
        }
        if (layerSize != null)
            this.layerSize = layerSize;
        if (window != null)
            this.window = window;
        if (alpha != null)
            this.alpha = alpha;
        if (sample != null)
            this.sample = sample;
    }

    public Learn() {
        createExpTable();
    }

    public static void main(String[] args) throws IOException {
        Learn learn = new Learn();
        long start = System.currentTimeMillis();
        learn.learnFile(new File("/root/PycharmProjects/TrainWord/file/fenci_std_zh_wiki_00"));
        System.out.println("use time " + (System.currentTimeMillis() - start));
        learn.saveModel("data/javaVector");
    }

    /**
     * skip gram 模型训练
     *
     * @param sentence skip分词后的句子
     */
    private void skipGram(int index, List<WordNeuron> sentence, int b) {
        WordNeuron word = sentence.get(index);
        int a, c = 0;
        for (a = b; a < window * 2 + 1 - b; a++) {
            if (a == window) {
                continue;
            }
            c = index - window + a;
            if (c < 0 || c >= sentence.size()) {
                continue;
            }

            double[] neu1e = new double[layerSize];// 误差项
            // HIERARCHICAL SOFTMAX
            List<Neuron> neurons = word.neurons;
            WordNeuron we = sentence.get(c);
            for (int i = 0; i < neurons.size(); i++) {
                HiddenNeuron out = (HiddenNeuron) neurons.get(i);
                double f = 0;
                // Propagate hidden -> output
                for (int j = 0; j < layerSize; j++) {
                    f += we.syn0[j] * out.syn1[j];
                }
                if (f <= -MAX_EXP || f >= MAX_EXP) {
                    continue;
                } else {
                    f = (f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2);
                    f = expTable[(int) f];
                }
                // 'g' is the gradient multiplied by the learning rate
                double g = (1 - word.codeArr[i] - f) * alpha;
                // Propagate errors output -> hidden
                for (c = 0; c < layerSize; c++) {
                    neu1e[c] += g * out.syn1[c];
                }
                // Learn weights hidden -> output
                for (c = 0; c < layerSize; c++) {
                    out.syn1[c] += g * we.syn0[c];
                }
            }

            // Learn weights input -> hidden
            for (int j = 0; j < layerSize; j++) {
                we.syn0[j] += neu1e[j];
            }
        }

    }

    /**
     * 词袋模型
     *
     * @param index
     * @param sentence
     * @param b
     */
    private void cbowGram(int index, List<WordNeuron> sentence, int b) {
        WordNeuron word = sentence.get(index);
        int a, c = 0;

        List<Neuron> neurons = word.neurons;
        double[] neu1e = new double[layerSize];// 误差项
        double[] neu1 = new double[layerSize];// 误差项
        WordNeuron last_word;

        for (a = b; a < window * 2 + 1 - b; a++)
            if (a != window) {
                c = index - window + a;
                if (c < 0)
                    continue;
                if (c >= sentence.size())
                    continue;
                last_word = sentence.get(c);
                if (last_word == null)
                    continue;
                for (c = 0; c < layerSize; c++)
                    neu1[c] += last_word.syn0[c];
            }

        // HIERARCHICAL SOFTMAX
        for (int d = 0; d < neurons.size(); d++) {
            HiddenNeuron out = (HiddenNeuron) neurons.get(d);
            double f = 0;
            // Propagate hidden -> output
            for (c = 0; c < layerSize; c++)
                f += neu1[c] * out.syn1[c];
            if (f <= -MAX_EXP)
                continue;
            else if (f >= MAX_EXP)
                continue;
            else
                f = expTable[(int) ((f + MAX_EXP) * (EXP_TABLE_SIZE / MAX_EXP / 2))];
            // 'g' is the gradient multiplied by the learning rate
            // double g = (1 - word.codeArr[d] - f) * alpha;
            // double g = f*(1-f)*( word.codeArr[i] - f) * alpha;
            double g = f * (1 - f) * (word.codeArr[d] - f) * alpha;
            //
            for (c = 0; c < layerSize; c++) {
                neu1e[c] += g * out.syn1[c];
            }
            // Learn weights hidden -> output
            for (c = 0; c < layerSize; c++) {
                out.syn1[c] += g * neu1[c];
            }
        }
        for (a = b; a < window * 2 + 1 - b; a++) {
            if (a != window) {
                c = index - window + a;
                if (c < 0)
                    continue;
                if (c >= sentence.size())
                    continue;
                last_word = sentence.get(c);
                if (last_word == null)
                    continue;
                for (c = 0; c < layerSize; c++)
                    last_word.syn0[c] += neu1e[c];
            }

        }
    }

    /**
     * trainModel
     *
     * @throws IOException 文件读取异常
     */
    private void trainModel(File file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String temp = null;
        long nextRandom = 5;
        int wordCount = 0;
        int lastWordCount = 0;
        int wordCountActual = 0;
        while ((temp = br.readLine()) != null) {
            if (wordCount - lastWordCount > 10000) {
                System.out.println("alpha:" + alpha + "\tProgress: " + (int) (wordCountActual / (double) (trainWordsCount + 1) * 100) + "%");
                wordCountActual += wordCount - lastWordCount;
                lastWordCount = wordCount;
                alpha = startingAlpha * (1 - wordCountActual / (double) (trainWordsCount + 1));
                if (alpha < startingAlpha * 0.0001) {
                    alpha = startingAlpha * 0.0001;
                }
            }
            String[] strs = temp.split("\\s+");
            wordCount += strs.length;
            List<WordNeuron> sentence = new ArrayList<>();
            for (String str : strs) {
                Neuron entry = wordMap.get(str);
                if (entry == null) {
                    continue;
                }
                if (sample > 0) {
                    double ran = (Math.sqrt(entry.freq / (sample * trainWordsCount)) + 1) * (sample * trainWordsCount) / entry.freq;
                    nextRandom = nextRandom * 25214903917L + 11;
                    if (ran < (nextRandom & 0xFFFF) / (double) 65536) {
                        continue;
                    }
                }
                sentence.add((WordNeuron) entry);
            }

            for (int index = 0; index < sentence.size(); index++) {
                nextRandom = nextRandom * 25214903917L + 11;
                if (isCbow) {
                    cbowGram(index, sentence, (int) nextRandom % window);
                } else {
                    skipGram(index, sentence, (int) nextRandom % window);
                }
            }

        }
        System.out.println("Vocab size: " + wordMap.size());
        System.out.println("Words in train file: " + trainWordsCount);
        System.out.println("sucess train over!");

    }

    /**
     * 对文本进行预分类
     *
     * @param files
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void readVocabWithSupervised(File[] files) throws IOException {
        for (int category = 0; category < files.length; category++) {
            // 对多个文件学习
            MapCount<String> mc = new MapCount<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(files[category])));
            String temp = null;
            while ((temp = br.readLine()) != null) {
                String[] split = temp.split("\\s+");
                trainWordsCount += split.length;
                for (String string : split) {
                    mc.add(string);
                }
            }

            for (Entry<String, Integer> element : mc.get().entrySet()) {
                double tarFreq = (double) element.getValue() / mc.size();
                if (wordMap.get(element.getKey()) != null) {
                    double srcFreq = wordMap.get(element.getKey()).freq;
                    if (srcFreq >= tarFreq) {
                        continue;
                    } else {
                        Neuron wordNeuron = wordMap.get(element.getKey());
                        wordNeuron.category = category;
                        wordNeuron.freq = tarFreq;
                    }
                } else {
                    wordMap.put(element.getKey(), new WordNeuron(element.getKey(),
                            tarFreq, category, layerSize));
                }
            }
        }
    }

    /**
     * Precompute the exp() table f(x) = x / (x + 1)
     */
    private void createExpTable() {
        for (int i = 0; i < EXP_TABLE_SIZE; i++) {
            expTable[i] = Math.exp(((i / (double) EXP_TABLE_SIZE * 2 - 1) * MAX_EXP));
            expTable[i] = expTable[i] / (expTable[i] + 1);
        }
    }

    /**
     * 根据文件学习
     *
     * @param file
     * @throws IOException
     */
    public void learnFile(File file) throws IOException {
        System.out.println("readVocab start");
        readVocab(file);
        System.out.println("new huffman tree start");
        new Haffman(layerSize).make(wordMap.values());
        System.out.println("查找每个神经元");
        // 查找每个神经元
        for (Neuron neuron : wordMap.values()) {
            ((WordNeuron) neuron).makeNeurons();
        }
        System.out.println("trainModel start");
        trainModel(file);
    }

    /**
     * 根据预分类的文件学习
     *
     * @param summaryFile     合并文件
     * @param classifiedFiles 分类文件
     * @throws IOException
     */
    public void learnFile(File summaryFile, File[] classifiedFiles)
            throws IOException {
        readVocabWithSupervised(classifiedFiles);
        new Haffman(layerSize).make(wordMap.values());
        // 查找每个神经元
        for (Neuron neuron : wordMap.values()) {
            ((WordNeuron) neuron).makeNeurons();
        }
        trainModel(summaryFile);
    }

    /**
     * 保存模型
     */
    public void saveModel(File file) {
        // TODO Auto-generated method stub

        try (DataOutputStream dataOutputStream = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {
            dataOutputStream.writeInt(wordMap.size());
            dataOutputStream.writeInt(layerSize);
            double[] syn0 = null;
            for (Entry<String, Neuron> element : wordMap.entrySet()) {
                dataOutputStream.writeUTF(element.getKey());
                syn0 = ((WordNeuron) element.getValue()).syn0;
                for (double d : syn0) {
                    dataOutputStream.writeFloat(((Double) d).floatValue());
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveModel(String file) throws IOException {
        BufferedWriter writer = FileUtils.getBufferedWriter(file);
        writer.write(wordMap.size() + " " + layerSize);
        for (Entry<String, Neuron> element : wordMap.entrySet()) {
            writer.newLine();
            writer.write(element.getKey() + " ");
            double[] syn0 = ((WordNeuron) element.getValue()).syn0;
            StringBuffer stringBuffer = new StringBuffer();
            for (double vectorValues : syn0) {
                stringBuffer.append((float) vectorValues + " ");
            }
            writer.write(stringBuffer.toString().trim());
        }
        writer.flush();
        writer.close();
    }

    public int getLayerSize() {
        return layerSize;
    }

    public void setLayerSize(int layerSize) {
        this.layerSize = layerSize;
    }

    public int getWindow() {
        return window;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public double getSample() {
        return sample;
    }

    public void setSample(double sample) {
        this.sample = sample;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
        this.startingAlpha = alpha;
    }

    public Boolean getIsCbow() {
        return isCbow;
    }

    public void setIsCbow(Boolean isCbow) {
        this.isCbow = isCbow;
    }

    /**
     * 统计词频
     *
     * @param file
     * @throws IOException
     */
    private void readVocab(File file) throws IOException {
        MapCount<String> mapCount = new MapCount<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String temp;
        while ((temp = br.readLine()) != null) {
            String[] split = temp.split("\\s+");
            trainWordsCount += split.length;
            for (String string : split) {
                mapCount.add(string);
            }
        }
        for (Entry<String, Integer> element : mapCount.get().entrySet()) {
            wordMap.put(element.getKey(),
                    new WordNeuron(element.getKey(), (double) element.getValue() / mapCount.size(), layerSize));
        }
    }
}
