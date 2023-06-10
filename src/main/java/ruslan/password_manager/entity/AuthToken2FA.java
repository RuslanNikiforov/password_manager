package ruslan.password_manager.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "auth_token_2fa")
@Entity
public class AuthToken2FA extends AbstractToken{

    private String token;

    public AuthToken2FA(String token) {
        this.token = token;
        this.sentTime = LocalDateTime.now();
    }
}
