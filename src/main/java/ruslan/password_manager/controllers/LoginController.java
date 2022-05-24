package ruslan.password_manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/failure-login")
    public String login(Model model) {
        model.addAttribute("wrongLoginOrPassword", "wrongData");
        return "login";
    }
}
