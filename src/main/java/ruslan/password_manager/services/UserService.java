package ruslan.password_manager.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ruslan.password_manager.entity.Privilege;
import ruslan.password_manager.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService extends UserDetailsService {

    public Set<Privilege> addAuthorities(List<Privilege> newAuthorities,
                                                       Set<Privilege> userAuthorities);

    public boolean removeAuthorities(List<Privilege> toRemoveAuthorities,
                                         Set<Privilege> userAuthorities);

    User getById(long id);

    User getByEmail(String email);

    User saveUser(User user);

    User updateUser(User user);

    List<User> getAll();

    void delete(Long id);

    List<User> filterAndGetAll(String min_id, String max_id, String name, String email);

    List<User> getAllWithAdmin();

    Optional<User> getUserByPasswordResetCode(String code);

    boolean updatePassword(User user, String password);
}
