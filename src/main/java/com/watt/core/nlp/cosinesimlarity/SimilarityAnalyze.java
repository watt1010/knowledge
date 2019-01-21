//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.watt.core.nlp.cosinesimlarity;

import com.hankcs.hanlp.seg.common.Term;

import java.io.IOException;
import java.util.List;

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


    float[] getWordVector(String word) {
        return !this.loadModel ? null : this.vec.getWordVector(word);
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

    public abstract double wordSimilarity(String word1, String word2);

    public abstract double sentenceSimilarity(List<Term> sentence1Words, List<Term> sentence2Words);

}
