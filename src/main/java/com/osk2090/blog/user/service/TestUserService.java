package com.osk2090.blog.user.service;

import com.osk2090.blog.user.dao.UserDaoJdbc;
import com.osk2090.blog.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestUserService extends UserServiceImpl {
    private String id = "madnite1"; // users(3).getId()

    public TestUserService(UserDaoJdbc userDao, DummyMailSender mailSender) {
        super(userDao, mailSender);
    }

    protected void upgradeLevel(User user) {
        if (user.getId().equals(this.id)) throw new TestUserServiceException();
        super.upgradeLevel(user);
    }

    public List<User> getAll() {
        for (User user : super.getAll()) {
            super.update(user);
        }

        return null;
    }


    static class TestUserServiceException extends RuntimeException {

    }
}
