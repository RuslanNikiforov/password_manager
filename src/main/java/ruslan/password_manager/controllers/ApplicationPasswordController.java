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
import ruslan.password_manager.services.*;

import javax.servlet.http.HttpServletResponse;
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

    private final PasswordGeneratorService passwordGeneratorService;


    @Autowired
    public ApplicationPasswordController(ApplicationPasswordService appPasswordService, EmailServiceImpl emailService,
                                         UsersSessionService usersSessionService,
                                         PasswordGeneratorService passwordGeneratorService) {
        this.appPasswordService = appPasswordService;
        this.emailService = emailService;
        this.usersSessionService = usersSessionService;
        this.passwordGeneratorService = passwordGeneratorService;
    }

    @GetMapping()
    public String getAll(Model model, @AuthenticationPrincipal User user,
                         @RequestParam(name = "genPassLength", required = false) String genPassLength,
                         @RequestParam(name = "appName", required = false, defaultValue = "") String appName,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "3") String pageSize,
                         @RequestParam(name = "currentPage", required = false, defaultValue = "1") String currentPage)
            throws UserPrincipalNotFoundException {
        if (user == null) {
            throw new UserPrincipalNotFoundException("UserPrincipalNotFound");
        }
        List<ApplicationPassword> applicationPasswordsList;
        if(user.isAbleToSeePasswords()) {
            applicationPasswordsList = appPasswordService.getAllAndSetDecryptedPassword(user.getId());
        }
        else {
            applicationPasswordsList = appPasswordService.getAllWithoutPassword(user.getId());
        }
        applicationPasswordsList = appPasswordService.getAllFilteredByAppName(applicationPasswordsList, appName);
        model.addAttribute("amount_of_pages",
                Math.ceil((double) applicationPasswordsList.size() / Integer.parseInt(pageSize)));
        applicationPasswordsList = appPasswordService.getAllOnThePage(applicationPasswordsList, pageSize, currentPage);
        model.addAttribute("app_passwords", applicationPasswordsList);
        model.addAttribute("isAbleToSeePasswords", user.isAbleToSeePasswords());
        model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
        model.addAttribute("username", user.getName());
        if(genPassLength != null) {
            String generatedPassword = passwordGeneratorService.generatePassayPassword(Integer.parseInt(genPassLength));
            model.addAttribute("generatedPassword", generatedPassword);
        }
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
    public String showUpdateForm(@AuthenticationPrincipal User user, @PathVariable String id, Model model) {
        ApplicationPassword applicationPassword;

        applicationPassword = appPasswordService.get(Long.parseLong(id));
        if(!user.isAbleToSeePasswords()) {
            applicationPassword.setPassword("");
        }
        else {
            applicationPassword.setPassword(WebSecurityConfig.stringEncryptor().
                    decrypt(applicationPassword.getPassword()));
        }
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
    public String checkToken(@ModelAttribute(name = "token") String token, @AuthenticationPrincipal User user,
                             Model model) {
        LOG.info("tokenGonnaBeChecked");
        try {
            if (usersSessionService.getUsersTokens().get(user.getId()).equals(token)) {
                LOG.info("Token is correct");
                user.setAbleToSeePasswords(true);
                return "redirect:/passwords";
            } else {
                LOG.info("Token incorrect. User Input Token -> " + token + " Actual token -> " +
                        usersSessionService.getUsersTokens().get(user.getId()));
                throw new IncorrectTokenException();
            }
        }
        catch (IncorrectTokenException e) {
            model.addAttribute("hasError", true);
            model.addAttribute("isTokenSent", true);
            return "popUpToSendTokenInEmail";
        }
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response, @AuthenticationPrincipal User user, Model model) {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=Application_Passwords" + ".xlsx");
        appPasswordService.exportToExcel(appPasswordService.getAllAndSetDecryptedPassword(user.getId()), response);
    }


}
