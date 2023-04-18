package ruslan.password_manager.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ruslan.password_manager.entity.Privilege;
import ruslan.password_manager.entity.User;
import ruslan.password_manager.repository.ResetPasswordCodeRepository;
import ruslan.password_manager.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService{

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository repository;

    @Autowired
    private ResetPasswordCodeRepository codeRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public Set<Privilege> addAuthorities(List<Privilege> newAuthorities,
                                                Set<Privilege> userAuthorities) {
        userAuthorities.addAll(newAuthorities);
        return userAuthorities;
    }

    public boolean removeAuthorities(List<Privilege> toRemoveAuthorities,
                                            Set<Privilege> userAuthorities) {
        int userAuthoritiesSize = userAuthorities.size();
        toRemoveAuthorities.forEach(userAuthorities::remove);
        return userAuthoritiesSize != userAuthorities.size();
    }

    public User getById(long id) {
        return repository.findById(id).orElseThrow();
    }

    public User getByEmail(String email) {
        User user = repository.findUserByEmail(email);
        if (user != null) {
            return user;
        }
        else {
            log.info("User -> UserDetailsService returned null");
            throw new UsernameNotFoundException("user == null");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByEmail(username);
    }


    public User saveUser(User user){
        return repository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll().stream().
                filter(user -> user.getAuthorities().contains(Privilege.USER) &&
                        !user.getAuthorities().contains(Privilege.ADMIN)).collect(Collectors.toList());
    }

    public List<User> getAllWithAdmin() {
        return repository.findAll();
    }

    @Override
    public Optional<User> getUserByPasswordResetCode(String code) {
        return Optional.ofNullable(codeRepository.getByCode(code).getUser());
    }

    @Override
    public boolean updatePassword(User user, String password) {
        if(user == null || password == null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(password));
        return true;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<User> filterAndGetAll(String min_id, String max_id, String name, String email) {
        /*long current_id;
        String current_email, current_name;

        List<User> filteredUsers = new ArrayList<>();
        for(User user : getAll()) {
            current_id = user.getId();
            current_name = user.getName();
            current_email = user.getEmail();
            if(current_id >= Long.parseLong(min_id) && current_id <= Long.parseLong(max_id) && current_name.contains(name)
                    && current_email.contains(email)) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
         */
        List<User> filteredUsers = getAll();
        if (!min_id.equals("0")) {
            filteredUsers = filteredUsers.stream().filter(user -> user.getId() >= Long.parseLong(min_id)).
                    collect(Collectors.toList());
        }
        if (!max_id.equals("0")) {
            filteredUsers = filteredUsers.stream().filter(user -> user.getId() <= Long.parseLong(max_id)).
                    collect(Collectors.toList());
        }
        if(!"".equals(name)) {
            filteredUsers = filteredUsers.stream().filter(user -> user.getName().contains(name)).
                    collect(Collectors.toList());
        }
        if (!"".equals(email)) {
            filteredUsers = filteredUsers.stream().filter(user -> user.getEmail().contains(email)).
                    collect(Collectors.toList());
        }

        return filteredUsers;
    }


}
