package ruslan.password_manager.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ruslan.password_manager.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    User getById(long id);

    User getByEmail(String email);

    User saveUser(User user);

    List<User> getAll();

    void delete(Long id);

    List<User> filterAndGetAll(String min_id, String max_id, String name, String email);

    List<User> getAllWithAdmin();
}
