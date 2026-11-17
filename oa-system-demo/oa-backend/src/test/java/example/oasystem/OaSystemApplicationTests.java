package example.oasystem;

import example.oasystem.entries.UserPresent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.provisioning.UserDetailsManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OaSystemApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldCreateANewUser(){
        UserPresent userPresent = new UserPresent();
        userPresent.setUsername("admin");
        userPresent.setPassword("admin");
        HttpEntity<UserPresent> request = new HttpEntity<>(userPresent);
        ResponseEntity<String> createResponse = restTemplate
                .exchange("/user/signup", HttpMethod.POST,request, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldNotCreateANewUser(){
        UserPresent userPresent = new UserPresent();
        userPresent.setUsername("admin123");
        userPresent.setPassword("admin");
        HttpEntity<UserPresent> request = new HttpEntity<>(userPresent);
        ResponseEntity<String> createResponse = restTemplate
                .exchange("/user/signup", HttpMethod.POST,request, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldLogin(){
        UserPresent userPresent = new UserPresent();
        userPresent.setUsername("admin");
        userPresent.setPassword("admin");
        HttpEntity<UserPresent> request = new HttpEntity<>(userPresent);

        ResponseEntity<String> createResponse = restTemplate
                .exchange("/user/login", HttpMethod.POST,request, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void shouldNotLogin(){
        UserPresent userPresent = new UserPresent();
        userPresent.setUsername("admin");
        userPresent.setPassword("admin222");
        HttpEntity<UserPresent> request = new HttpEntity<>(userPresent);

        ResponseEntity<String> createResponse = restTemplate
                .exchange("/user/login", HttpMethod.POST,request, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }

    @Test
    void shouldGetUser(){
        ResponseEntity<String> createResponse = restTemplate
                .withBasicAuth("admin123", "admin")
                .postForEntity("/user/admin123", null, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(createResponse.getBody());
    }

    @Test
    void contextLoads() {
//        userDetailsManager.deleteUser("admin");
    }

}
