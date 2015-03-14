package com.plataformaam.mobile.clientefinal.helpers.eventbus;

import com.plataformaam.mobile.clientefinal.models.location.UserPosition;

/**
 * Created by bernard on 05/03/2015.
 */
public class MyPositionMessage {
    private String Sender;
    private String Message;
    private UserPosition userPosition;

    public MyPositionMessage(String sender, String message, UserPosition userPosition) {
        Sender = sender;
        Message = message;
        this.userPosition = userPosition;
    }

    public MyPositionMessage(String sender, String message) {
        Sender = sender;
        Message = message;
    }

    public MyPositionMessage() {
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public UserPosition getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(UserPosition userPosition) {
        this.userPosition = userPosition;
    }
}
