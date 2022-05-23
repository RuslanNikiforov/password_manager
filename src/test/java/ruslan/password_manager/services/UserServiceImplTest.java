package ruslan.password_manager.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ruslan.password_manager.entity.User;
import ruslan.password_manager.util.AssertUserHelper;
import ruslan.password_manager.util.UserTestData;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ruslan.password_manager.util.UserTestData.*;

@SpringBootTest
@Sql(scripts = {"classpath:initDb.sql", "classpath:populateDb.sql"})
//@ContextConfiguration(classes = {UserServiceImpl.class, UserRepository.class, WebSecurityConfig.class})
class UserServiceImplTest {

    @Autowired
    private UserService userService;



    @Test
    void getByEmail() {
        AssertUserHelper.isEqual(UserTestData.USER_1, userService.getByEmail("example2@mail.com"));
    }

    @Test
    void loadUserByUsername() {
        getByEmail();
    }

    @Test
    void createUser() {
        User newUser = getNew();
        User createdUser = userService.saveUser(newUser);
        newUser.setId(createdUser.getId());
        AssertUserHelper.isEqual(newUser, userService.getById(createdUser.getId()));
    }

    @Test
    void updateUser() {
        User existingUser = getExisting();
        existingUser.setEmail("new@mail.ru");
        existingUser.setName("Boris");
        User updatedUser = userService.saveUser(existingUser);
        AssertUserHelper.isEqual(existingUser, updatedUser);
    }

    @Test
    void getAll() {
        AssertUserHelper.isEqual(USERS, userService.getAll());
    }

    @Test
    void delete() {
        userService.delete(2L);
        assertThrows(NoSuchElementException.class, () -> userService.getById(2L));
    }

    @Test
    void filterAndGetAll() {
        AssertUserHelper.isEqual(FILTERED_USERS, userService.filterAndGetAll("0", "0", "sha", ""));
    }

    @Test
    void getById() {
        AssertUserHelper.isEqual(UserTestData.USER_1, userService.getById(2L));
    }
}