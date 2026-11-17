package example.oasystem.repository;

import example.oasystem.entries.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    Optional<Employee> findById(Integer id);
    Page<Employee> findAll(Pageable pageable);
    Employee findByUsername(String username);
    Employee findByIdAndUsername(Integer id, String username);
    boolean existsByIdAndUsername(Integer id, String username);

    Page<Employee> findByUsernameContaining(String keyword, Pageable pageable);
}
