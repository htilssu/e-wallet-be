package com.ewallet.ewallet.transfer;

public class ReceiverNotFoundException extends RuntimeException {
    public ReceiverNotFoundException(String message) {
        super(message);
    }

    public ReceiverNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}