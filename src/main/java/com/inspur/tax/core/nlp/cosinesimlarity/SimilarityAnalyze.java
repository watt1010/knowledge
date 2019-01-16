//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.inspur.tax.core.nlp.cosinesimlarity;

import com.hankcs.hanlp.seg.common.Term;
import com.inspur.tax.core.nlp.cosinesimlarity.entry.WordEntry;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public abstract class SimilarityAnalyze {
    Word2Vec vec = new Word2Vec();
    boolean loadModel;

    public Word2Vec getVec() {
        return vec;
    }

    public void setVec(Word2Vec vec) {
        this.vec = vec;
    }

    public void loadGoogleModel(String filePath) {
        try {
            this.vec.loadGoogleModel(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.loadModel = true;
    }

    public void loadCommonModel(String filePath) {
        try {
            this.vec.loadCommonModel(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.loadModel = true;
    }

    public void loadJavaModel(String filePath) {
        try {
            this.vec.loadJavaModel(filePath);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
        this.loadModel = true;
    }

    public static void trainJavaModel(String trainFilePath, String modelFilePath) {
        Learn learn = new Learn();
        long start = System.currentTimeMillis();

        try {
            learn.learnFile(new File(trainFilePath));
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        System.out.println("use time " + (System.currentTimeMillis() - start));
        learn.saveModel(new File(modelFilePath));
    }

    float[] getWordVector(String word) {
        return !this.loadModel ? null : this.vec.getWordVector(word);
    }

    float calDist(float[] vec1, float[] vec2) {
        float dist = 0.0F;
        if (vec1.length != vec2.length) {
            return dist;
        }
        for (int i = 0; i < vec1.length; ++i) {
            dist += vec1[i] * vec2[i];
        }
        return dist;
    }

    /**
     * 计算两个向量之间的余弦相似度
     *
     * @param vec1 向量1
     * @param vec2 向量2
     * @return 相似度值
     */
    double calCosine(float[] vec1, float[] vec2) {
        double dist = 0.0;
        double sum1 = 0.0;
        double sum2 = 0.0;
        if (vec1.length != vec2.length) {
            return dist;
        }
        for (int i = 0; i < vec1.length; ++i) {
            dist += vec1[i] * vec2[i];
            sum1 += Math.pow(vec1[i], 2);
            sum2 += Math.pow(vec2[i], 2);
        }
        double result = dist / Math.sqrt(sum1 * sum2);
        //在计算过程中，向量一致的词由于算法的不完全匹配性，存在比较小的误差，
        //为避免大于1这种情况，对后续计算过程中的英雄，暂时的将相似度控制到100%以内
        return result > 1.0 ? 1.0D : result;
    }

    private void calSum(float[] sum, float[] vec) {
        for (int i = 0; i < sum.length; ++i) {
            sum[i] += vec[i];
        }

    }

    public double wordSimilarity(String word1, String word2) {
        if (!this.loadModel) {
            return 0.0D;
        } else {
            float[] word1Vec = this.getWordVector(word1);
            float[] word2Vec = this.getWordVector(word2);
            return word1Vec != null && word2Vec != null ? this.calCosine(word1Vec, word2Vec) : 0.0D;
        }
    }

    private void printFloat() {

    }

    public Set<WordEntry> getSimilarWords(String word, int maxReturnNum) {
        if (!this.loadModel) {
            return null;
        } else {
            float[] center = this.getWordVector(word);
            if (center == null) {
                return Collections.emptySet();
            } else {
                int resultSize = this.vec.getWords() < maxReturnNum ? this.vec.getWords() : maxReturnNum;
                TreeSet<WordEntry> result = new TreeSet<>();
                double min = 4.9E-324D;
                Iterator var8 = this.vec.getWordMap().entrySet().iterator();

                while (var8.hasNext()) {
                    Entry<String, float[]> entry = (Entry) var8.next();
                    float[] vector = entry.getValue();
                    float dist = this.calDist(center, vector);
                    if (result.size() <= resultSize) {
                        result.add(new WordEntry(entry.getKey(), dist));
                        min = (double) (result.last()).score;
                    } else if ((double) dist > min) {
                        result.add(new WordEntry(entry.getKey(), dist));
                        result.pollLast();
                        min = (double) (result.last()).score;
                    }
                }

                result.pollFirst();
                return result;
            }
        }
    }

    double calMaxSimilarity(String centerWord, List<Term> wordList) {
        double max = -1.0F;
        for (Term term : wordList) {
            if (term.word.equals(centerWord)) {
                return 1.0F;
            }
        }
        for (Term term : wordList) {
            double temp = this.wordSimilarity(centerWord, term.word);
            if (temp != 0.0F && temp > max) {
                max = temp;
            }
        }

        if (max == -1.0F) {
            return 0.0F;
        } else {
            return max;
        }

    }

    public float[] getSentenceWordVec(List<String> sentenceWords) {
        if (!this.loadModel) {
            return null;
        } else if (sentenceWords.isEmpty()) {
            return null;
        } else {
            float[] senvector = new float[this.vec.getSize()];

            for (String sentenceWord : sentenceWords) {
                float[] tmp = this.getWordVector(sentenceWord);
                if (tmp != null) {
                    this.calSum(senvector, tmp);
                }
            }

            return senvector;
        }
    }

    public float fastSentenceSimilarity(List<String> sentence1Words, List<String> sentence2Words) {
        if (!this.loadModel) {
            return 0.0F;
        } else if (!sentence1Words.isEmpty() && !sentence2Words.isEmpty()) {
            float[] sen1vector = new float[this.vec.getSize()];
            float[] sen2vector = new float[this.vec.getSize()];
            double len1 = 0.0D;
            double len2 = 0.0D;

            int i;
            float[] tmp;
            for (i = 0; i < sentence1Words.size(); ++i) {
                tmp = this.getWordVector(sentence1Words.get(i));
                if (tmp != null) {
                    this.calSum(sen1vector, tmp);
                }
            }

            for (i = 0; i < sentence2Words.size(); ++i) {
                tmp = this.getWordVector(sentence2Words.get(i));
                if (tmp != null) {
                    this.calSum(sen2vector, tmp);
                }
            }

            for (i = 0; i < this.vec.getSize(); ++i) {
                len1 += (double) (sen1vector[i] * sen1vector[i]);
                len2 += (double) (sen2vector[i] * sen2vector[i]);
            }

            return (float) ((double) this.calDist(sen1vector, sen2vector) / Math.sqrt(len1 * len2));
        } else {
            return 0.0F;
        }
    }

    public double sentenceSimilarity(List<Term> sentence1Words, List<Term> sentence2Words) {
        if (!this.loadModel) {
            return 0.0F;
        } else if (!sentence1Words.isEmpty() && !sentence2Words.isEmpty()) {
            float sum1 = 0.0F;
            float sum2 = 0.0F;
            int count1 = 0;
            int count2 = 0;

            int i;
            for (i = 0; i < sentence1Words.size(); ++i) {
                if (this.getWordVector(sentence1Words.get(i).word) != null) {
                    ++count1;
                    sum1 += this.calMaxSimilarity(sentence1Words.get(i).word, sentence2Words);
                }
            }

            for (i = 0; i < sentence2Words.size(); ++i) {
                if (this.getWordVector(sentence2Words.get(i).word) != null) {
                    ++count2;
                    sum2 += this.calMaxSimilarity(sentence2Words.get(i).word, sentence1Words);
                }
            }

            return (sum1 + sum2) / (count1 + count2);
        } else {
            return 0.0F;
        }
    }

    public float sentenceSimilarity(List<Term> sentence1Words, List<Term> sentence2Words, float[] weightVector1, float[] weightVector2) throws Exception {
        if (!this.loadModel) {
            return 0.0F;
        } else if (!sentence1Words.isEmpty() && !sentence2Words.isEmpty()) {
            if (sentence1Words.size() == weightVector1.length && sentence2Words.size() == weightVector2.length) {
                float sum1 = 0.0F;
                float sum2 = 0.0F;
                float divide1 = 0.0F;
                float divide2 = 0.0F;

                int i;
                for (i = 0; i < sentence1Words.size(); ++i) {
                    if (this.getWordVector(sentence1Words.get(i).word) != null) {
                        sum1 += this.calMaxSimilarity(sentence1Words.get(i).word, sentence2Words) * weightVector1[i];
                        divide1 += weightVector1[i];
                    }
                }
                for (i = 0; i < sentence2Words.size(); ++i) {
                    if (this.getWordVector(sentence2Words.get(i).word) != null) {
                        sum2 += this.calMaxSimilarity(sentence2Words.get(i).word, sentence1Words) * weightVector2[i];
                        divide2 += weightVector2[i];
                    }
                }

                return (sum1 + sum2) / (divide1 + divide2);
            } else {
                throw new Exception("length of word list and weight vector is different");
            }
        } else {
            return 0.0F;
        }
    }
}
