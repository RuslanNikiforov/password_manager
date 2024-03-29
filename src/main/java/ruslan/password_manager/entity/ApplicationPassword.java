package ruslan.password_manager.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

@Entity
@Table(name = "app_passwords")
public class ApplicationPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Не может быть пустым")
    @Column(name = "app_name")
    private String appName;

    @NotNull
    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @NotBlank(message = "Не может быть пустым")
    @Column(name = "password")
    private String password;

    @Column(name = "url")
    private String url;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(table = "app_passwords", name = "user_id")
    @NotNull
    private User user;

    public String getFormattedLastModified() {
        return lastModified.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public ApplicationPassword(Long id, String appName, String password) {
        this.id = id;
        this.appName = appName;
        this.password = password;
    }

    public ApplicationPassword(Long id, String appName, String password, LocalDateTime localDateTime) {
        this.id = id;
        this.appName = appName;
        this.password = password;
        this.lastModified = localDateTime;
    }
}
