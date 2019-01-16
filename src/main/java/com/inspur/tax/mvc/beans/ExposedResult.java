package com.inspur.tax.mvc.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import java.util.Map;

public class ExposedResult {
    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @JSONField(name = "result")
    private List list;

    public ExposedResult(){}
    public ExposedResult(List<Map> list){
        this.list=list;
    }
}
