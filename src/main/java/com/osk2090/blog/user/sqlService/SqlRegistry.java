package com.osk2090.blog.user.sqlService;

public interface SqlRegistry {
    /**
     * sql를 키와함께 등록한다.
     *
     * @param key
     * @param sql
     */
    void registerSql(String key, String sql);

    /**
     * 키를 이용해서 sql를 검색한다. 실패시 예외던진다.
     *
     * @param key
     * @return
     * @throws SqlNotFoundException
     */
    String findSql(String key) throws SqlNotFoundException;
}
