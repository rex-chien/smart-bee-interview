package rex.chien.cms.repository;

import org.springframework.data.repository.CrudRepository;
import rex.chien.cms.domain.Company;

public interface CompanyRepository extends CrudRepository<Company, Long> {
}
