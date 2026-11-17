package example.oasystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PayRollTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldCreateSamplePayRoll() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin123", "admin")
                .postForEntity("/payroll/payment", null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnPayRolls() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin123","admin")
                .getForEntity("/payroll", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response.getBody());
    }
}
