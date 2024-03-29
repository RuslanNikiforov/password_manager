package ruslan.password_manager.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_code_reset_password")
public class ResetPasswordCode extends AbstractToken{

    private String code;

    public ResetPasswordCode(String code) {
        this.code = code;
        this.sentTime = LocalDateTime.now();
    }
}
