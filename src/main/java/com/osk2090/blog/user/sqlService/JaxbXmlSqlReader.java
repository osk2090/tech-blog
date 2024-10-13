package com.osk2090.blog.user.sqlService;

import com.epril.sqlmap.SqlType;
import com.epril.sqlmap.Sqlmap;
import com.osk2090.blog.user.dao.UserDao;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Setter
@Component
public class JaxbXmlSqlReader implements SqlReader {
    private static final String DEFAULT_SQLMAP_FILE = "/sqlmap.xml";
    private String sqlmapFile = DEFAULT_SQLMAP_FILE;

    @Override
    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();
        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = UserDao.class.getResourceAsStream(sqlmapFile);
            Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
            for(SqlType sql : sqlmap.getSql()) {
                sqlRegistry.registerSql(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) { throw new RuntimeException(e); }
    }
}
