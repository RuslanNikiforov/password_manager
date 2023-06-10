package ruslan.password_manager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruslan.password_manager.entity.AuthToken2FA;
import ruslan.password_manager.repository.AuthToken2FARepository;

import java.time.LocalDateTime;

@Service
public class AuthToken2FAServiceImpl implements AuthToken2FAService{

    @Autowired
    AuthToken2FARepository token2FARepository;

    @Override
    public AuthToken2FA getAuthToken(String token) {
        return token2FARepository.getByToken(token);
    }

    @Override
    public AuthToken2FA saveAuthToken(AuthToken2FA token) {
        return token2FARepository.save(token);
    }

    @Override
    public AuthToken2FA getByUserId(Long userId) {
        return token2FARepository.getByUser_Id(userId);
    }

    @Override
    public boolean userHasToken(Long user_id) {
        return token2FARepository.countAuthTokensByUserId(user_id) != 0;
    }

    @Override
    public void deleteToken(String token) {
        token2FARepository.delete(token2FARepository.getByToken(token));
    }

    @Transactional
    @Override
    public void updateToken(String token, LocalDateTime sentTime, Long user_id) {
        token2FARepository.setAuthTokenToUser(token, sentTime, user_id);
    }
}
