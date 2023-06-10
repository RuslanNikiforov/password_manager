package ruslan.password_manager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import ruslan.password_manager.entity.ApplicationPassword;
import ruslan.password_manager.entity.Privilege;
import ruslan.password_manager.entity.User;
import ruslan.password_manager.model.ResponseToUser;
import ruslan.password_manager.services.AppServiceImpl;
import ruslan.password_manager.services.ApplicationPasswordService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;


@org.springframework.web.bind.annotation.RestController

public class RestController {

    @Autowired
    private ApplicationPasswordService applicationPasswordService;

    private final static Logger LOG = LoggerFactory.getLogger(RestController.class);

    @CrossOrigin
    @GetMapping("/rest/passwords")
    public ResponseEntity<List<ApplicationPassword>> getAllPasswords(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(applicationPasswordService.getAll(user.getId()));
    }

    @CrossOrigin
    @GetMapping("/rest/password")
    public ResponseEntity<ApplicationPassword> getByUrl(
                                                        @AuthenticationPrincipal User user, HttpServletRequest request) {
        String originUrl = request.getHeader("origin");
        if(originUrl == null) {
            originUrl = AppServiceImpl.serverName;
        }
        String originHost = URI.create(originUrl).getHost();
        if(originHost.contains("www.")) {
            originHost.replace("www.", "");
        }
        var appPassword = applicationPasswordService.
                getByUrl(originHost, user.getId());
        if(appPassword != null) {
            return ResponseEntity.status(200).body(appPassword);
        }
        else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @CrossOrigin
    @GetMapping("/rest/verify")
    public ResponseEntity<ResponseToUser> verify(@AuthenticationPrincipal User user) {
        if(user.getAuthorities().contains(Privilege.READ_APP_PASSWORDS)) {
            LOG.info("user verified");
            return ResponseEntity.status(200).body(ResponseToUser.builder().username(user.getName()).
                    message("Успешно").hasErrors(false).build());
        }
        else {
            LOG.info("user has to pass 2fa auth");
            return ResponseEntity.status(200).body(ResponseToUser.builder().username(user.getName()).
                    message("Ошибка, необходимо пройти двухфакторную аутентификацию в приложении").hasErrors(true).build());
        }
    }

}
