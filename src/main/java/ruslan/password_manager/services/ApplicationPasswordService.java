package ruslan.password_manager.services;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ruslan.password_manager.entity.ApplicationPassword;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ApplicationPasswordService {

    List<ApplicationPassword> getAll(long userId);

    List<ApplicationPassword> getAllWithoutPassword(long userId);

    List<ApplicationPassword> getAllFilteredByAppName(List<ApplicationPassword> appPasswords, String appName);

    public List<ApplicationPassword> getAllOnThePage(List<ApplicationPassword> appPasswords,
                                                     String pageSize, String currentPage);

    List<ApplicationPassword> getAllAndSetDecryptedPassword(long userId);

    ApplicationPassword get(long appPasswordId);

    void delete(long appPasswordId);

    ApplicationPassword save(ApplicationPassword applicationPassword, long userId);

    boolean addErrorsToModel(BindingResult bindingResult, Model model);

    void exportToExcel(List<ApplicationPassword> applicationPasswords, HttpServletResponse response);
}
