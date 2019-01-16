package com.inspur.tax.mvc.beans;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;


public class PlatformResponse {
    @JSONField(name = "match")
    private String match;
    @JSONField(name = "question")
    private String question;
    @JSONField(name = "score")
    private double score;
    @JSONField(name = "list")
    private JSONArray list;
    @JSONField(name = "answer")
    private String answer;
    @JSONField(name = "answer_type")
    private String answer_type;
    @JSONField(name = "ref_id")
    private String ref_id;
    @JSONField(name = "key")
    private String key;
    @JSONField(name = "scene_end")
    private String scene_end;
    @JSONField(name = "user_id")
    private String user_id;


    @JSONField(name = "media_url")
    private String media_url;

    public PlatformResponse() {
    }

    public PlatformResponse(String match, String question, double score, JSONArray list, String answer, String key, String answer_type, String ref_id, String scene_end, String user_id,String media_url) {
        this.match = match;
        this.question = question;
        this.score = score;
        this.list = list;
        this.answer = answer;
        this.key = key;
        this.answer_type = answer_type;
        this.ref_id = ref_id;
        this.scene_end = scene_end;
        this.user_id = user_id;
        this.media_url = media_url;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public JSONArray getList() {
        return list;
    }

    public void setList(JSONArray list) {
        this.list = list;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getScene_end() {
        return scene_end;
    }

    public void setScene_end(String scene_end) {
        this.scene_end = scene_end;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    @Override
    public String toString() {
        return "PlatformResponse{" +
                "match='" + match + '\'' +
                ", question='" + question + '\'' +
                ", score=" + score +
                ", list='" + list + '\'' +
                ", answer='" + answer + '\'' +
                ", key='" + key + '\'' +
                ", ref_id='" + ref_id + '\'' +
                ", scene_end='" + scene_end + '\'' +
                '}';
    }

    public String getAnswer_type() {
        return answer_type;
    }

    public void setAnswer_type(String answer_type) {
        this.answer_type = answer_type;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }
}
