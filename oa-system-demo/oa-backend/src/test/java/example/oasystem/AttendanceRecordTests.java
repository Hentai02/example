package example.oasystem;

import example.oasystem.entries.AttendanceRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AttendanceRecordTests {
    @Autowired
    TestRestTemplate restTemplate;


    @Test
    void shouldCheckInAttendanceRecord() {
        int employeeId = 2;
        HttpEntity<Integer> request = new HttpEntity<>(employeeId);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("admin123", "admin")
                .postForEntity("/attendance/checkin", request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldCheckOutAttendanceRecord() {
        int employeeId = 2;
        HttpEntity<Integer> request = new HttpEntity<>(employeeId);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("admin123", "admin")
                .exchange("/attendance/checkout", HttpMethod.PUT,request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldViewEmployeeOfAttendanceRecord(){
        ResponseEntity<String> response = restTemplate
                .getForEntity("/attendance/2",String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response.getBody());
    }
}
