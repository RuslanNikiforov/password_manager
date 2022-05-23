package ruslan.password_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ruslan.password_manager.entity.ApplicationPassword;

import java.util.List;

@Repository
public interface ApplicationPasswordRepository extends JpaRepository<ApplicationPassword, Long> {

    List<ApplicationPassword> findAllByUser_IdOrderByLastModifiedDesc(long userId);


}
