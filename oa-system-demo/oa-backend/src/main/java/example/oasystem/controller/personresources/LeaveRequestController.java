package example.oasystem.controller.personresources;

import example.oasystem.entries.AttendanceRecord;
import example.oasystem.entries.Employee;
import example.oasystem.entries.LeaveRequest;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.service.LeaveRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestController(LeaveRequestService leaveRequestService, EmployeeRepository employeeRepository) {
        this.leaveRequestService = leaveRequestService;
        this.employeeRepository = employeeRepository;
    }

    // 获取请假申请
    @GetMapping("/all")
    private ResponseEntity<Page<LeaveRequest>> getLeaveRequest(Pageable pageable,
                                                               @RequestParam(required = false) Integer keyword) {
        Page<LeaveRequest> leaveRequests;
        if (keyword != null) {
            leaveRequests = leaveRequestService.getLeaveRequests(
                    pageable,keyword);
        }else {
            leaveRequests = leaveRequestService.getLeaveRequests(
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSort() // 页面、大小和排序参数的默认值。
                    ));
        }

        return ResponseEntity.ok(leaveRequests);
    }

    // 获取请假申请
    @GetMapping
    private ResponseEntity<List<LeaveRequest>> getLeaveRequest(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        List<LeaveRequest> leaveRequests =
                leaveRequestService.getLeaveRequests(employee.getId());
        return ResponseEntity.ok(leaveRequests);
    }

    // 更新请假请求
    @PutMapping()
    private ResponseEntity<Void> updateLeaveRequest(@RequestBody LeaveRequest leaveRequest){
        leaveRequestService.updateLeaveRequest(leaveRequest);
        return ResponseEntity.ok().build();
    }

    // 删除请假请求
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteLeaveRequest(@PathVariable("id") Integer id){
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.ok().build();
    }
}
