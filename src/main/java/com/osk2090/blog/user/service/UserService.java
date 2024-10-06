package com.osk2090.blog.user.service;

import com.osk2090.blog.user.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService {
    void add(User user);

    void deleteAll();

    void update(User user);

    void upgradeLevels();


    /*
    이거 하위 클래스에서 별도로 선언하면 해당 트랜잭션 옵션은 무시된다.
    왜냐하면 트랜잭션의 대체정책에 따라 타깃 클래스의 트랜잭션 옵션부터 먼저 적용되기 때문이다.
     */
    @Transactional(readOnly = true)
    User get(String id);

    @Transactional(readOnly = true)
    List<User> getAll();
}
