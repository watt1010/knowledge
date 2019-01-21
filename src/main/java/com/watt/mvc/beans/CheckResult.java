package com.watt.mvc.beans;

public class CheckResult {
    private String code;
    private String message;
    private String content;

    public CheckResult(String code, String message, String content) {
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CheckResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
