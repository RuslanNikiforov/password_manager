package ruslan.password_manager.util;

import ruslan.password_manager.config.WebSecurityConfig;
import ruslan.password_manager.entity.ApplicationPassword;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationPasswordTestData {

    public final static String PASSWORD1;

    public final static String PASSWORD2;

    public final static String PASSWORD3;

    public final static String PASSWORD4;

    public final static String PASSWORD5;

    public final static String PASSWORD6;

    public final static long FIRST_USER_ID = 1;

    public final static long SECOND_USER_ID = 4;

    public final static ApplicationPassword APPLICATION_PASSWORD1 =
            new ApplicationPassword(1L, "Steam", "12345");

    public final static ApplicationPassword APPLICATION_PASSWORD2 =
            new ApplicationPassword(2L, "AliExpress", "qwerty");

    public final static ApplicationPassword APPLICATION_PASSWORD3 =
            new ApplicationPassword(3L, "Mail.ru", "qwerty_mail");

    public final static ApplicationPassword APPLICATION_PASSWORD4 =
            new ApplicationPassword(4L, "Google.com", "qwerty289");

    public final static ApplicationPassword APPLICATION_PASSWORD5 =
            new ApplicationPassword(5L, "test.com", "password");

    public final static List<ApplicationPassword> FIRST_USER_PASSWORDS = new ArrayList<>();

    public final static List<ApplicationPassword> SECOND_USER_PASSWORDS = new ArrayList<>();

    static {
        Collections.addAll(FIRST_USER_PASSWORDS, APPLICATION_PASSWORD1, APPLICATION_PASSWORD2);
        Collections.addAll(SECOND_USER_PASSWORDS, APPLICATION_PASSWORD3, APPLICATION_PASSWORD4, APPLICATION_PASSWORD5);
        PASSWORD1 = WebSecurityConfig.stringEncryptor().encrypt("12345");
        PASSWORD2 = WebSecurityConfig.stringEncryptor().encrypt("qwerty");
        PASSWORD3 = WebSecurityConfig.stringEncryptor().encrypt("qwerty_mail");
        PASSWORD4 = WebSecurityConfig.stringEncryptor().encrypt("qwerty289");
        PASSWORD5 = WebSecurityConfig.stringEncryptor().encrypt("password");
        PASSWORD6 = WebSecurityConfig.stringEncryptor().encrypt("12345");
    }

    public static ApplicationPassword getNew() {
        return new ApplicationPassword(null, "Yandex.ru", "123", LocalDateTime.now());
    }

    public static ApplicationPassword getExisting() {
        return new ApplicationPassword(3L, "ex.ru", "example", LocalDateTime.now());
    }

}
