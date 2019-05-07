package local.mrjnk.messenger.service;

import local.mrjnk.messenger.domain.Role;
import local.mrjnk.messenger.domain.User;
import local.mrjnk.messenger.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    private EMailSender EMailSender;

    @Value("${spring.application.name}")
    private String appName;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDB = userRepo.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setActive(false);
        Role grantedRole = Role.USER;
        if (userRepo.findAll().isEmpty())
            grantedRole = Role.ADMIN;
        user.setRoles(new HashSet<>(Arrays.asList(grantedRole)));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n"+
                            "Welcome to "+appName+"\n"+
                            "Please visit next link: http://localhost/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            EMailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        userRepo.save(user);

        return true;
    }
}
