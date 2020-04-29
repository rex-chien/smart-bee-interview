package rex.chien.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rex.chien.cms.domain.Company;
import rex.chien.cms.domain.CompanyRequest;
import rex.chien.cms.repository.CompanyRepository;
import rex.chien.cms.security.JwtUser;
import rex.chien.cms.security.RoleName;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @RolesAllowed({RoleName.SUPER_USER, RoleName.MANAGER, RoleName.OPERATOR})
    public Iterable<Company> getAll() {
        return companyRepository.findAll();
    }

    @RequestMapping(value = "{companyId}", method = RequestMethod.POST)
    @RolesAllowed({RoleName.SUPER_USER, RoleName.MANAGER, RoleName.OPERATOR})
    public ResponseEntity<Company> get(@PathVariable long companyId) {
        Optional<Company> targetCompany = companyRepository.findById(companyId);

        return ResponseEntity.of(targetCompany);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @RolesAllowed({RoleName.SUPER_USER, RoleName.OPERATOR})
    public Company create(@RequestBody CompanyRequest companyRequest, Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Company company = new Company();
        company.setName(companyRequest.getName());
        company.setAddress(companyRequest.getAddress());
        company.setCreatedBy(jwtUser.getId());
        company.setCreatedAt(new Date());

        companyRepository.save(company);

        return company;
    }

    @RequestMapping(method = RequestMethod.POST)
    @RolesAllowed({RoleName.SUPER_USER, RoleName.MANAGER})
    public ResponseEntity<?> update(@RequestBody CompanyRequest companyRequest, Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Optional<Company> targetCompany = companyRepository.findById(companyRequest.getId());

        if (targetCompany.isPresent()) {
            Company company = targetCompany.get();
            company.setName(companyRequest.getName());
            company.setAddress(companyRequest.getAddress());
            company.setUpdatedBy(jwtUser.getId());
            company.setUpdatedAt(new Date());

            companyRepository.save(company);

            return ResponseEntity.ok(company);
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "{companyId}", method = RequestMethod.DELETE)
    @RolesAllowed({RoleName.SUPER_USER, RoleName.MANAGER})
    public ResponseEntity<?> delete(@PathVariable long companyId) {
        Optional<Company> targetCompany = companyRepository.findById(companyId);

        if (targetCompany.isPresent()) {
            Company company = targetCompany.get();

            companyRepository.delete(company);

            return ResponseEntity.ok(company);
        }

        return ResponseEntity.notFound().build();
    }
}
