package ruslan.password_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ruslan.password_manager.entity.AuthToken2FA;

import java.time.LocalDateTime;

public interface AuthToken2FARepository extends JpaRepository<AuthToken2FA, Long> {

    AuthToken2FA getByToken(String token);

    Integer countAuthTokensByUserId(Long user_id);

    @Modifying
    @Query("update AuthToken2FA token set token.token=?1, token.sentTime=?2 where token.user.id=?3")
    void setAuthTokenToUser(String token, LocalDateTime sentTime, Long user_id);

    AuthToken2FA getByUser_Id(Long userId);
}