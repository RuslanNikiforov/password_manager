package ruslan.password_manager.services;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ruslan.password_manager.entity.ApplicationPassword;

import java.util.List;

public interface ApplicationPasswordService {

    List<ApplicationPassword> getAll(long userId);

    ApplicationPassword get(long appPasswordId);

    void delete(long appPasswordId);

    ApplicationPassword save(ApplicationPassword applicationPassword, long userId);

    boolean addErrorsToModel(BindingResult bindingResult, Model model);

}
