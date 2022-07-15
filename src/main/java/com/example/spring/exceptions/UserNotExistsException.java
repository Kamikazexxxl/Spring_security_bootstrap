package com.example.spring.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotExistsException extends AuthenticationException {
    public UserNotExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserNotExistsException(String msg) {
        super(msg);
    }
}
