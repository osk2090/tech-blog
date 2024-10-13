package com.osk2090.blog.user.sqlService;

import org.springframework.stereotype.Component;

@Component
public class DefaultSqlService extends BaseSqlService {

    public DefaultSqlService() {
        super(new JaxbXmlSqlReader(), new HashMapSqlRegistry());
    }
}
