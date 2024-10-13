package com.osk2090.blog.user.sqlService;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Component
public class SimpleSqlService implements SqlService, SqlRegistry, SqlReader {
    private Map<String, String> sqlMap;

    public SimpleSqlService() {
        // hint: 중간에 sql이 삽입될수 있으니 동시성 이슈를 위해 concurrenthashmap 구현체를 사용하자
        sqlMap = new ConcurrentHashMap<>();
        /*sqlMap.put("userAdd", "insert into users (id, name, password, email, level, login, recommend) values(?,?,?,?,?,?,?)");
        sqlMap.put("userGet", "select * from users where id = ?");
        sqlMap.put("userGetAll", "select * from users order by id");
        sqlMap.put("userDeleteAll", "delete from users");
        sqlMap.put("userGetCount", "select count(*) from users");
        sqlMap.put("userUpdate", "update users set name = ?, password = ?, email = ?, level = ?, login = ?, recommend = ? where id = ?");*/
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);
        if (sql == null) {
            throw new SqlRetrievalFailureException(key + " not found");
        } else {
            return sql;
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if (sql == null) {
            throw new SqlNotFoundException(key + "not found");
        } else {
            return sql;
        }
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {

    }
}
