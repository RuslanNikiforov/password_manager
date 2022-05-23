package ruslan.password_manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ruslan.password_manager.util.UserTestData;

@SpringBootTest
class PasswordManagerApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(UserTestData.user1_password);
        System.out.println(UserTestData.user2_password);
        System.out.println(UserTestData.user3_password);
        System.out.println(UserTestData.user4_password);
        System.out.println(UserTestData.user5_password);

    }

}
