package ruslan.password_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ruslan.password_manager.entity.ResetPasswordCode;

import java.time.LocalDateTime;

public interface ResetPasswordCodeRepository extends JpaRepository<ResetPasswordCode, Long> {

    ResetPasswordCode getByCode(String code);

    Integer countResetPasswordCodeByUserId(Long user_id);

    @Modifying
    @Query("update ResetPasswordCode code set code.code=?1, code.sentTime=?2 where code.user.id=?3")
    void setResetPasswordCodeToUser(String code, LocalDateTime sentTime, Long user_id);
}