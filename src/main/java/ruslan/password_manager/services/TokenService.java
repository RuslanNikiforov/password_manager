package ruslan.password_manager.services;

import java.time.LocalDateTime;

public interface TokenService {

    boolean userHasToken(Long user_id);

    void deleteToken(String token);

    void updateToken(String token, LocalDateTime sentTime, Long user_id);
}
