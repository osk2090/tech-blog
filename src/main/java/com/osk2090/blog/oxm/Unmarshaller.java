package com.osk2090.blog.oxm;

import org.springframework.oxm.XmlMappingException;

import javax.xml.transform.Source;
import java.io.IOException;

public interface Unmarshaller {
    boolean supports(Class<?> clazz);

    Object unmarshal(Source source) throws IOException, XmlMappingException;
}
