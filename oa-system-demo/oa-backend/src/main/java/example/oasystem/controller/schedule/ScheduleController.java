package example.oasystem.controller.schedule;

import example.oasystem.entries.Employee;
import example.oasystem.entries.Schedule;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.service.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final EmployeeRepository employeeRepository;

    public ScheduleController(ScheduleService scheduleService,
                              EmployeeRepository employeeRepository) {
        this.scheduleService = scheduleService;
        this.employeeRepository = employeeRepository;
    }


    // 获取日程
    @GetMapping
    public ResponseEntity<Page<Schedule>> getSchedules(Principal principal,
                                                       Pageable pageable,
                                                       @RequestParam(required = false) String keyword) {
        Employee employee = employeeRepository.findByUsername(principal.getName());

        Page<Schedule> schedules;
        if (keyword != null) {
            schedules = scheduleService.searchSchedule(keyword, pageable,employee.getId());
        } else {
            schedules = scheduleService.getSchedules(
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSort()),employee.getId());
        }
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        List<Schedule> allSchedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(allSchedules);
    }

    // 创建日程
    @PostMapping
    public ResponseEntity<Void> createSchedule(Principal principal, @RequestBody Schedule schedule) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        scheduleService.createSchedule(schedule, employee.getId());
        return ResponseEntity.ok().build();
    }

    // 删除日期
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Integer id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }

    // 更新日程
    @PutMapping
    public ResponseEntity<Void> updateSchedule(Schedule schedule) {
        scheduleService.updateSchedule(schedule);
        return ResponseEntity.ok().build();
    }

}
