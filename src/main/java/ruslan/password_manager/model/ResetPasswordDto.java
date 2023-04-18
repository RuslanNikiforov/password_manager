package ruslan.password_manager.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ResetPasswordDto {

    private String code;

    @Pattern(regexp = "^[A-Za-z0-9_^&@!-$#.]+$", message = "Символы могут содежрать только буквы латинского алфавита \n" +
            "[a-z, A-Z], цифры [0-9] и специальные символы _^&@!-#$.")
    @Size(min = 8, max = 64, message = "Введенный пароль должен содержать от 8 до 64 символов.")
    @NotBlank(message = "Пароль не может быть пустым.")
    private String password;

    private String matchPassword;
}
