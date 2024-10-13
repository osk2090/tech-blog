package com.osk2090.blog.jaxb;

import com.epril.sqlmap.SqlType;
import com.epril.sqlmap.Sqlmap;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JaxbTest {

    @Test
    public void readSqlmap() throws JAXBException {
        String contextPath = Sqlmap.class.getPackage().getName();
        JAXBContext context = JAXBContext.newInstance(contextPath);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(getClass().getResourceAsStream("/sqlmap.xml"));

        List<SqlType> sqlTypeList = sqlmap.getSql();

        assertThat(sqlTypeList.size()).isEqualTo(3);
        assertThat(sqlTypeList.get(0).getKey()).isEqualTo("add");
        assertThat(sqlTypeList.get(0).getValue()).isEqualTo("insert");

    }
}
