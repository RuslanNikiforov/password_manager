package ruslan.password_manager.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ruslan.password_manager.config.WebSecurityConfig;
import ruslan.password_manager.entity.Privilege;
import ruslan.password_manager.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserTestData {

    private final static Logger log = LoggerFactory.getLogger(UserTestData.class);
    public final static String user1_password;
    public final static String user2_password;
    public final static String user3_password;
    public final static String user4_password;
    public final static String user5_password;
    public final static User USER_1 = new User(2L, "Misha","example2@mail.com",
            "$2a$05$SAdwkv4GXf0jUuSPppNJz.eRILbyPd.fcis3IjlT.yfsVY9WWNhvK", Collections.singleton(Privilege.USER));
    public final static User USER_2 = new User(3L, "Ruslan","example3@mail.com",
            "$2a$05$I0yAgHFcjlJnmRb1dqLSxuv664NA9IhGIlZr5Eq9Ux57ge3n6sWHW", Collections.singleton(Privilege.USER));

    public final static User USER_3 = new User(4L, "Dasha","example4@mail.com",
            "$2a$05$2yfvxQzLW0vFzDtXBlFtROhD2ujvUrOMPBKgF5a9Xv6nYHiQlUanq", Collections.singleton(Privilege.USER));

    public final static User USER_4 = new User(5L, "Victor","example5@mail.com",
            "$2a$05$NTHHpxz3tvrnLThdcd9oyOa1IqKicnSj.Qx7a2wOZ63qlS4BJNfMy", Collections.singleton(Privilege.USER));

    public final static List<User> USERS = new ArrayList<>();

    public final static List<User> FILTERED_USERS = new ArrayList<>();



    static {
        Collections.addAll(FILTERED_USERS, USER_1, USER_3);
        Collections.addAll(USERS, USER_1, USER_2, USER_3, USER_4);
        user1_password = WebSecurityConfig.bCryptPasswordEncoder().encode("qwerty1");
        user2_password = WebSecurityConfig.bCryptPasswordEncoder().encode("qwerty123");
        user3_password = WebSecurityConfig.bCryptPasswordEncoder().encode("qwerty76");
        user4_password = WebSecurityConfig.bCryptPasswordEncoder().encode("qwerty5");
        user5_password = WebSecurityConfig.bCryptPasswordEncoder().encode("qwerty139");
    }

    public static User getNew() {
        User user = new User();
        user.setName("Anna");
        user.setPassword("test");
        user.setEmail("test@mail.ru");
        return user;
    }

    public static User getExisting() {
        User user = new User();
        user.setId(2L);
        user.setName("Anna");
        user.setPassword("test");
        user.setEmail("test@mail.ru");
        return user;
    }
}
