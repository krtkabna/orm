package com.wasp.orm.querygenerator.exception;

public class NoSuchFieldRuntimeException extends RuntimeException {
    public NoSuchFieldRuntimeException() {
        super();
    }

    public NoSuchFieldRuntimeException(String message) {
        super(message);
    }
}
