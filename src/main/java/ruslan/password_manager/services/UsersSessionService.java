package ruslan.password_manager.services;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class UsersSessionService {
    private final Map<Long, String> usersTokens;

    public UsersSessionService() {
        usersTokens = new HashMap<>();
    }

    public void putToken(long id, String token) {
        usersTokens.put(id, token);
    }

    public void deleteToken(long id) {
        usersTokens.remove(id);
    }
}
