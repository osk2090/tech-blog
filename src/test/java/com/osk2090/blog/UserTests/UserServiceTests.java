package com.osk2090.blog.UserTests;

import com.osk2090.blog.user.dao.UserDao;
import com.osk2090.blog.user.domain.Level;
import com.osk2090.blog.user.domain.User;
import com.osk2090.blog.user.service.TestUserService;
import com.osk2090.blog.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    TestUserService testUserService;

    @Autowired
    private UserDao userDao;
    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    List<User> users;	// test fixture

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", "user1@ksug.org", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER-1, 0),
                new User("joytouch", "강명성", "p2", "user2@ksug.org", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", "user3@ksug.org", Level.SILVER, 60, UserServiceImpl.MIN_RECCOMEND_FOR_GOLD-1),
                new User("madnite1", "이상호", "p4", "user4@ksug.org", Level.SILVER, 60, UserServiceImpl.MIN_RECCOMEND_FOR_GOLD),
                new User("green", "오민규", "p5", "user5@ksug.org", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    /*@BeforeEach()
    public void setUp() {
        this.user1 = new User("gyumee", "박성철", "springno1", "user1@ksug.org", Level.BASIC, 1, 0);
        this.user2 = new User("leegw700", "이길원", "springno2", "user2@ksug.org", Level.SILVER, 55, 10);
        this.user3 = new User("bumjin", "박범진", "springno3", "user3@ksug.org", Level.GOLD, 100, 40);
    }*/

    @Test
    public void andAndGet() {
        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount()).isEqualTo(2);

        User userget1 = userDao.get(user1.getId());
        checkSameUser(userget1, user1);

        User userget2 = userDao.get(user2.getId());
        checkSameUser(userget2, user2);
    }

    @Test()
    public void getUserFailure() throws SQLException {
        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        assertThrows(DuplicateKeyException.class, () -> {
            userDao.get("unknown_id");
        });

    }


    @Test
    public void count() {
        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(user1);
        assertThat(userDao.getCount()).isEqualTo(1);

        userDao.add(user2);
        assertThat(userDao.getCount()).isEqualTo(2);

        userDao.add(user3);
        assertThat(userDao.getCount()).isEqualTo(3);
    }

    @Test
    public void getAll()  {
        userDao.deleteAll();

        List<User> users0 = userDao.getAll();
        assertThat(users0.size()).isEqualTo(0);

        userDao.add(user1); // Id: gyumee
        List<User> users1 = userDao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(user1, users1.get(0));

        userDao.add(user2); // Id: leegw700
        List<User> users2 = userDao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        userDao.add(user3); // Id: bumjin
        List<User> users3 = userDao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }

    @Test()
    public void duplciateKey() {
        assertThrows(DuplicateKeyException.class, () -> {
            userDao.deleteAll();

            userDao.add(user1);
            userDao.add(user1);
        });

    }

    @Test
    public void sqlExceptionTranslate() {
        userDao.deleteAll();

        try {
            userDao.add(user1);
            userDao.add(user1);
        }
        catch(DuplicateKeyException ex) {
            SQLException sqlEx = (SQLException)ex.getCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            DataAccessException transEx = set.translate(null, null, sqlEx);
            assertThat(transEx).isEqualTo(DuplicateKeyException.class);
        }
    }

    @Test
    public void update() {
        userDao.deleteAll();

        userDao.add(user1);		// 수정할 사용자
        userDao.add(user2);		// 수정하지 않을 사용자

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setEmail("user6@ksug.org");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);

        userDao.update(user1);

        User user1update = userDao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2same = userDao.get(user2.getId());
        checkSameUser(user2, user2same);
    }

    @Test
    public void readOnlyTransactionAttribute() {
        assertThrows(UncategorizedSQLException.class, () -> {
            testUserService.getAll();
        });
    }

    @Nested
    @SpringBootTest
    class TransactionTests {

        @Autowired
        PlatformTransactionManager transactionManager;

        @Test
        public void transactionSyncThreeTransactions() {

            // 이렇게 하면 로직마다 트랜잭션이 발생하기 때문에 총 3개의 트랜잭션이 생긴다.
            userServiceImpl.deleteAll();

            userServiceImpl.add(users.get(0));
            userServiceImpl.add(users.get(1));
        }

        @Test
        public void transactionSyncPackage() {
            DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
            txDefinition.setReadOnly(true);// 읽기 전용 트랜잭션으로 명시

            TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

            userServiceImpl.deleteAll(); // 이렇게 하면 읽기전용 트랜잭션 옵션을 무시하므로 예외 발생

            userServiceImpl.add(users.get(0));
            userServiceImpl.add(users.get(1));
            transactionManager.commit(txStatus);

        }

        @Test
        public void transactionSyncPackageAndRollback() {
            userServiceImpl.deleteAll();
            assertThat(userDao.getCount()).isEqualTo(0);
            // 위의 코드는 초기화를 했는지 확인하는 로직 추가

            DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
            TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

            userServiceImpl.add(users.get(0));
            userServiceImpl.add(users.get(1));
            // 위의 코드가 제대로 동작해서 두개의 로우가 들어갔는지 확인
            assertThat(userDao.getCount()).isEqualTo(2);

            // 강제로 롤백한다.
            transactionManager.rollback(txStatus);

            // 결국엔 원상태로 돌아갔는지 확인하는 로직
            assertThat(userDao.getCount()).isEqualTo(0);
        }
    }
}
