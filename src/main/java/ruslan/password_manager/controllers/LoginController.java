package ruslan.password_manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ruslan.password_manager.entity.AuthToken2FA;
import ruslan.password_manager.entity.Privilege;
import ruslan.password_manager.entity.User;
import ruslan.password_manager.services.AuthToken2FAService;
import ruslan.password_manager.services.UserService;

import java.util.Collections;

@Controller
public class LoginController {

    @Autowired
    AuthToken2FAService service;

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/failure-login")
    public String failureLogin(Model model) {
        model.addAttribute("wrongLoginOrPassword", "wrongData");
        return "login";
    }

    @GetMapping("/successful_Login")
    public String successfulLogin(@AuthenticationPrincipal User user) {
        AuthToken2FA token = service.getByUserId(user.getId());
        if(token != null && token.isExpired()) {
            service.deleteToken(token.getToken());
            if (userService.removeAuthorities(Collections.singletonList(Privilege.READ_APP_PASSWORDS),
                    user.getPrivileges())) {
                userService.updateUser(user);
            }
        }
        return "redirect:/passwords";
    }

}
