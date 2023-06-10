package ruslan.password_manager.services;


import ruslan.password_manager.entity.AuthToken2FA;

public interface AuthToken2FAService extends TokenService{

    AuthToken2FA getAuthToken(String token);

    AuthToken2FA saveAuthToken(AuthToken2FA token);

    AuthToken2FA getByUserId(Long userId);
}
