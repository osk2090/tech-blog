package com.osk2090.blog.oxm;

import com.epril.sqlmap.SqlType;
import com.epril.sqlmap.Sqlmap;
import com.osk2090.blog.jaxb.UnmarshallerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.oxm.XmlMappingException;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class OxmTests {

    @Autowired
    org.springframework.oxm.Unmarshaller unmarshaller;

    @Test
    void unmarshallerSqlMap() throws XmlMappingException, IOException {
        StreamSource xmlSource = new StreamSource(getClass().getResourceAsStream("/sqlmap.xml"));
        Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);
        List<SqlType> sqlList = sqlmap.getSql();
        assertThat(sqlList.size()).isEqualTo(3);
        assertThat(sqlList.get(0).getKey()).isEqualTo("add");
        assertThat(sqlList.get(2).getKey()).isEqualTo("delete");
    }
}
