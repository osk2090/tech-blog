package com.osk2090.blog.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class UnmarshallerConfig {

    @Bean("jaxb2Marshaller")  // 빈 이름 명시적 지정
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(com.epril.sqlmap.Sqlmap.class);  // 패키지명 확인 필요
        return marshaller;
    }
}
