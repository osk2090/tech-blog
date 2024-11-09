package com.osk2090.blog.user.sqlService;

import com.epril.sqlmap.SqlType;
import com.epril.sqlmap.Sqlmap;
import com.osk2090.blog.user.dao.UserDao;
import com.osk2090.blog.user.sqlService.updatable.ConcurrentHashMapSqlRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

@Component
public class OxmSqlService implements SqlService {
    private final BaseSqlService baseSqlService;
    private final SqlRegistry sqlRegistry;
    private final OxmSqlReader oxmSqlReader;

    public void setSqlmap(Resource sqlmap) {
        this.oxmSqlReader.setSqlmap(sqlmap);
    }

    public OxmSqlService(
            BaseSqlService baseSqlService,
            @Qualifier("jaxb2Marshaller") Unmarshaller unmarshaller,  // @Qualifier 추가
            ConcurrentHashMapSqlRegistry sqlRegistry  // SqlRegistry 주입 추가
    ) {
        this.baseSqlService = baseSqlService;
        this.sqlRegistry = sqlRegistry;
        this.oxmSqlReader = new OxmSqlReader(unmarshaller);
    }

    @PostConstruct
    private void loadSql() {
        this.baseSqlService.setSqlReader(this.oxmSqlReader);
        this.baseSqlService.setSqlRegistry(this.sqlRegistry);
        this.baseSqlService.loadSql();
    }

    @RequiredArgsConstructor
    static class OxmSqlReader implements SqlReader {
        private final Unmarshaller unmarshaller;  // 인터페이스로 받는 것은 유지 (느슨한 결합)
        private Resource sqlmap = new ClassPathResource("/sqlmap.xml", UserDao.class);

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
//                StreamSource source = new StreamSource(UserDao.class.getResourceAsStream(this.sqlmapFile));
                Source source = new StreamSource(sqlmap.getInputStream());
                Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(source);
                for (SqlType sql : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(this.sqlmap.getFilename() + "을 가져올 수 없습니다.", e);
            }
        }
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.baseSqlService.getSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(key, e);
        }
    }
}
