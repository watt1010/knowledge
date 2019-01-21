package com.watt.mvc.beans;

public class QAAnalyzeResult {
    private double score;
    private String key;
    private String match;

    public QAAnalyzeResult() {
    }

    public QAAnalyzeResult(double score, String key, String match) {
        this.score = score;
        this.key = key;
        this.match = match;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }
}
