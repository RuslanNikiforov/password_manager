package ruslan.password_manager.services;

import ruslan.password_manager.entity.ResetPasswordCode;

import java.time.LocalDateTime;

public interface ResetPasswordCodeService {

    ResetPasswordCode getResetPasswordCode(String code);

    ResetPasswordCode saveResetPasswordCode(ResetPasswordCode code);

    boolean userHasCode(Long user_id);

    void deleteResetPasswordCode(String code);

    void updateResetPasswordCode(String code, LocalDateTime sentTime, Long user_id);
}
