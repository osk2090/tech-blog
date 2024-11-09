package com.osk2090.blog.updatable;

import com.osk2090.blog.embeddeddb.AbstractUpdatableSqlRegistryTest;
import com.osk2090.blog.user.sqlService.updatable.ConcurrentHashMapSqlRegistry;
import com.osk2090.blog.user.sqlService.UpdatableSqlRegistry;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConcurrentHashMapSqlRegistryTests extends AbstractUpdatableSqlRegistryTest {

    UpdatableSqlRegistry sqlRegistry;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
