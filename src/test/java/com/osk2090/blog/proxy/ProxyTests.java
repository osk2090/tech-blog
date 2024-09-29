package com.osk2090.blog.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyTests {

    static class UpperCaseHandler implements InvocationHandler {
        Hello target;

        public UpperCaseHandler(Hello target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String ret = (String) method.invoke(target, args);
            return ret.toUpperCase();
        }
    }

    @Test
    void simpleProxy() {
        // jdk 다이나믹 프록시 생성
        Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[]{Hello.class},
                new UpperCaseHandler(new HelloTarget())
        );
    }

    @Test
    void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean(); // 프록시 객체를 만들수 있도록 함
        pfBean.setTarget(new HelloTarget()); // 타겟을 정함 즉 어떤 객체를 프록시 객체로 만드는지
        pfBean.addAdvice(new UpperCaseAdvice()); // 위에서 정한 타겟에 어떤 어드바이스를 적용시킬지

        Hello proxiedHello = (Hello) pfBean.getObject(); // 해당 메서드로 호출하면 위에서 정한 타겟의 프록시 객체를 던진다.
        assertThat(proxiedHello.sayHello("Osk")).isEqualTo("HELLO OSK");
        assertThat(proxiedHello.sayHi("Osk")).isEqualTo("HI OSK");
        assertThat(proxiedHello.sayThankYou("Osk")).isEqualTo("THANK YOU OSK");
    }

    @Test
    void proxyFactoryBeanDifferentMethodName() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        // 포인트컷을 "sayThankYou" 메서드에만 적용하도록 변경
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayThankYou");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        // 테스트
        assertThat(proxiedHello.sayHello("Osk")).isEqualTo("HELLO OSK");
        assertThat(proxiedHello.sayHi("Osk")).isEqualTo("HI OSK");
        assertThat(proxiedHello.sayThankYou("Osk")).isEqualTo("THANK YOU OSK");
    }

    interface Hello {
        String sayHello(String name);

        String sayHi(String name);

        String sayThankYou(String name);
    }

    static class HelloTarget implements Hello {

        @Override
        public String sayHello(String name) {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name) {
            return "Hi " + name;
        }

        @Override
        public String sayThankYou(String name) {
            return "Thank you " + name;
        }
    }

    static class UpperCaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    void pointCutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        // 메서드 이름을 기준으로 포인트컷을 생성하는 인스턴스 생성
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*"); // sayHxxx로 선언된 모든 메서드이름을 찾는다.

        // 포인트컷과 어드바이저를 같이 추가
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        // 테스트
        assertThat(proxiedHello.sayHello("Osk")).isEqualTo("HELLO OSK");
        assertThat(proxiedHello.sayHi("Osk")).isEqualTo("HI OSK");
        // 해당 메서드는 포인트컷에서 선언한 메서드명 기준에 부합되지 않아서 대문자로 변경되지 않는다.
        assertThat(proxiedHello.sayThankYou("Osk")).isEqualTo("Thank you Osk");
    }
}
