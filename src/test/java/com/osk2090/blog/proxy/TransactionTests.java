package com.osk2090.blog.proxy;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBootTest
public class TransactionTests {

    PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object ret = invocation.proceed();
            this.transactionManager.commit(status);
            return ret;
        } catch (RuntimeException e) { // 지금 런타임예외가 발생하면 롤백하게 되어있는데 이거 아마 옵션에서 상위예외인 Exception 클래스를 넣어줘야된다.
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
