package ruslan.password_manager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "sent_time")
    protected LocalDateTime sentTime;

    private static int expirationTimeInMinutes = 60 * 24;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    protected User user;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(sentTime.plusMinutes(expirationTimeInMinutes));
    }


}
