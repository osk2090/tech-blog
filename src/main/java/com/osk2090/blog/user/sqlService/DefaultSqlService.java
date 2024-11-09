package com.osk2090.blog.user.sqlService;

import com.osk2090.blog.user.sqlService.updatable.ConcurrentHashMapSqlRegistry;
import org.springframework.stereotype.Component;

public class DefaultSqlService extends BaseSqlService {

    public DefaultSqlService() {
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new ConcurrentHashMapSqlRegistry());
    }
}
