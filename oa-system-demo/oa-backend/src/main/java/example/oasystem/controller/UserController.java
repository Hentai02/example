package example.oasystem.controller;

import example.oasystem.entries.Employee;
import example.oasystem.entries.UserPresent;
import example.oasystem.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final EmployeeRepository employeeRepository;

    public UserController(UserDetailsManager userDetailsManager,
                          PasswordEncoder passwordEncoder,
                          JdbcUserDetailsManager jdbcUserDetailsManager,
                          EmployeeRepository employeeRepository) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/signup")
    private ResponseEntity<Void> signup(@RequestBody UserPresent userPresent) {
        if (userDetailsManager.userExists(userPresent.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        UserDetails user = User.builder()
                .username(userPresent.getUsername())
                .password(passwordEncoder.encode(userPresent.getPassword()))
                .roles("USER")
                .build();
        userDetailsManager.createUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    private ResponseEntity<UserDetails> login(@RequestBody UserPresent userPresent,
                                         HttpSession session) {
        UserDetails userDetails = jdbcUserDetailsManager.loadUserByUsername(userPresent.getUsername());
        if (userDetails != null &&
                passwordEncoder.
                        matches(userPresent.getPassword(),
                                userDetails.getPassword())) {
            session.setAttribute("username", userDetails.getUsername());
            return ResponseEntity.ok(userDetails);

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{username}")
//    @PreAuthorize("USER")
    private ResponseEntity<String> me(Principal principal) {
        UserDetails userDetails = userDetailsManager.loadUserByUsername(principal.getName());
        return ResponseEntity.ok().body(userDetails.getUsername());
    }


    @PutMapping
    private ResponseEntity<Void> update(@RequestBody UserPresent userPresent,Principal principal) {
        UserDetails userDetails = jdbcUserDetailsManager.loadUserByUsername(principal.getName());
        jdbcUserDetailsManager.changePassword(userDetails.getPassword(),passwordEncoder.encode(userPresent.getPassword()));
        return ResponseEntity.ok().build();
    }

}
