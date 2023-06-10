package ruslan.password_manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/koda/password-manager/")
    public String root() {
        return "index";
    }
}
