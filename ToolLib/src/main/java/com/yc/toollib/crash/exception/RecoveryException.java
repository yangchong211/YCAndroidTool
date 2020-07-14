package com.yc.toollib.crash.exception;


public class RecoveryException extends RuntimeException {

    public RecoveryException(String message) {
        super(message);
    }

    public RecoveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
