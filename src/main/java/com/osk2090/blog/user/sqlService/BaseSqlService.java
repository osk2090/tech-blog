package com.osk2090.blog.user.sqlService;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Setter
@Component
public class BaseSqlService implements SqlService {
    @Autowired
    @Qualifier("jaxbXmlSqlReader")
    private SqlReader sqlReader;
    @Autowired
    @Qualifier("hashMapSqlRegistry")
    private SqlRegistry sqlRegistry;

    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(key, e);
        }
    }
}
