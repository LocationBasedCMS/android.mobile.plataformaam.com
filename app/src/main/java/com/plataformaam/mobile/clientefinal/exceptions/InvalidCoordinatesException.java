package com.plataformaam.mobile.clientefinal.exceptions;

/**
 * Created by bernard on 17/01/2015.
 */
public class InvalidCoordinatesException extends Exception {
    public InvalidCoordinatesException() {
        super();
    }

    public InvalidCoordinatesException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidCoordinatesException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidCoordinatesException(Throwable throwable) {
        super(throwable);
    }
}
