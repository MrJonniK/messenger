package local.mrjnk.messenger.repos;

import local.mrjnk.messenger.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);


}
