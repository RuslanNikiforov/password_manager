package ruslan.password_manager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruslan.password_manager.entity.ResetPasswordCode;
import ruslan.password_manager.repository.ResetPasswordCodeRepository;

import java.time.LocalDateTime;

@Service
public class ResetPasswordCodeServiceImpl implements ResetPasswordCodeService{

    @Autowired
    private ResetPasswordCodeRepository repository;

    @Override
    public ResetPasswordCode getResetPasswordCode(String code) {
        return repository.getByCode(code);
    }

    public ResetPasswordCode saveResetPasswordCode(ResetPasswordCode code) {
        return repository.save(code);
    }

    @Override
    public boolean userHasToken(Long user_id) {
        return repository.countResetPasswordCodeByUserId(user_id) != 0;
    }

    @Override
    public void deleteToken(String code) {
        repository.delete(repository.getByCode(code));
    }

    @Transactional
    @Override
    public void updateToken(String code, LocalDateTime sentTime, Long user_id) {
        repository.setResetPasswordCodeToUser(code, sentTime, user_id);
    }
}
