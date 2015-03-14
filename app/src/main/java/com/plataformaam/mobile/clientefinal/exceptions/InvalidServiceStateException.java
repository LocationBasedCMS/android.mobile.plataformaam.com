package com.plataformaam.mobile.clientefinal.exceptions;

/**
 * Created by bernard on 07/02/2015.
 */
public class InvalidServiceStateException  extends Exception  {
    public InvalidServiceStateException() {
        super();
    }

    public InvalidServiceStateException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidServiceStateException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidServiceStateException(Throwable throwable) {
        super(throwable);
    }
}
