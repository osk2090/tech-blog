package com.osk2090.blog.proxy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.PatternMatchUtils;

@SpringBootTest
@Slf4j
public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {

    // 기존에는 모든 클래스이름을 확인하였으나 이번에는 인자로 받은 클래스이름만 필터에서 확인할수 있도록 한다.
    public void setMappedClassName(String mappedClassName) {
        this.setClassFilter(new SimpleClassFilter(mappedClassName));
    }

    static class SimpleClassFilter implements ClassFilter {

        String mappedName;

        public SimpleClassFilter(String mappedName) {
            this.mappedName = mappedName;
        }

        @Override
        public boolean matches(Class<?> clazz) {
            // 와일드카드가 들어간 문자열 비교를 지원하는 스프링의 유틸리티 메소드
            return PatternMatchUtils.simpleMatch(mappedName, clazz.getSimpleName());
        }
    }

    @Test
    void wildCardTest() {
        String pattern = "*one";
        String str = "one";

        boolean result = PatternMatchUtils.simpleMatch(pattern, str);
        log.info("wildCardTest: {}", result ? "same" : "different");
    }
}
