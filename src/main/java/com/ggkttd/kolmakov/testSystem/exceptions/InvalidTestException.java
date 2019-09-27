package com.ggkttd.kolmakov.testSystem.exceptions;

public class InvalidTestException extends RuntimeException {
    public InvalidTestException() {
        super();
    }

    public InvalidTestException(String message) {
        super(message);
    }
}
