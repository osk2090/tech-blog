package com.osk2090.blog.user.sqlService;

public class SqlNotFoundException extends RuntimeException {
    public SqlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlNotFoundException(String message) {
        super(message);
    }
}
