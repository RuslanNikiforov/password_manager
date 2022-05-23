package ruslan.password_manager.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ruslan.password_manager.entity.ApplicationPassword;
import ruslan.password_manager.util.AssertApplicationPasswordHelper;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ruslan.password_manager.util.ApplicationPasswordTestData.*;

@SpringBootTest
@Sql(scripts = {"classpath:initDb.sql", "classpath:populateDb.sql"})
class ApplicationPasswordServiceImplTest {

    @Autowired
    ApplicationPasswordService service;

    @Test
    void getAll() {
        AssertApplicationPasswordHelper.isEqual(FIRST_USER_PASSWORDS, service.getAll(FIRST_USER_ID));
    }

    @Test
    void get() {
        AssertApplicationPasswordHelper.isEqual(APPLICATION_PASSWORD2, service.get(APPLICATION_PASSWORD2.getId()));
    }

    @Test
    void delete() {
        service.delete(APPLICATION_PASSWORD3.getId());
        assertThrows(NoSuchElementException.class, () -> service.get(APPLICATION_PASSWORD3.getId()));
    }

    @Test
    void create() {
        ApplicationPassword newApplicationPassword = getNew();
        ApplicationPassword created = service.save(getNew(), SECOND_USER_ID);
        newApplicationPassword.setId(created.getId());
        AssertApplicationPasswordHelper.isEqual(newApplicationPassword, created);
    }

    @Test
    void update() {
        ApplicationPassword existingApplicationPassword = getExisting();
        ApplicationPassword updatedApplicationPassword = service.save(existingApplicationPassword, FIRST_USER_ID);
        AssertApplicationPasswordHelper.isEqual(getExisting(), updatedApplicationPassword);

    }
}