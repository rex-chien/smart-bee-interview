package rex.chien.cms.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rex.chien.cms.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByAccount() {
        // arrange
        User user = new User();
        user.setAccount("account");
        entityManager.persist(user);

        // act
        Optional<User> byAccountUser = userRepository.findByAccount(user.getAccount());

        // assert
        assertTrue(byAccountUser.isPresent());
    }
}