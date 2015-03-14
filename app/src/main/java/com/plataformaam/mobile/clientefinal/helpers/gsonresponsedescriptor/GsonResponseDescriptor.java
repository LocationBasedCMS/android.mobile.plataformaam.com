package com.plataformaam.mobile.clientefinal.helpers.gsonresponsedescriptor;

/**
 * Created by bernard on 31/01/2015.
 */
public class GsonResponseDescriptor<T> {
    boolean success;
    String message;
    T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public GsonResponseDescriptor(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public GsonResponseDescriptor() {
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

