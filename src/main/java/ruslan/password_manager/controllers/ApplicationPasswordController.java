package ruslan.password_manager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ruslan.password_manager.exceptions.IncorrectTokenException;
import ruslan.password_manager.services.ApplicationPasswordService;
import ruslan.password_manager.services.EmailServiceImpl;
import ruslan.password_manager.services.UserServiceImpl;
import ruslan.password_manager.services.UsersSessionService;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/passwords")
public class ApplicationPasswordController {

    private final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final ApplicationPasswordService appPasswordService;

    private final EmailServiceImpl emailService;

    private final UsersSessionService usersSessionService;


    @Autowired
    public ApplicationPasswordController(ApplicationPasswordService appPasswordService, EmailServiceImpl emailService,
                                         UsersSessionService usersSessionService) {
        this.appPasswordService = appPasswordService;
        this.emailService = emailService;
        this.usersSessionService = usersSessionService;
    }

    @GetMapping()
    public String getAll(Model model, @AuthenticationPrincipal User user) throws UserPrincipalNotFoundException {
        if (user == null) {
            throw new UserPrincipalNotFoundException("UserPrincipalNotFound");
        }
        List<ApplicationPassword> applicationPasswordList = appPasswordService.getAll(user.getId());
        applicationPasswordList.forEach(applicationPassword ->
                applicationPassword.setPassword(WebSecurityConfig.stringEncryptor().
                        decrypt(applicationPassword.getPassword())));
        model.addAttribute("app_passwords", applicationPasswordList);
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
        return "passwords";
    }

    @GetMapping("/PopUp")
    public String getPopUp(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("isTokenSent", usersSessionService.getUsersTokens().containsKey(user.getId()));
        return "popUpToSendTokenInEmail";
    }


    @GetMapping("/delete/{id}")
    public String deleteAppPassword(@PathVariable String id) {
        appPasswordService.delete(Long.parseLong(id));
        return "redirect:/passwords";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("app_password", new ApplicationPassword());
        return "updatePassword";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable String id, Model model) {
        ApplicationPassword applicationPassword = appPasswordService.get(Long.parseLong(id));
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
        if (appPasswordService.getAll(user.getId()).stream().map(ApplicationPassword::getAppName).toList().
                contains(applicationPassword.getAppName())) {
            bindingResult.addError(new FieldError("appError", "appName",
                    "Приложение с данным названием уже существует"));
        }
        if (bindingResult.hasErrors() & appPasswordService.addErrorsToModel(bindingResult, model)) {
            return "updatePassword";
        }
        applicationPassword.setLastModified(LocalDateTime.now());
        appPasswordService.save(applicationPassword, user.getId());
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
        if (bindingResult.hasErrors() & appPasswordService.addErrorsToModel(bindingResult, model)) {
            return "updatePassword";
        }
        ApplicationPassword existing = appPasswordService.get(Long.parseLong(id));
        existing.setPassword(applicationPassword.getPassword());
        existing.setAppName(applicationPassword.getAppName());
        existing.setLastModified(LocalDateTime.now());
        appPasswordService.save(existing, user.getId());
        return "redirect:/passwords";
    }

    @GetMapping("/sendToken")
    public String sendTokenToEmail(@AuthenticationPrincipal User user, Model model) {
        LOG.info("tokenGonnaBeSend");
        String token = WebSecurityConfig.getAuthToken();
        emailService.sendEmail(user.getEmail(), "Your auth token", token);
        usersSessionService.putToken(user.getId(), token);
        model.addAttribute("isTokenSent", true);
        return "popUpToSendTokenInEmail";
    }
    @PostMapping("/checkToken")
    public String checkToken(@ModelAttribute(name = "token") String token, @AuthenticationPrincipal User user) {
        LOG.info("tokenGonnaBeChecked");
        if(usersSessionService.getUsersTokens().get(user.getId()).equals(token)) {
            LOG.info("Token is correct");
            user.setAbleToSeePasswords(true);
            return "redirect:/passwords";
        }
        else {
            LOG.info("Token incorrect. User Input Token -> " + token + " Actual token -> " +
                    usersSessionService.getUsersTokens().get(user.getId()));
            throw new IncorrectTokenException();
        }
    }

}
