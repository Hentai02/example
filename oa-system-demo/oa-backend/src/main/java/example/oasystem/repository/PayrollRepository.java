package example.oasystem.repository;

import example.oasystem.entries.Payroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PayrollRepository extends CrudRepository<Payroll, Integer> {
    List<Payroll> findByEmployeeId(Integer employeeId);
    //
    @Query("SELECT * FROM payrolls p WHERE MONTH(CURDATE()) = MONTH(p.created_at) AND p.employee_id = :employeeId")
    Payroll findPayrollsByMonthAndEmployeeId(@Param("employeeId") Integer employeeId);
    // 获取该年的当月薪酬
    @Query("SELECT * FROM payrolls WHERE YEAR(created_at) = YEAR(CURDATE()) AND MONTH(created_at) = MONTH(CURDATE()) AND employee_id = :employeeId")
    Payroll findPayrollsByYearOfMonth(@Param("employeeId") Integer employeeId);

    Page<Payroll> findAll(Pageable pageable);

    Page<Payroll> findByEmployeeIdContaining(Integer employeeId, Pageable pageable);
}
