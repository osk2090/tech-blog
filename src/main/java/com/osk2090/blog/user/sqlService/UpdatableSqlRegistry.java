package com.osk2090.blog.user.sqlService;

import com.osk2090.blog.user.sqlService.updatable.SqlUpdateFailureException;

import java.util.Map;

public interface UpdatableSqlRegistry extends SqlRegistry {
    public void updateSql(String key, String sql) throws SqlUpdateFailureException;

    public void updateSql(Map<String, String> sqlmqp) throws SqlUpdateFailureException;
}
