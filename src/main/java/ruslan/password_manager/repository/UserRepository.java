package ruslan.password_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruslan.password_manager.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findUserByEmail(String email);
}
