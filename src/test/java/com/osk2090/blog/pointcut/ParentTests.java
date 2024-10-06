package com.osk2090.blog.pointcut;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ParentTests {
    interface Parent {
        void name();
    }

    static class SonFromParent implements Parent {
        @Override
        public void name() {
            log.info("my name is son");
        }
    }

    static class DaughterFromParent implements Parent {
        @Override
        public void name() {
            log.info("my name is daughter");
        }
    }

    static class GrandSonFromDaughter extends DaughterFromParent {
        @Override
        public void name() {
            log.info("my name is daughter's son");
        }
    }
}
