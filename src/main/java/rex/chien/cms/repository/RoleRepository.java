package rex.chien.cms.repository;

import org.springframework.data.repository.CrudRepository;
import rex.chien.cms.domain.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
