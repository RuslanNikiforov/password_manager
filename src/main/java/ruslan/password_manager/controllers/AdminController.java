package ruslan.password_manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ruslan.password_manager.services.UserService;

@Controller
public class AdminController {

    @Autowired
    UserService service;

    @GetMapping("/admin")
    public String getAdminPage(Model model, @RequestParam(required = false, name = "min_id", defaultValue = "0") String min_id,
                               @RequestParam(required = false, name = "max_id", defaultValue = "0") String max_id,
                               @RequestParam(required = false, name = "name", defaultValue = "") String name,
                               @RequestParam(required = false, name = "email", defaultValue = "") String email)
    {
        if(min_id.equals("0") && max_id.equals("0") && name.equals("") && email.equals("")) {
            model.addAttribute("users", service.getAll());
        }
        else {
            model.addAttribute("users", service.filterAndGetAll(min_id, max_id, name, email));
            return "admin";
        }
        return "admin";
    }

    @GetMapping("/admin/{id}")
    public String deleteUser(@PathVariable String id) {
        service.delete(Long.parseLong(id));
        return "redirect:/admin";
    }
}
