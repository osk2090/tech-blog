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
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class OxmTests {

    @Autowired
    org.springframework.oxm.Unmarshaller unmarshaller;

    @Test
    void unmarshallerSqlMap() {
        try (InputStream is = getClass().getResourceAsStream("/sqlmap.xml")) {
            assertThat(is).isNotNull().withFailMessage("Failed to load sqlmap.xml");
            StreamSource xmlSource = new StreamSource(is);
            Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);

            List<SqlType> sqlList = sqlmap.getSql();
            assertThat(sqlList).hasSize(3);
            assertThat(sqlList.get(0).getKey()).isEqualTo("add");
            assertThat(sqlList.get(2).getKey()).isEqualTo("delete");
        } catch (XmlMappingException e) {
            e.printStackTrace();
            fail("XML mapping failed: " + e.getMessage());
        } catch (IOException e) {
            fail("IO operation failed: " + e.getMessage());
        }
    }
}
