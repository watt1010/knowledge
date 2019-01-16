package com.inspur.tax.mvc.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class NLPResult {

    @JSONField(name = "nlp")
    private List nlp;

    public NLPResult(List nlp){
        this.nlp=nlp;
    }

    public List getList() {
        return nlp;
    }

    public void setList(List nlp) {
        this.nlp = nlp;
    }
}
