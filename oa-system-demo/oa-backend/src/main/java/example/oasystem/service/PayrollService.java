package example.oasystem.service;

import example.oasystem.entries.*;
import example.oasystem.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PayrollService {
    private final PayrollRepository payrollRepository;
    private final OvertimeRequestRepository overtimeRequestRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final EmployeeRepository employeeRepository;

    public PayrollService(PayrollRepository payrollRepository,
                          OvertimeRequestRepository overtimeRequestRepository,
                          AttendanceRecordRepository attendanceRecordRepository,
                          EmployeeRepository employeeRepository) {
        this.payrollRepository = payrollRepository;
        this.overtimeRequestRepository = overtimeRequestRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.employeeRepository = employeeRepository;
    }

    // 获取所有薪酬记录
    public Page<Payroll> getPayrolls(Pageable pageable) {
        Page<Payroll> payrolls = payrollRepository.findAll(pageable);
        return payrolls;
    }
    public Page<Payroll> searchPayrolls(Integer keyword, Pageable pageable) {
        Page<Payroll> payrolls = payrollRepository
                .findByEmployeeIdContaining(keyword, pageable);
        return payrolls;

    }

    // 获取员工薪酬记录
    public List<Payroll> getPayrolls(Integer employeeId) {
        List<Payroll> payrolls = payrollRepository.findByEmployeeId(employeeId);
        return payrolls;
    }

    // 获取该年当月薪酬
    public Payroll getPayroll(Integer employeeId) {
        Payroll payroll = payrollRepository.findPayrollsByYearOfMonth(employeeId);
        return payroll;
    }

    // 创建酬薪
    public Payroll createPayroll(Integer employeeId) {
        Payroll payroll = new Payroll();
        // 基础工资
//        payroll.setBaseSalary(5000.0);
        Employee employee = employeeRepository.findById(employeeId).get();
        switch (employee.getPositionId()) {
            case 1:
                payroll.setBaseSalary(8000.00);
                break;
            case 2:
                payroll.setBaseSalary(6000.00);
                break;
            case 3:
                payroll.setBaseSalary(5000.00);
                break;
            case 4:
                payroll.setBaseSalary(10000.00);
                break;
            case 6:
                payroll.setBaseSalary(7000.00);
                break;
            case 7:
                payroll.setBaseSalary(1000.00);
                break;
        }
        payroll.setEmployeeId(employeeId);

        payroll.setBonus(0.0);// 奖金
        payroll.setDeductions(0.0);// 扣除
        payroll.setOvertimeSalary(0.0);// 加班
        // 总工资
        payroll.setTotalSalary(payroll.getTotalSalary());
        payroll.setCreatedAt(LocalDateTime.now());
        payroll.setUpdatedAt(LocalDateTime.now());
        Payroll save = payrollRepository.save(payroll);
        return save;

    }
    public void createPayroll(Payroll payroll) {
        payrollRepository.save(payroll);
    }

    // 计算当月薪酬
    public void calculateOfMonthSalary(Payroll payroll) {
        // 计算加班工资
        List<OvertimeRequest> overtimeRequests = overtimeRequestRepository
                .findByEmployeeId(payroll.getEmployeeId());
        for (OvertimeRequest overtimeRequest : overtimeRequests) {
            int hour = overtimeRequest.getEndTime().getHour() - overtimeRequest.getStartTime().getHour();
            if (payroll.getOvertimeSalary() != 0.0){
                payroll.setOvertimeSalary(payroll.getOvertimeSalary() + (hour * 15.0));
            }else {
                payroll.setOvertimeSalary(hour * 15.0);
            }
        }

        // 计算扣除
        List<AttendanceRecord> attendanceRecords = attendanceRecordRepository
                .findByEmployeeId(payroll.getEmployeeId());
        // 获取当月天数
        YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), LocalDate.now().getMonth());
        int daysInMonth = yearMonth.lengthOfMonth();
        int daysInWork = 0;// 出勤天数
        int daysOutWork = 0;// 迟到天数
        int daysOffWork = 0;// 请假天数
        for (AttendanceRecord attendanceRecord : attendanceRecords) {
            if(attendanceRecord.getStatus().equals("出勤")){
                daysInWork++;
            }else if(attendanceRecord.getStatus().equals("迟到")){
                daysOutWork++;
            }else {
                daysInWork++;
            }
        }
        double offset = ((daysInWork - daysOffWork) * 1.5) + (daysOutWork * 0.7);
        if (attendanceRecords.isEmpty()){
            LocalDate today = LocalDate.now();
            // 获取今天是当月的第几日
            int dayOfMonth = today.getDayOfMonth();
            offset = 1.0 * dayOfMonth;
        }
        if ((daysInWork - daysInMonth) < 0) {
            if (payroll.getDeductions() != 0.0){
                payroll.setDeductions(payroll.getDeductions() + (8.0 * offset));
            }else {
                payroll.setDeductions(8.0 * offset);
            }
        }else {
            // 计算奖金（全勤）
            if (payroll.getBonus() != 0.0){
                payroll.setBonus(payroll.getBonus() + 200.00);
            }else {
                payroll.setBonus(200.00);
            }
        }

        payrollRepository.save(payroll);
    }


    // 删除酬薪
    public void deletePayroll(Integer id) {
        payrollRepository.deleteById(id);
    }

    public void updatePayroll(Payroll payroll) {
        payrollRepository.save(payroll);
    }


}
