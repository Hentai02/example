package example.oasystem;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import example.oasystem.entries.Employee;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    void shouldCreateANewEmployee(){
        Employee employee = new Employee();
        employee.setBirthDate(LocalDate.now());
        employee.setEmail("admin@gmail.com");
        employee.setGender(false);
        employee.setPhone("123456789");
        employee.setHireDate(LocalDate.now());
        HttpEntity<Employee> request = new HttpEntity<>(employee);
        ResponseEntity<String> createResponse = restTemplate
                .withBasicAuth("admin123", "admin")
                .exchange("/employee", HttpMethod.POST,request, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldReturnAllEmployees(){
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin123", "admin")
                .getForEntity("/employee?page=0&size=2&sort=id,asc", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray read = documentContext.read("$[*]");
        assertThat(read.size()).isEqualTo(2);

        String username = documentContext.read("$[1].username");
        assertThat(username).isEqualTo("admin");
    }

    @Test
    void shouldUpdateEmployee(){
        Employee employee = new Employee();
        employee.setId(2);
        employee.setUsername("admin123");
        employee.setBirthDate(LocalDate.now());
        employee.setEmail("admin123@gmail.com");
        employee.setGender(true);
        employee.setPhone("1234567890");
        employee.setHireDate(LocalDate.now());
        HttpEntity<Employee> request = new HttpEntity<>(employee);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("admin123", "admin")
                .exchange("/employee/2", HttpMethod.PUT,request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
