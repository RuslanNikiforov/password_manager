package ruslan.password_manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorHandlerController {

    @GetMapping("/403")
    public String accessDenied() {
        return "accessDenied";
    }
}
