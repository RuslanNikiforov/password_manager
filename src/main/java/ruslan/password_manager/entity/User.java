package ruslan.password_manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
//@SecondaryTable(name = "user_roles", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id"))
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не может быть пустым.")
    @Column(name = "name")
    @Size(min = 2, max = 32, message = "Введенное имя должно быть в пределах 2-32 символов.")
    private String name;


    @Email(message = "Невалидный email.")
    @NotBlank(message = "Email не может быть пустым.")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым.")
    @Size(min = 8, max = 64, message = "Введенный пароль должен содержать от 8 до 64 символов.")
    @Column(name = "password")
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles" , joinColumns = {@JoinColumn(name = "user_id")})
    @Column(table = "user_roles", name = "role")
    private Set<Privilege> privileges = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<ApplicationPassword> passwords;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return privileges;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(Long id, String name, String email, String password, Set<Privilege> privileges) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.privileges = privileges;
    }

    public boolean hasAuthority(GrantedAuthority authority) {
        return getAuthorities().contains(authority);
    }

}
