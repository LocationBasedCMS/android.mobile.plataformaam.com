package com.plataformaam.mobile.clientefinal.helpers.jsonresponse;


/**
 * Created by bernard on 06/01/2015.
 */
public class JsonSuccessResponse {
    protected String success;
    protected String message;

    public JsonSuccessResponse(String success, String message) {
        this.success = success;
        this.message = message;
    }

    public JsonSuccessResponse() {
    }

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
