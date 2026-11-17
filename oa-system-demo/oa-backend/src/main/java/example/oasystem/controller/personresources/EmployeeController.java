package example.oasystem.controller.personresources;

import example.oasystem.entries.Position;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.entries.Employee;
import example.oasystem.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(EmployeeService employeeService,
                              UserDetailsManager userDetailsManager,
                              PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    // 获取所有员工
    @GetMapping("/all")
    private ResponseEntity<Page<Employee>> getEmployees(Pageable pageable,
                                                        @RequestParam(required = false) String keyword) {
        Page<Employee> employees;
        if (keyword != null && !keyword.isEmpty()) {
            employees = employeeService.searchEmployees(keyword, pageable);
        } else {
            employees = employeeService.getEmployees(
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSortOr(Sort.by(Sort.Direction.ASC, "username")) // 页面、大小和排序参数的默认值。
                    ));
        }
        return ResponseEntity.ok(employees);
    }


    // 查看员工
    @GetMapping("/{id}")
    private ResponseEntity<Employee> getEmployee(@PathVariable Integer id) {
        Employee employee = employeeService.getEmployee(id);
        return ResponseEntity.ok(employee);
    }
    @GetMapping("/username")
    private ResponseEntity<Employee> getEmployee(@RequestParam("username") String username) {
        Employee employee = employeeService.getEmployee(username);
        return ResponseEntity.ok(employee);
    }

    // 创建新员工
    @PostMapping()
    private ResponseEntity<Void> createEmployee(@RequestBody Employee employee,
                                                @RequestParam("password")String password,
                                                Principal principal) {
        employeeService.createEmployee(employee,password);
        return ResponseEntity.ok().build();
    }



    // 更新员工信息
    @PutMapping()
    private ResponseEntity<Void> updateEmployee(@RequestBody Employee newEmployee,
                                                @RequestParam("password") String password) {
        employeeService.updateEmployee(newEmployee,password);
        return ResponseEntity.ok().build();
    }

    // 删除员工
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/position")
    private ResponseEntity<Position> getEmployeePosition(@RequestParam("id") Integer id) {
        Position position = employeeService.getPosition(id);
        return ResponseEntity.ok(position);
    }

    @GetMapping("/positions")
    private ResponseEntity<List<Position>> getEmployeePositions() {
        List<Position> positions = employeeService.getPositions();
        return ResponseEntity.ok(positions);
    }
}
