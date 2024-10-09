package com.osk2090.blog.user.sqlService;

public interface SqlReader {
    /**
     * sql를 외부에서 가져와서 sqlRegistry에 등록하는 역할
     * @param sqlRegistry
     */
    void read(SqlRegistry sqlRegistry);
}
