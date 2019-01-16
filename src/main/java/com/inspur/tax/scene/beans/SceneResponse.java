package com.inspur.tax.scene.beans;

import com.alibaba.fastjson.annotation.JSONField;

public class SceneResponse {
    //状态代码 0：成功；1：失败
    @JSONField(name = "code")
    private int code;
    //阶段代码 11：返回话术 22：返回答案
    @JSONField(name = "st_code")
    private int st_code;
    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "en_id")
    private String en_id;
    @JSONField(name = "en_say")
    private String en_say;
    //结束标志 0：未结束；1：结束
    @JSONField(name = "end")
    private int end;

    public SceneResponse(String msg,String en_id,String en_say,int end,int code,int st_code){
        this.msg=msg;
        this.en_id=en_id;
        this.en_say=en_say;
        this.end=end;
        this.code=code;
        this.st_code=st_code;
    }

    public SceneResponse(){
        this.end=1;
        this.code=1;
    }
    public String getEn_id() {
        return en_id;
    }

    public void setEn_id(String en_id) {
        this.en_id = en_id;
    }

    public String getEn_say() {
        return en_say;
    }

    public void setEn_say(String en_say) {
        this.en_say = en_say;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSt_code() {
        return st_code;
    }

    public void setSt_code(int st_code) {
        this.st_code = st_code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
