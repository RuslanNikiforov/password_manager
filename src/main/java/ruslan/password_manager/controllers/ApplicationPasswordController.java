package ruslan.password_manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ruslan.password_manager.config.WebSecurityConfig;
import ruslan.password_manager.entity.ApplicationPassword;
import ruslan.password_manager.entity.Role;
import ruslan.password_manager.entity.User;
import ruslan.password_manager.services.ApplicationPasswordService;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/passwords")
public class ApplicationPasswordController {

    private ApplicationPasswordService service;

    @Autowired
    public ApplicationPasswordController(ApplicationPasswordService service) {
        this.service = service;
    }

    @GetMapping()
    public String getAll(Model model, @AuthenticationPrincipal User user) throws UserPrincipalNotFoundException {

        if (user == null) {
            throw new UserPrincipalNotFoundException("UserPrincipalNotFound");
        }
        List<ApplicationPassword> applicationPasswordList = service.getAll(user.getId());
        applicationPasswordList.forEach(applicationPassword ->
                applicationPassword.setPassword(WebSecurityConfig.stringEncryptor().
                        decrypt(applicationPassword.getPassword())));
        model.addAttribute("app_passwords", applicationPasswordList);
        model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
        return "passwords";
    }

    @GetMapping("/delete/{id}")
    public String deleteAppPassword(@PathVariable String id) {
        service.delete(Long.parseLong(id));
        return "redirect:/passwords";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("app_password", new ApplicationPassword());
        return "updatePassword";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable String id, Model model) {
        ApplicationPassword applicationPassword = service.get(Long.parseLong(id));
        applicationPassword.setPassword(WebSecurityConfig.stringEncryptor().
                decrypt(applicationPassword.getPassword()));
        model.addAttribute("app_password", applicationPassword);
        return "updatePassword";
    }

    @PostMapping("")
    public String addAppPassword(@Valid @ModelAttribute(name = "app_password") ApplicationPassword applicationPassword,
                                 BindingResult bindingResult, @AuthenticationPrincipal User user, Model model) throws UserPrincipalNotFoundException {
        if (user == null) {
            throw new UserPrincipalNotFoundException("UserPrincipalNotFound");
        }
        if (service.getAll(user.getId()).stream().map(ApplicationPassword::getAppName).toList().
                contains(applicationPassword.getAppName())) {
            bindingResult.addError(new FieldError("appError", "appName",
                    "Приложение с данным названием уже существует"));
        }
        if (bindingResult.hasErrors() & service.addErrorsToModel(bindingResult, model)) {
            return "updatePassword";
        }
        applicationPassword.setLastModified(LocalDateTime.now());
        service.save(applicationPassword, user.getId());
        return "redirect:/passwords";
    }

    @PostMapping("/{id}")
    public String updateAppPassword(@PathVariable(value = "id") String id,
                                    @Valid @ModelAttribute(name = "app_password") ApplicationPassword applicationPassword,
                                    BindingResult bindingResult, @AuthenticationPrincipal User user, Model model)
            throws UserPrincipalNotFoundException {
        if (user == null) {
            throw new UserPrincipalNotFoundException("UserPrincipalNotFound");
        }
        if (bindingResult.hasErrors() & service.addErrorsToModel(bindingResult, model)) {
            return "updatePassword";
        }
        ApplicationPassword existing = service.get(Long.parseLong(id));
        existing.setPassword(applicationPassword.getPassword());
        existing.setAppName(applicationPassword.getAppName());
        existing.setLastModified(LocalDateTime.now());
        service.save(existing, user.getId());
        return "redirect:/passwords";
    }
}
