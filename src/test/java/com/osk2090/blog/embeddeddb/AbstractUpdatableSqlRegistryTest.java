package com.osk2090.blog.embeddeddb;

import com.osk2090.blog.user.sqlService.SqlNotFoundException;
import com.osk2090.blog.user.sqlService.UpdatableSqlRegistry;
import com.osk2090.blog.user.sqlService.updatable.SqlUpdateFailureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public abstract class AbstractUpdatableSqlRegistryTest {
    UpdatableSqlRegistry sqlRegistry;

    @BeforeEach
    public void setUp() {
        sqlRegistry = createUpdatableSqlRegistry();
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    // 테스트 픽처를 생성하는 부분만 추상 메서드로 만들어 서브클래스에서 구현하도록
    abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

    // 서브클래스에서 테스트를 추가한다면 필요할수도 있으니 서브클래스에서 접근이 가능하도록 protected 선언
    protected void checkFind(String expected1, String expected2, String expected3) {
        assertThat(sqlRegistry.findSql("KEY1")).isEqualTo(expected1);
        assertThat(sqlRegistry.findSql("KEY2")).isEqualTo(expected2);
        assertThat(sqlRegistry.findSql("KEY3")).isEqualTo(expected3);
    }

    @Test
    public void find() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    private void checkFindResult(String expected1, String expected2, String expected3) {
        assertThat(sqlRegistry.findSql("KEY1")).isEqualTo(expected1);
        assertThat(sqlRegistry.findSql("KEY2")).isEqualTo(expected2);
        assertThat(sqlRegistry.findSql("KEY3")).isEqualTo(expected3);
    }

    @Test
    public void unknownKey() {
        Assertions.assertThrows(SqlNotFoundException.class, () -> {
            sqlRegistry.findSql("SQL9999!@#$");
        });
    }

    @Test
    public void updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2");
        checkFindResult("SQL1", "Modified2", "SQL3");
    }

    @Test
    public void updateMulti() {
        HashMap<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY3", "Modified3");

        sqlRegistry.updateSql(sqlmap);
        checkFindResult("Modified1", "SQL2", "Modified3");
    }

    @Test
    public void updateWithNotExistingKey() {
        Assertions.assertThrows(SqlUpdateFailureException.class, () -> {
            sqlRegistry.updateSql("SQL9999!@#$", "Modified2");
        });
    }
}
