package ruslan.password_manager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ruslan.password_manager.config.WebSecurityConfig;
import ruslan.password_manager.entity.ApplicationPassword;
import ruslan.password_manager.entity.AuthToken2FA;
import ruslan.password_manager.entity.Privilege;
import ruslan.password_manager.entity.User;
import ruslan.password_manager.exceptions.IncorrectTokenException;
import ruslan.password_manager.services.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/passwords")
public class ApplicationPasswordController {

    private final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserService userService;

    private final ApplicationPasswordService appPasswordService;

    private final EmailService emailService;

    private final AuthToken2FAService authToken2FAService;

    private final PasswordGeneratorService passwordGeneratorService;


    @Autowired
    public ApplicationPasswordController(ApplicationPasswordService appPasswordService, EmailService emailService,
                                         PasswordGeneratorService passwordGeneratorService, UserService userService,
                                         AuthToken2FAService authToken2FAService) {
        this.appPasswordService = appPasswordService;
        this.emailService = emailService;
        this.authToken2FAService = authToken2FAService;
        this.passwordGeneratorService = passwordGeneratorService;
        this.userService = userService;
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
        if(user.hasAuthority(Privilege.READ_APP_PASSWORDS)) {
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
        if(genPassLength != null) {
            String generatedPassword = passwordGeneratorService.generatePassayPassword(Integer.parseInt(genPassLength));
            model.addAttribute("generatedPassword", generatedPassword);
        }
        return "passwords";
    }

    @GetMapping("/PopUp")
    public String getPopUp(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("isTokenSent", false);
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
        return "updateAppPassword";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@AuthenticationPrincipal User user, @PathVariable String id, Model model) {
        ApplicationPassword applicationPassword;

        applicationPassword = appPasswordService.get(Long.parseLong(id));
        if(!user.hasAuthority(Privilege.READ_APP_PASSWORDS)) {
            applicationPassword.setPassword("");
        }
        else {
            applicationPassword.setPassword(WebSecurityConfig.stringEncryptor().
                    decrypt(applicationPassword.getPassword()));
        }
        model.addAttribute("app_password", applicationPassword);
        return "updateAppPassword";
    }

    @PostMapping("/create")
    public String addAppPassword(@Valid @ModelAttribute(name = "app_password") ApplicationPassword applicationPassword,
                                 BindingResult bindingResult, @AuthenticationPrincipal User user, Model model) throws UserPrincipalNotFoundException {
        if (user == null) {
            throw new UserPrincipalNotFoundException("UserPrincipalNotFound");
        }
        if (appPasswordService.getAll(user.getId()).stream().map(ApplicationPassword::getAppName).collect(Collectors.toList()).
                contains(applicationPassword.getAppName())) {
            bindingResult.addError(new FieldError("appError", "appName",
                    "Приложение с данным названием уже существует"));
        }
        if (bindingResult.hasErrors() & appPasswordService.addErrorsToModel(bindingResult, model)) {
            return "updateAppPassword";
        }
        applicationPassword.setLastModified(LocalDateTime.now());
        appPasswordService.save(applicationPassword, user.getId());
        return "redirect:/passwords";
    }

    @PostMapping("/create/{id}")
    public String updateAppPassword(@PathVariable(value = "id") String id,
                                    @Valid @ModelAttribute(name = "app_password") ApplicationPassword applicationPassword,
                                    BindingResult bindingResult, @AuthenticationPrincipal User user, Model model)
            throws UserPrincipalNotFoundException {
        if (user == null) {
            throw new UserPrincipalNotFoundException("UserPrincipalNotFound");
        }
        if (bindingResult.hasErrors() & appPasswordService.addErrorsToModel(bindingResult, model)) {
            return "updateAppPassword";
        }
        ApplicationPassword existing = appPasswordService.get(Long.parseLong(id));
        existing.setPassword(applicationPassword.getPassword());
        existing.setAppName(applicationPassword.getAppName());
        existing.setLastModified(LocalDateTime.now());
        if(existing.getUrl() != null) {
            existing.setUrl(applicationPassword.getUrl());
        }
        appPasswordService.save(existing, user.getId());
        return "redirect:/passwords";
    }

    @GetMapping("/sendToken")
    public String sendTokenToEmail(@AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes) {
        LOG.info("tokenGonnaBeSend");
        String token = WebSecurityConfig.getAuthToken();
        AuthToken2FA authToken2FA = new AuthToken2FA(token);
        authToken2FA.setUser(user);
        if(authToken2FAService.userHasToken(user.getId())) {
            AuthToken2FA oldToken = authToken2FAService.getByUserId(user.getId());
            if(oldToken.getSentTime().plusMinutes(3).isAfter(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("error", "Токен был отправлен, " +
                        "запросить новый сможете через " + (180 - ChronoUnit.SECONDS.between
                        (oldToken.getSentTime(), LocalDateTime.now())) + "сек.");
                return "redirect:/passwords/#zatemnenie";
            }
            authToken2FAService.updateToken(token, authToken2FA.getSentTime(), user.getId());
        }
        else{
            authToken2FAService.saveAuthToken(authToken2FA);
        }
        emailService.sendEmail(user.getEmail(), "Ваш токен двухфакторной аутентификации", token);
        redirectAttributes.addFlashAttribute("message", "Ваш токен был успешно отправлен");
        return "redirect:/passwords/#zatemnenie";
    }

    @PostMapping("/checkToken")
    public String checkToken(@ModelAttribute(name = "token") String token, @AuthenticationPrincipal User user,
                             Model model, RedirectAttributes redirectAttributes) {
        LOG.info("tokenGonnaBeChecked");
        try {
            if (authToken2FAService.getByUserId(user.getId()).getToken().equals(token)) {
                LOG.info("Token is correct");
                user.setPrivileges(userService.addAuthorities(Collections.singletonList(Privilege.READ_APP_PASSWORDS),
                        user.getPrivileges()));
                userService.updateUser(user);
                Authentication newAuth = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
                        user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuth);
                return "redirect:/passwords";
            } else {
                LOG.info("Token incorrect. User Input Token -> " + token + " Actual token -> " +
                        authToken2FAService.getAuthToken(token));
                throw new IncorrectTokenException();
            }
        }
        catch (IncorrectTokenException e) {
            redirectAttributes.addFlashAttribute("error", "Неправильный токен");
            return "redirect:/passwords/#zatemnenie";
        }
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response, @AuthenticationPrincipal User user) {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=Application_Passwords" + ".xlsx");
        appPasswordService.exportToExcel(appPasswordService.getAllAndSetDecryptedPassword(user.getId()), response);
    }


}
