package com.plataformaam.mobile.clientefinal.helpers.gson.api;

import org.json.JSONObject;

/**
 * Created by bernard on 02/03/2015.
 */
public class SuccessResponseV1 {
    boolean success;
    String message;
    JSONObject data;


    public SuccessResponseV1() {
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
