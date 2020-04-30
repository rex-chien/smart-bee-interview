package rex.chien.cms.security;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import rex.chien.cms.domain.Client;
import rex.chien.cms.domain.Company;
import rex.chien.cms.repository.ClientRepository;
import rex.chien.cms.repository.CompanyRepository;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OperatorRoleTests {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ClientRepository clientRepository;

    private List<Company> companies;
    private List<Client> clients;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        companies = IntStream.range(0, 5)
                .mapToObj(i -> {
                    Company company = new Company();
                    company.setName("company " + i);
                    company.setAddress("address" + i);
                    return company;
                }).collect(Collectors.toList());
        companyRepository.saveAll(companies);

        clients = companies.stream()
                .flatMap(company -> IntStream.range(0, 5)
                        .mapToObj(i -> {
                            Client client = new Client();
                            client.setName("client " + company.getId() + i);
                            client.setPhone("phone " + company.getId() + i);
                            client.setEmail("email" + company.getId() + i);
                            client.setCompany(company);
                            return client;
                        }))
                .collect(Collectors.toList());
        clientRepository.saveAll(clients);
    }

    @Nested
    @WithMockOperator
    class CompanyController {
        @Test
        public void getAll_then_status200() throws Exception {
            mvc.perform(MockMvcRequestBuilders.get("/companies")
            )
                    .andExpect(status().isOk());
        }

        @Test
        public void get_when_existed_then_status200() throws Exception {
            mvc.perform(MockMvcRequestBuilders.get("/companies/" + companies.get(0).getId())
            )
                    .andExpect(status().isOk());
        }

        @Test
        public void get_when_notexisted_then_status404() throws Exception {
            mvc.perform(MockMvcRequestBuilders.get("/companies/" + (companies.get(0).getId() - 1))
            )
                    .andExpect(status().isNotFound());
        }

//        @Test
//        public void create_when_success_then_status200() throws Exception {
//            // arrange
//            CompanyRequest request = new CompanyRequest();
//            request.setName("new company");
//            request.setAddress("new address");
//
//            mvc.perform(MockMvcRequestBuilders.put("/companies")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content((new ObjectMapper()).writeValueAsString(request)))
//                    .andExpect(status().isOk());
//        }
    }
}

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(roles = {RoleName.OPERATOR})
@interface WithMockOperator {
}