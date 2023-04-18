package ruslan.password_manager.services;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AppServiceImpl implements AppService {

    @Override
    public String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @Override
    public String getContextUrlForPasswordReset() {
        return "/forgetPassword/resetLink?code=";
    }
}
