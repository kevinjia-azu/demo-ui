package com.pinyougou.entity;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description TODO
 * @Author kevin_Azu
 * @Date 2018.12.23 10:48
 * @Version 1.0
 **/
public class Result implements Serializable {

    private boolean if_success;
    private String message;

    public Result(boolean if_success, String message) {
        this.if_success = if_success;
        this.message = message;
    }

    public boolean isIf_success() {
        return if_success;
    }

    public void setIf_success(boolean if_success) {
        this.if_success = if_success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
