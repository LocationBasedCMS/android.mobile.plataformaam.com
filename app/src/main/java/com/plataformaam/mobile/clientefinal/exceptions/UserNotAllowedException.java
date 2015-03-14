package com.plataformaam.mobile.clientefinal.exceptions;

/**
 * Created by bernard on 17/01/2015.
 */
public class UserNotAllowedException extends Exception {
    public UserNotAllowedException() {
    }

    public UserNotAllowedException(String detailMessage) {
        super(detailMessage);
    }

    public UserNotAllowedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UserNotAllowedException(Throwable throwable) {
        super(throwable);
    }
}
