package ruslan.password_manager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ruslan.password_manager.entity.ApplicationPassword;
import ruslan.password_manager.repository.ApplicationPasswordRepository;
import ruslan.password_manager.repository.UserRepository;

import java.util.List;

@Service
public class ApplicationPasswordServiceImpl implements ApplicationPasswordService {


    private ApplicationPasswordRepository appPasswordRepository;

    private UserRepository userRepository;

    @Autowired
    public ApplicationPasswordServiceImpl(ApplicationPasswordRepository repository, UserRepository userRepository) {
        this.appPasswordRepository = repository;
        this.userRepository = userRepository;
    }

    public List<ApplicationPassword> getAll(long userId) {
        return appPasswordRepository.findAllByUser_IdOrderByLastModifiedDesc(userId);
    }

    @Override
    public ApplicationPassword get(long appPasswordId) {
        return appPasswordRepository.findById(appPasswordId).orElseThrow();
    }

    @Override
    public void delete(long appPasswordId) {
        appPasswordRepository.deleteById(appPasswordId);
    }

    @Override
    public ApplicationPassword save(ApplicationPassword applicationPassword, long userId) {
        applicationPassword.setUser(userRepository.getById(userId));
        return appPasswordRepository.save(applicationPassword);
    }

    @Override
    public boolean addErrorsToModel(BindingResult bindingResult, Model model) {
        if ((bindingResult.getFieldError("password") != null) || bindingResult.getFieldError("appName") != null) {
            if (bindingResult.getFieldError("password") != null) {
                model.addAttribute("passwordError", bindingResult.getFieldError("password").getDefaultMessage());
            }
            if (bindingResult.getFieldError("appName") != null) {
                model.addAttribute("appNameError", bindingResult.getFieldError("appName").getDefaultMessage());
            }
            return true;
        }
        return false;
    }
}
