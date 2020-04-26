package rex.chien.cms;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rex.chien.cms.domain.Client;
import rex.chien.cms.domain.Company;
import rex.chien.cms.domain.Role;
import rex.chien.cms.domain.User;
import rex.chien.cms.repository.ClientRepository;
import rex.chien.cms.repository.CompanyRepository;
import rex.chien.cms.repository.RoleRepository;
import rex.chien.cms.repository.UserRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class InitialDataLoader implements ApplicationRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final ClientRepository clientRepository;

    public InitialDataLoader(UserRepository userRepository, RoleRepository roleRepository, CompanyRepository companyRepository, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Role roleSuperUser = new Role("ROLE_SUPER_USER");
        Role roleManager = new Role("ROLE_MANAGER");
        Role roleOperator = new Role("ROLE_OPERATOR");

        roleRepository.save(roleSuperUser);
        roleRepository.save(roleManager);
        roleRepository.save(roleOperator);

        User administratorUser = new User("administrator", passwordEncoder.encode("123456"), Collections.singletonList(roleSuperUser));
        userRepository.save(administratorUser);
        userRepository.save(new User("manager", passwordEncoder.encode("123456"), Collections.singletonList(roleManager)));
        userRepository.save(new User("operator", passwordEncoder.encode("123456"), Collections.singletonList(roleOperator)));

        List<Company> fakeCompanies = fakeCompanies(administratorUser.getId());
        companyRepository.saveAll(fakeCompanies);

        List<Client> fakeClients = fakeCompanies.stream()
                .flatMap(company -> fakeClients(company, administratorUser.getId()).stream())
                .collect(Collectors.toList());
        clientRepository.saveAll(fakeClients);
    }

    private List<Company> fakeCompanies(long createdBy) {
        Faker faker = new Faker();

        return IntStream.range(0, 5)
                .mapToObj(i -> {
                    Company company = new Company();
                    company.setName(faker.company().name());
                    company.setAddress(faker.address().fullAddress());
                    company.setCreatedBy(createdBy);
                    company.setCreatedAt(new Date());
                    return company;
                }).collect(Collectors.toList());
    }

    private List<Client> fakeClients(Company company, long createdBy) {
        Faker faker = new Faker();

        return IntStream.range(0, 10)
                .mapToObj(i -> {
                    Client client = new Client();
                    client.setName(faker.name().fullName());
                    client.setPhone(faker.phoneNumber().cellPhone());
                    client.setEmail(faker.internet().emailAddress());
                    client.setCompany(company);
                    client.setCreatedBy(createdBy);
                    client.setCreatedAt(new Date());
                    return client;
                }).collect(Collectors.toList());
    }
}
