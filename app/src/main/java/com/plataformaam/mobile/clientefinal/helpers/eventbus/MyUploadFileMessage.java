package com.plataformaam.mobile.clientefinal.helpers.eventbus;

import android.net.Uri;

/**
 * Created by bernard on 25/05/2015.
 */
public class MyUploadFileMessage {
    private String sender;
    private String message;
    private String resultMessage;
    private boolean success;
    Uri imageUri;


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public MyUploadFileMessage() {
    }

    public MyUploadFileMessage(String sender, String message, Uri imageUri) {
        this.sender = sender;
        this.message = message;
        this.imageUri = imageUri;
    }


    public MyUploadFileMessage(boolean success, String resultMessage, String message, String sender) {
        this.success = success;
        this.resultMessage = resultMessage;
        this.message = message;
        this.sender = sender;
    }
    
}
