package ruslan.password_manager.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ruslan.password_manager.entity.User;

@Controller
@RequestMapping("/settings")
public class UserSettingsController {

    @GetMapping("/info")
    public String getUserInfoSettings(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getName());
        return "personalSettings";
    }

}
