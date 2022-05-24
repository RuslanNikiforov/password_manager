package ruslan.password_manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ruslan.password_manager.entity.Role;
import ruslan.password_manager.entity.User;
import ruslan.password_manager.services.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SignUpController {

    @Autowired
    UserService service;

    @GetMapping("/signUp")
    public String showSignUpPage(Model model) {
        User newUser = new User();
        model.addAttribute("newUser", newUser);
        return "signUp";
    }

    @PostMapping("/signUp")
    public String registerUser(@Valid @ModelAttribute(name = "newUser") User user,
                               BindingResult bindingResult, Model model) {
        if(!user.getPassword().matches("^[A-Za-z0-9_^&@!-]+$")) {
            FieldError passwordError = new FieldError("passwordError", "password",
                    "Символы могут содежрать только буквы латинского алфавита \n" +
                    "[a-z, A-Z], цифры [0-9] и специальные символы _^&@!- .");
            bindingResult.addError(passwordError);
        }
        if(service.getAllWithAdmin().stream().map(User::getEmail).toList().contains(user.getEmail())) {
            FieldError emailError = new FieldError("emailError", "email",
                    "Пользователь с данным email уже существует.");
            bindingResult.addError(emailError);
        }
        if (bindingResult.hasErrors()) {
            List<FieldError> nameErrors = bindingResult.getFieldErrors("name");
            List<FieldError> emailErrors = bindingResult.getFieldErrors("email");
            List<FieldError> passwordErrors = bindingResult.getFieldErrors("password");
            if (!bindingResult.getFieldErrors("name").isEmpty()) {
                model.addAttribute("nameErrors", nameErrors.stream().
                        map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
            }
            if (!bindingResult.getFieldErrors("email").isEmpty()) {
                model.addAttribute("emailErrors", emailErrors.stream().
                        map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
            }
            if (!bindingResult.getFieldErrors("password").isEmpty()) {
                model.addAttribute("passwordErrors", passwordErrors.stream().
                        map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
            }
            return "signUp";
        }
        user.setRoles(Collections.singleton(Role.USER));
        service.saveUser(user);
        return "registrationSucceed";
    }
}
