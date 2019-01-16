package com.inspur.tax.core.nlp.cosinesimlarity;

import com.hankcs.hanlp.seg.common.Term;

import java.io.IOException;
import java.util.List;

public class SimilarityAnalyzeUnfamiliarWords extends SimilarityAnalyze {
    Word2Vec charVec;
    public static int dimension = 200;

    /**
     * 相似性判断的分析过程测试
     */
    public static void main(String[] args) throws IOException {
        SimilarityAnalyzeUnfamiliarWords similarityAnalyzeUnfamiliarWords = new SimilarityAnalyzeUnfamiliarWords();
        similarityAnalyzeUnfamiliarWords.loadCommonModel("D:/ideaProject/knowledge-core/data/word2vec_wv_add_java_2018-10-09.model");
        System.out.println(similarityAnalyzeUnfamiliarWords.wordSimilarity("父亲", "母亲"));
    }

    public SimilarityAnalyzeUnfamiliarWords() {
        this.vec = new Word2Vec();
        this.charVec = new Word2Vec();
        this.loadModel = false;
    }

    public void loadCharJavaModel(String modelPath) {
        try {
            this.charVec.loadCommonModel(modelPath);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        this.loadModel = true;
    }

    public double wordSimilarity(String word1, String word2) {
        if (!this.loadModel) {
            return 0.0F;
        } else {
            float[] word1Vec = this.getWordVector(word1);
            float[] word2Vec = this.getWordVector(word2);
            if (word1Vec == null) {
                word1Vec = this.getNullVec(word1);
            }
            if (word2Vec == null) {
                word2Vec = this.getNullVec(word2);
            }

            return this.calCosine(word1Vec, word2Vec);
        }
    }

    public float[] meanVec(List<String> sentenceWords) {
        float[] mean = new float[this.dimension];
        int count = 0;

        int i;
        for (i = 0; i < sentenceWords.size(); ++i) {
            if (this.getWordVector(sentenceWords.get(i)) != null) {
                ++count;
                for (int j = 0; j < this.dimension; ++j) {
                    mean[j] += this.getWordVector(sentenceWords.get(i))[j];
                }
            }
        }

        for (i = 0; i < this.dimension; ++i) {
            mean[i] /= (float) this.dimension;
        }

        return mean;
    }

    private float[] getCharVector(String word) {
        return !this.loadModel ? null : this.charVec.getWordVector(word);
    }

    private float[] getNullVec(String word) {
        float[] nullVec = new float[this.dimension];
        int count = 0;
        String atomSegment = AtomSegment.atomSegment(word).trim();
        String[] atomSegmentStr = atomSegment.split("\\s+");
        int i;
        for (i = 0; i < atomSegmentStr.length; ++i) {
            if (this.getCharVector(atomSegmentStr[i]) != null) {
                ++count;

                for (int j = 0; j < this.dimension; ++j) {
                    nullVec[j] += this.getCharVector(atomSegmentStr[i])[j];
                }
            }
        }

        if (0 != count && 1 != count) {
            for (i = 0; i < this.dimension; ++i) {
                nullVec[i] /= (float) count;
            }

            return nullVec;
        } else {
            return nullVec;
        }
    }

    /**
     * 句子相似度计算
     * @param sentence1Words 分词之后的文本一
     * @param sentence2Words 分词之后的文本二
     * @return 相似度评分（0-1之间的概率）
     */
    public double sentenceSimilarity(List<Term> sentence1Words, List<Term> sentence2Words) {
        if (!this.loadModel) {
            return 0.0F;
        } else if (!sentence1Words.isEmpty() && !sentence2Words.isEmpty()) {
            float sum1 = 0.0F;
            float sum2 = 0.0F;
            int count1 = 0;
            int count2 = 0;
            //计算第一句话得每一个词和另一句话中最相似的词的相似度
            for (Term sentence1Word : sentence1Words) {
                ++count1;
                sum1 += this.calMaxSimilarity(sentence1Word.word, sentence2Words);
            }
            //计算第二句话得每一个词和另一句话中最相似的词的相似度
            for (Term sentence2Word : sentence2Words) {
                ++count2;
                sum2 += this.calMaxSimilarity(sentence2Word.word, sentence1Words);
            }
            //检测数量是不是为0是为了避免计算过程中产生NAN导致报错
            if (count1 == 0) {
                if (count2 == 0) {
                    return 0F;
                } else {
                    return sum2 / count2;
                }
            } else if (count2 == 0) {
                return sum1 / count1;
            }
            //去相似度最小的那个，能够避免长短文本比较而产生文本包含关系的问题
            return Math.min(sum1 / (count1), sum2 / count2);
        } else {
            return 0.0F;
        }
    }

}
