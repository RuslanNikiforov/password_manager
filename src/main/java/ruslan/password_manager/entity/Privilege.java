package ruslan.password_manager.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Privilege implements GrantedAuthority {
    ADMIN,
    USER,
    READ_APP_PASSWORDS,
    CHANGE_PASSWORD;

    @Override
    public String getAuthority() {
        return name();
    }
}
