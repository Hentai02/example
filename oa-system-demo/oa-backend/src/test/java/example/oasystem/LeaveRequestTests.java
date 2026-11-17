package example.oasystem;

import example.oasystem.entries.LeaveRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LeaveRequestTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldViewApprovalRecordOfEmployee() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin123", "admin")
                .getForEntity("/leave", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        System.out.println(response.getBody());
    }

    @Test
    void shouldSubmitLeaveRequest(){
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setLeaveType("Admin");
        leaveRequest.setReason("Leave Reason");
        leaveRequest.setStartDate(LocalDate.now());
        leaveRequest.setEndDate(LocalDate.now().plusDays(4));
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin123", "admin")
                .postForEntity("/leave", leaveRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        System.out.println(response.getBody());
    }

    @Test
    void shouldApproveLeaveRequest(){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/leave/approve")
                .queryParam("leaveRequestId", 1)
                .queryParam("employeeId", 2);
        URI uri = uriBuilder.build().toUri();

        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin123", "admin")
                .exchange(uri, HttpMethod.POST, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
