package rex.chien.cms.repository;

import org.springframework.data.repository.CrudRepository;
import rex.chien.cms.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByAccount(String account);
}
