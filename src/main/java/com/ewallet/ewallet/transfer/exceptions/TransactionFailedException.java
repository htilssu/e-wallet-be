package com.ewallet.ewallet.transfer.exceptions;

public class TransactionFailedException extends RuntimeException {

    public TransactionFailedException(String message) {
        super(message);
    }
}
