package example.oasystem.controller.approval;

import example.oasystem.entries.ApprovalRecord;
import example.oasystem.entries.Employee;
import example.oasystem.entries.LeaveRequest;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.service.LeaveRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveApprovalController {

    private final LeaveRequestService leaveRequestService;
    private final EmployeeRepository employeeRepository;


    public LeaveApprovalController(LeaveRequestService leaveRequestService,
                                   EmployeeRepository employeeRepository) {
        this.leaveRequestService = leaveRequestService;
        this.employeeRepository = employeeRepository;
    }

    // 获取请假审批记录
    @GetMapping("/approvals")
    private ResponseEntity<List<ApprovalRecord>> getApprovals(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        List<ApprovalRecord> records = leaveRequestService
                .getApprovals(employee);
        return ResponseEntity.ok(records);
    }

//    @GetMapping("/approvals/all")
//    private ResponseEntity<List<ApprovalRecord>> getAllApprovals(Principal principal) {
//        Employee employee = employeeRepository.findByUsername(principal.getName());
//        if (employee)
//        List<ApprovalRecord> records = leaveRequestService
//                .getApprovals(employee.getId());
//        return ResponseEntity.ok(records);
//    }
    @GetMapping("/")
    private ResponseEntity<List<LeaveRequest>> getLeaves(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequests(employee.getId());
        return ResponseEntity.ok(leaveRequests);

    }

    // 提交请假申请
    @PostMapping
    private ResponseEntity<Void> createLeaveRequest(@RequestBody LeaveRequest leaveRequest,
                                                    Principal principal,
                                                    UriComponentsBuilder ubc) {

        Employee employee = employeeRepository.findByUsername(principal.getName());
        leaveRequestService.
                createLeaveRequest(leaveRequest,employee.getId());
        return ResponseEntity.ok().build();
    }

    // 审批请假申请
    @PostMapping("/approve")
    private ResponseEntity<Void> approveLeaveRequest(@RequestParam("id") Integer id ,
                                                     @RequestParam("approvalComments") String approvalComments,
                                                     @RequestParam("approvalStatus") String approvalStatus) {
        leaveRequestService.updateApprovalRecord(id, approvalComments,approvalStatus);
        return ResponseEntity.ok().build();
    }

    // 获取请假申请详细信息
    @GetMapping("/{id}")
    private ResponseEntity<LeaveRequest> getLeaveRequest(@PathVariable Integer id) {
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequest(id);
        return ResponseEntity.ok(leaveRequest);
    }
}
