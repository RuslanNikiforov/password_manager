package ruslan.password_manager.services;

import javax.servlet.http.HttpServletRequest;

public interface AppService {

    String getAppUrl(HttpServletRequest request);

    String getContextUrlForPasswordReset();
}
