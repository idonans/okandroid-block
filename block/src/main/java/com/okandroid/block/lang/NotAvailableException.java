package com.okandroid.block.lang;

public class NotAvailableException extends RuntimeException {

    public NotAvailableException() {}

    public NotAvailableException(String detailMessage) {
        super(detailMessage);
    }

    public NotAvailableException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NotAvailableException(Throwable throwable) {
        super(throwable);
    }
}
