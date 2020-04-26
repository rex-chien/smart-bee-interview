package rex.chien.cms.repository;

import org.springframework.data.repository.CrudRepository;
import rex.chien.cms.domain.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
