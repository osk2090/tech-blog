package com.osk2090.blog.user.sqlService;

public class SqlRetrievalFailureException extends RuntimeException {
    public SqlRetrievalFailureException(String message) {
        super(message);
    }

    // 여기서 throwable은 근본적인 예외 원인을 알수 있도록 해준다.
    public SqlRetrievalFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
