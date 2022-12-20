package ruslan.password_manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ruslan.password_manager.entity.User;
import ruslan.password_manager.services.UsersSessionService;

@Controller
public class LoginController {

    @Autowired
    UsersSessionService usersSessionService;

    @GetMapping("/failure-login")
    public String failureLogin(Model model) {
        model.addAttribute("wrongLoginOrPassword", "wrongData");
        return "login";
    }

    @GetMapping("/successful_Login")
    public String successfulLogin(@AuthenticationPrincipal User user) {
        if (user != null) {
            usersSessionService.getUsersTokens().remove(user.getId());
            user.setAbleToSeePasswords(false);
            return "redirect:/passwords";
        }
        return "redirect:/failure-login";
    }

}
