package rex.chien.cms.controller;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import rex.chien.cms.domain.Company;
import rex.chien.cms.repository.CompanyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompanyControllerTest {
    private CompanyRepository companyRepository;
    private CompanyController companyController;

    @BeforeEach
    public void setUp() {
        companyRepository = Mockito.mock(CompanyRepository.class);
        companyController = new CompanyController(companyRepository);
    }

    @Test
    void getAll() {
        // arrange
        List<Company> expected = new ArrayList<>();
        expected.add(fakeCompany());
        expected.add(fakeCompany());
        expected.add(fakeCompany());

        Mockito.when(companyRepository.findAll()).thenReturn(expected);

        // act
        Iterable<Company> actual = companyController.getAll();

        // assert
        assertIterableEquals(expected, actual);
    }

    @Test
    void get() {
        // arrange
        Company expected = fakeCompany();

        Mockito.when(companyRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        // act
        ResponseEntity<Company> actual = companyController.get(expected.getId());

        // assert
        assertEquals(expected, actual.getBody());
    }

    @Test
    void get_When_CompanyNotExisted_Then_NotFound() {
        // arrange
        Mockito.when(companyRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        // act
        ResponseEntity<Company> actual = companyController.get(1);

        // assert
        assertEquals(expectedStatus, actual.getStatusCode());
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    private Company fakeCompany() {
        Faker faker = new Faker();
        Company company = new Company();
        company.setId(faker.number().randomNumber());
        company.setName(faker.company().name());
        company.setAddress(faker.address().fullAddress());

        return company;
    }
}