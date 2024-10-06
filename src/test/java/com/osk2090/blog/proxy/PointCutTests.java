package com.osk2090.blog.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@SpringBootTest
public class PointCutTests {

    @Test
    void classNamePointcutAdvisor() {
        // 포인트컷 준비
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut(){
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        // 클래스이름이 HelloT 라고 시작하는 클래스에만 적용될수 있도록 한다.
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };

        // 테스트
        classMethodPointcut.setMappedName("sayH*"); // 내부 메서드중에서 sayH 로 시작하는 메서드만 동작할수 있도록 한다.

        checkAdviced(new ProxyTests.HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends ProxyTests.HelloTarget {

        }

        // 해당 클래스는 HelloW 로 시작하므로 동작되지않는다.
        checkAdviced(new HelloWorld(), classMethodPointcut, false);

        class HelloOsk extends ProxyTests.HelloTarget {

        }

        // 해당 클래스도 classFilter 오버라이딩된 메서드에 부합하지 않는 조건의 클래스이름이라 실패
        checkAdviced(new HelloOsk(), classMethodPointcut, true);
    }

    /**
     *
     * @param target
     * @param pointcut
     * @param adviced 적용대상인가?
     */
    void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new ProxyTests.UpperCaseAdvice()));
        ProxyTests.Hello proxiedHello = (ProxyTests.Hello) pfBean.getObject();

        if (adviced) {
            // 메서드 선정 방식을 통해 어드바이스 적용되는 구간
            assertThat(proxiedHello.sayHello("Osk")).isEqualTo("HELLO OSK");
            assertThat(proxiedHello.sayHi("Osk")).isEqualTo("HI OSK");
            //
            assertThat(proxiedHello.sayThankYou("Osk")).isEqualTo("Thank you Osk");
        } else {
            // 어드바이스 저개용 대상 후보에서 아예 탈락하는 구간
            assertThat(proxiedHello.sayHello("Osk")).isEqualTo("Hello OSK");
            assertThat(proxiedHello.sayHi("Osk")).isEqualTo("Hi OSK");
            assertThat(proxiedHello.sayThankYou("Osk")).isEqualTo("Thank you Osk");
        }
    }
}
