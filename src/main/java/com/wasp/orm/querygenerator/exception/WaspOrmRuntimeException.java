package com.wasp.orm.querygenerator.exception;

public class WaspOrmRuntimeException extends RuntimeException {

    public WaspOrmRuntimeException() {
        super();
    }

    public WaspOrmRuntimeException(String message) {
        super(message);
    }

    public WaspOrmRuntimeException(String message, Exception e) {
        super(message, e);
    }
}
