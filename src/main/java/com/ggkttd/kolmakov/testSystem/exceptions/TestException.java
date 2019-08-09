package com.ggkttd.kolmakov.testSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,reason = "test case")
public class TestException extends RuntimeException {
    public TestException() {
        super();
    }

    public TestException(String message) {
        super(message);
    }
}
