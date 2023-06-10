package ruslan.password_manager.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ruslan.password_manager.config.WebSecurityConfig;
import ruslan.password_manager.entity.ApplicationPassword;
import ruslan.password_manager.repository.ApplicationPasswordRepository;
import ruslan.password_manager.repository.UserRepository;
import ruslan.password_manager.util.ApplicationPasswordsExcelExport;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationPasswordServiceImpl implements ApplicationPasswordService {

    private final static Logger LOG = LoggerFactory.getLogger(ApplicationPasswordServiceImpl.class);

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
    public List<ApplicationPassword> getAllWithoutPassword(long userId) {
        var list = appPasswordRepository.findAllByUser_IdOrderByLastModifiedDesc(userId);
        list.forEach(applicationPassword -> applicationPassword.setPassword(""));
        return list;
    }

    @Override
    public List<ApplicationPassword> getAllFilteredByAppName(List<ApplicationPassword> appPasswords, String appName) {
        var filteredPasswords = appPasswords.stream();
        if (!appName.equals("")) {
             filteredPasswords = filteredPasswords.filter(applicationPassword ->
                    applicationPassword.getAppName().toUpperCase().contains(appName.toUpperCase()));
        }
        return filteredPasswords.collect(Collectors.toList());
    }

    @Override
    public List<ApplicationPassword> getAllOnThePage(List<ApplicationPassword> appPasswords,
                                                     String pageSize, String currentPage) {
        int pageSizeNumber = Integer.parseInt(pageSize);
        int currentPageNumber = Integer.parseInt(currentPage);
        return appPasswords.stream().skip((long) (currentPageNumber - 1) * pageSizeNumber).
                limit(pageSizeNumber).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationPassword> getAllAndSetDecryptedPassword(long userId) {
        List<ApplicationPassword> applicationPasswordsList = getAll(userId);
        applicationPasswordsList.forEach(applicationPassword ->
                applicationPassword.setPassword(WebSecurityConfig.stringEncryptor().
                        decrypt(applicationPassword.getPassword())));
        return applicationPasswordsList;
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
        applicationPassword.setPassword(WebSecurityConfig.stringEncryptor().encrypt(applicationPassword.getPassword()));
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

    @Override
    public void exportToExcel(List<ApplicationPassword> applicationPasswords, HttpServletResponse response) {
        ApplicationPasswordsExcelExport excelExport = new ApplicationPasswordsExcelExport(applicationPasswords);
        excelExport.export(response);
    }

    @Override
    public ApplicationPassword getByUrl(String serverPath, long user_id) {
        try {
            var appPassword = appPasswordRepository.findAllByUser_IdOrderByLastModifiedDesc(user_id).
                    stream().filter(applicationPassword -> applicationPassword.getUrl() != null && applicationPassword.getUrl().
                            contains(serverPath)).collect(Collectors.toList()).get(0);
            appPassword.setPassword(WebSecurityConfig.stringEncryptor().decrypt(appPassword.getPassword()));
            LOG.info("serverName = " + serverPath);
            return appPassword;
        }
        catch (Exception e) {
            return null;
        }
    }
}
