package rex.chien.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rex.chien.cms.domain.Company;
import rex.chien.cms.domain.CompanyRequest;
import rex.chien.cms.repository.CompanyRepository;
import rex.chien.cms.security.JwtUser;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Company> getAll() {
        return companyRepository.findAll();
    }

    @RequestMapping(value = "{companyId}", method = RequestMethod.POST)
    public ResponseEntity<?> get(@PathVariable long companyId) {
        Optional<Company> targetCompany = companyRepository.findById(companyId);

        return ResponseEntity.of(targetCompany);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyRole('SUPER_USER', 'OPERATOR')")
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
    @PreAuthorize("hasAnyRole('SUPER_USER', 'MANAGER')")
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
    @PreAuthorize("hasAnyRole('SUPER_USER', 'MANAGER')")
    public ResponseEntity<?> delete(@PathVariable long companyId) {
        companyRepository.deleteById(companyId);

        return ResponseEntity.ok().build();
    }
}
