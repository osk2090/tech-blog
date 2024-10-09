package com.osk2090.blog.user.sqlService;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
