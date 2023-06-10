package ruslan.password_manager.services;

import ruslan.password_manager.entity.ResetPasswordCode;

public interface ResetPasswordCodeService extends TokenService{

    ResetPasswordCode getResetPasswordCode(String code);

    ResetPasswordCode saveResetPasswordCode(ResetPasswordCode code);


}
