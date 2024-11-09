package com.osk2090.blog.embeddeddb;

import com.osk2090.blog.user.sqlService.UpdatableSqlRegistry;
import com.osk2090.blog.user.sqlService.updatable.EmbeddedDbSqlRegistry;
import com.osk2090.blog.user.sqlService.updatable.SqlUpdateFailureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("/sqlRegistrySchema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void transactionalUpdate() {
        checkFind("SQL1", "SQL2", "SQL3");

        HashMap<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        // 아래코드는 키가 존재하지않으므로 예외가 발생할것으로 예상된다.
        // 따라서 롤백에 됨
        sqlmap.put("KEY9999!@#$", "Modified9999");

        try {
            sqlRegistry.updateSql(sqlmap);
            fail("dead line"); // 여기는 데드라인이므로 위에서 예외가 발생하면 해당 코드는 진행되면 안된다.
        } catch (SqlUpdateFailureException e) {
            // 위에서 롤백이 되었으니 데이터는 롤백된 상태인지 확인한다
            checkFind("SQL1", "SQL2", "SQL3");
        }
    }
}
