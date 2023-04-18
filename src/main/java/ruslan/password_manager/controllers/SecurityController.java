package ruslan.password_manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ruslan.password_manager.config.WebSecurityConfig;
import ruslan.password_manager.entity.Privilege;
import ruslan.password_manager.entity.ResetPasswordCode;
import ruslan.password_manager.entity.User;
import ruslan.password_manager.model.ResetPasswordDto;
import ruslan.password_manager.services.AppService;
import ruslan.password_manager.services.EmailService;
import ruslan.password_manager.services.ResetPasswordCodeService;
import ruslan.password_manager.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SecurityController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResetPasswordCodeService passwordCodeService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppService appService;


    @GetMapping("/forgetPassword")
    public String getForgetPasswordPage() {
        return "forgetPassword";
    }


    @PostMapping("/forgetPassword/sendCode")
    public String sendCode(HttpServletRequest request, Model model, @ModelAttribute(name = "email") String email,
                           RedirectAttributes redirectAttributes){
        String code = WebSecurityConfig.generateSecretKey();
        ResetPasswordCode resetPasswordCode = new ResetPasswordCode(code);
        try{
        User recoveryPasswordUser = userService.getByEmail(email);

            userService.addAuthorities(Collections.singletonList(Privilege.CHANGE_PASSWORD),
                    recoveryPasswordUser.getPrivileges());
            userService.updateUser(recoveryPasswordUser);
            long recoveryPasswordUserId = recoveryPasswordUser.getId();
            resetPasswordCode.setUser(recoveryPasswordUser);
            if (passwordCodeService.userHasCode(recoveryPasswordUserId)) {
                passwordCodeService.updateResetPasswordCode(code, resetPasswordCode.getSentTime(),
                        recoveryPasswordUserId);
            } else {
                passwordCodeService.saveResetPasswordCode(resetPasswordCode);
            }
            emailService.sendEmail(email, "Your reset password link", "Your reset password link" + "\r\n" +
                    appService.getAppUrl(request) + appService.getContextUrlForPasswordReset() +
                    resetPasswordCode.getCode());
            redirectAttributes.addFlashAttribute("message", "Ссылка для сброса пароля успешно доставлена на email");
            return "redirect:/forgetPassword";
        }
        catch (UsernameNotFoundException e){
            redirectAttributes.addFlashAttribute("error", "Пользователь с веденным email не найден");
            return "redirect:/forgetPassword";
        }
    }

    @GetMapping("/forgetPassword/resetLink")
    public String getResetPasswordPage(@RequestParam(name = "code") String code, RedirectAttributes redirectAttributes,
                                       Model model) {
        ResetPasswordCode resetPasswordCode = passwordCodeService.getResetPasswordCode(code);
        if(resetPasswordCode != null && !resetPasswordCode.isExpired()) {
            User user = resetPasswordCode.getUser();
            Authentication authentication = new UsernamePasswordAuthenticationToken(user,
                    null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "resetPassword";

        }
        else if(resetPasswordCode == null) {
            redirectAttributes.addFlashAttribute("message", "Ссылка для сброса пароля недействительна");
            return "redirect:/login";
        }
        else {
            redirectAttributes.addFlashAttribute("message", "Ссылка для сброса пароля устарела");
            return "redirect:/login";
        }
    }

    @PreAuthorize("hasAuthority('CHANGE_PASSWORD')")
    @PostMapping("/forgetPassword/savePassword")
    public String savePassword(@AuthenticationPrincipal User user, @Valid ResetPasswordDto passwordResetDto,
                               BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldErrors("password").
                    stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
            return "redirect:/forgetPassword/resetLink?code=" + passwordResetDto.getCode();
        }
        if(!passwordResetDto.getPassword().equals(passwordResetDto.getMatchPassword())) {
            redirectAttributes.addFlashAttribute("errors", List.of("Пароли не совпадают"));
            return "redirect:/forgetPassword/resetLink?code=" + passwordResetDto.getCode();
        }

        if(user != null) {
            userService.updatePassword(user, passwordResetDto.getPassword());
            userService.saveUser(user);
            passwordCodeService.deleteResetPasswordCode(passwordResetDto.getCode());
            redirectAttributes.addFlashAttribute("message", "Пароль успешно изменён");
            return "redirect:/login";
        }
        return "redirect:/";
    }


}
