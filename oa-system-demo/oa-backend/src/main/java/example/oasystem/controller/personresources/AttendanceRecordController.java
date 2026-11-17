package example.oasystem.controller.personresources;

import example.oasystem.entries.AttendanceRecord;
import example.oasystem.entries.Employee;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.service.AttendanceRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceRecordController {
    private final AttendanceRecordService attendanceRecordService;
    private final EmployeeRepository employeeRepository;

    public AttendanceRecordController(AttendanceRecordService attendanceRecordService,
                                      EmployeeRepository employeeRepository) {
        this.attendanceRecordService = attendanceRecordService;
        this.employeeRepository = employeeRepository;
    }

    // 获取考勤记录
    @GetMapping
    private ResponseEntity<List<AttendanceRecord>> getAttendanceRecords(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        List<AttendanceRecord> records = attendanceRecordService.getAttendanceRecords(employee.getId());
        return ResponseEntity.ok(records);
    }

    // 获取所有考勤记录
    @GetMapping("/all")
    private ResponseEntity<Page<AttendanceRecord>> getAttendanceRecords(Pageable pageable,
                                                                        @RequestParam(required = false) Integer keyword) {
        Page<AttendanceRecord> attendanceRecords;
        if (keyword != null) {
            attendanceRecords = attendanceRecordService.getAttendanceRecords(
                    pageable,keyword);
        }else {
            attendanceRecords = attendanceRecordService.getAttendanceRecords(
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSort() // 页面、大小和排序参数的默认值。
                    ));
        }

        return ResponseEntity.ok(attendanceRecords);
    }

    // 获取所有当日考勤记录
    @GetMapping("/today")
    private ResponseEntity<AttendanceRecord> getAttendanceRecord(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        // 获取当日考勤记录
        AttendanceRecord record = attendanceRecordService.getAttendanceRecord(employee.getId());
        return ResponseEntity.ok(record);
    }

    // 新增考勤记录
    @PostMapping
    private ResponseEntity<AttendanceRecord> createAttendanceRecord(@RequestBody AttendanceRecord attendanceRecord) {
        attendanceRecordService.createAttendanceRecord(attendanceRecord);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 更新考勤记录
    @PutMapping
    private ResponseEntity<Void> updateAttendanceRecord(@RequestBody AttendanceRecord record) {
        attendanceRecordService.updateAttendanceRecord(record);
        return ResponseEntity.noContent().build();
    }

    // 删除考勤记录
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteAttendanceRecord(@PathVariable Integer id) {
        attendanceRecordService.deleteAttendanceRecord(id);
        return ResponseEntity.noContent().build();
    }

    // 打卡
    @PostMapping("/checkin")
    private ResponseEntity<Void> checkIn(UriComponentsBuilder ubc,
                                        Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        attendanceRecordService
                .checkIn(employee.getId(), LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    // 签退
    @PutMapping("/checkout")
    private ResponseEntity<Void> checkout(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        attendanceRecordService
                .checkOut(employee.getId(), LocalDateTime.now());
        return ResponseEntity.ok().build();
    }




}
