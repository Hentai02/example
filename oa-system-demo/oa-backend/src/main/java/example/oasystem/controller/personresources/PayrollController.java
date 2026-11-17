package example.oasystem.controller.personresources;

import example.oasystem.entries.Employee;
import example.oasystem.entries.LeaveRequest;
import example.oasystem.entries.Payroll;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.service.PayrollService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/payroll")
public class PayrollController {
    private final PayrollService payrollService;
    private final EmployeeRepository employeeRepository;

    public PayrollController(PayrollService payrollService,
                             EmployeeRepository employeeRepository) {
        this.payrollService = payrollService;
        this.employeeRepository = employeeRepository;
    }

    // 获取所有酬薪记录
    @GetMapping("/all")
    private ResponseEntity<Page<Payroll>> getPayrolls(Pageable pageable,
                                                      @RequestParam(required = false) Integer keyword) {
        Page<Payroll> payrolls;
        if (keyword != null) {
            payrolls = payrollService.searchPayrolls(
                    keyword,pageable);
        }else {
            payrolls = payrollService.getPayrolls(
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSort() // 页面、大小和排序参数的默认值。
                    ));
        }

        return ResponseEntity.ok(payrolls);
    }

    // 获取当月酬薪记录
    @GetMapping
    private ResponseEntity<Payroll> getPayroll(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        Payroll payrolls = payrollService.getPayroll(employee.getId());
        return ResponseEntity.ok(payrolls);
    }

    // 计算当月工资
    @PostMapping("/calculate")
    private ResponseEntity<Void> createSalaryPayment(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        Payroll payroll = payrollService.getPayroll(employee.getId());
        if (payroll == null) {
            payroll = payrollService.createPayroll(employee.getId());
        }
        payrollService.calculateOfMonthSalary(payroll);
        return ResponseEntity.ok().build();
    }

    // 删除酬薪
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deletePayroll(@PathVariable Integer id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.ok().build();
    }

    // 更新酬薪
    @PutMapping
    private ResponseEntity<Void> updatePayroll(@RequestBody Payroll payroll) {
        payrollService.updatePayroll(payroll);
        return ResponseEntity.ok().build();
    }

    // 创建薪酬
    @PostMapping
    private ResponseEntity<Void> createPayroll(@RequestBody Payroll newPayroll) {
        Integer employeeId = newPayroll.getEmployeeId();
        Payroll payroll = payrollService.getPayroll(employeeId);

        int dayOfMonth = payroll.getUpdatedAt().getDayOfMonth();
        if (newPayroll.getUpdatedAt() == null) {
            newPayroll.setUpdatedAt(LocalDateTime.now());
        }
        if (dayOfMonth == newPayroll.getUpdatedAt().getDayOfMonth()) {
            return ResponseEntity.ok().build();
        }
        payrollService.createPayroll(newPayroll);
        return ResponseEntity.ok().build();
    }



}
