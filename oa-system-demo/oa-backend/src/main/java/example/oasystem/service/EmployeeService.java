package example.oasystem.service;

import example.oasystem.entries.Employee;
import example.oasystem.entries.Position;
import example.oasystem.repository.AuthoritiesRepository;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.repository.PositionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthoritiesRepository authoritiesRepository;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final PositionRepository positionRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           PasswordEncoder passwordEncoder,
                           AuthoritiesRepository authoritiesRepository,
                           JdbcUserDetailsManager jdbcUserDetailsManager,
                           PositionRepository positionRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authoritiesRepository = authoritiesRepository;
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.positionRepository = positionRepository;
    }

    //获取所有员工信息
//    @PreAuthorize("hasRole('管理员')")
    public Page<Employee> getEmployees(Pageable pageable) {
        Page<Employee> employees = employeeRepository.findAll(pageable);
        return employees;
    }

    public Page<Employee> searchEmployees(String keyword, Pageable pageable) {
        return employeeRepository.findByUsernameContaining(keyword, pageable);
    }

    // 获取员工详细信息
    public Employee getEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id).get();
        return employee;
    }
    public Employee getEmployee(String username) {
//        Employee employee = employeeRepository.findByIdAndUsername(id, username);
        Employee employee = employeeRepository.findByUsername(username);
        return employee;
    }

    // 创建员工信息
//    @PreAuthorize("hasRole('管理员')")
    public void createEmployee(Employee employee,String password) {
        String username = employee.getUsername();
        if (jdbcUserDetailsManager.userExists(username)) {
            return;
        }
        Position position = positionRepository.findById(employee.getPositionId()).get();
        UserDetails user = User.builder()
                .username(employee.getUsername())
                .password(passwordEncoder.encode(password))
                .roles(position.getName())
                .build();
        jdbcUserDetailsManager.createUser(user);
        employeeRepository.save(employee);

    }

    // 更新员工信息
//    @PreAuthorize("hasRole('管理员')")
    public void updateEmployee(Employee newEmployee,String password) {
        Employee employee = employeeRepository.findById(newEmployee.getId()).get();
        String newUsername = newEmployee.getUsername();
//        String oldUsername = employee.getUsername();
//        if (!newUsername.equals(oldUsername)) {
//            jdbcUserDetailsManager.deleteUser(oldUsername);
//            UserDetails user = User.builder()
//                    .username(newUsername)
//                    .password(passwordEncoder.encode(password))// 待定
//                    .roles(positionRepository.findById(employee.getPositionId()).get().getName())// 职位
//                    .build();
//            jdbcUserDetailsManager.createUser(user);
//        }
        if (password != null) {
            UserDetails user = User.builder()
                    .username(newUsername)
                    .password(passwordEncoder.encode(password))// 待定
                    .roles(positionRepository.findById(employee.getPositionId()).get().getName())// 职位
                    .build();
            jdbcUserDetailsManager.updateUser(user);
            //jdbcUserDetailsManager.changePassword(userDetails.getPassword(), passwordEncoder.encode(password));
        }
        employeeRepository.save(newEmployee);
    }

    //删除员工
//    @PreAuthorize("hasRole('管理员')")
    public void deleteEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id).get();
        employeeRepository.deleteById(id);// 删除员工
        authoritiesRepository.findById(employee.getUsername()).ifPresent(authorities -> {
            authoritiesRepository.deleteById(authorities.getUsername());// 删除权限
        });
        jdbcUserDetailsManager.deleteUser(employee.getUsername());// 删除用户
    }

    public Position getPosition(Integer id) {
        Position position = positionRepository.findById(id).get();
        return position;
    }

    public List<Position> getPositions() {
        List<Position> positions = new ArrayList<>();
        positionRepository.findAll().forEach(position -> {
            positions.add(position);
        });
        return positions;
    }


}
