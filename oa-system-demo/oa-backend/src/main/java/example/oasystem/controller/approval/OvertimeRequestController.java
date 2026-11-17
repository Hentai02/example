package example.oasystem.controller.approval;

import example.oasystem.entries.ApprovalRecord;
import example.oasystem.entries.Employee;
import example.oasystem.entries.OvertimeRequest;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.service.OvertimeRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/overtime")
public class OvertimeRequestController {
    private final OvertimeRequestService overtimeRequestService;
    private final EmployeeRepository employeeRepository;

    public OvertimeRequestController(OvertimeRequestService overtimeRequestService,
                                     EmployeeRepository employeeRepository) {
        this.overtimeRequestService = overtimeRequestService;
        this.employeeRepository = employeeRepository;
    }

    // 获取加班审批记录
    @GetMapping("/approvals")
    public ResponseEntity<List<ApprovalRecord>> getApprovals(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        List<ApprovalRecord> records = overtimeRequestService.getApprovalRecords(employee);
        return ResponseEntity.ok(records);
    }
    @GetMapping
    public ResponseEntity<List<OvertimeRequest>> getOvertimeRequests(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        List<OvertimeRequest> records = overtimeRequestService.getOvertimeRequests(employee.getId());
        return ResponseEntity.ok(records);
    }

    // 提交加班申请
    @PostMapping
    private ResponseEntity<Void> createOvertimeRequest(@RequestBody OvertimeRequest overtimeRequest,
                                                       Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        overtimeRequestService.createOvertimeRequest(overtimeRequest, employee.getId());
        return ResponseEntity.ok().build();
    }

    // 审批加班申请
    @PostMapping("/approve")
    private ResponseEntity<Void> approveOvertimeRequest(@RequestParam("id") Integer id ,
                                                        @RequestParam("approvalComments") String approvalComments,
                                                        @RequestParam("approvalStatus") String approvalStatus){
        overtimeRequestService.updateApprovalRecord(id, approvalComments, approvalStatus);
        return ResponseEntity.ok().build();
    }

    // 获取加班申请详细信息
    @GetMapping("/{id}")
    public ResponseEntity<OvertimeRequest> getOvertimeRequest(@PathVariable Integer id) {
        OvertimeRequest overtimeRequest = overtimeRequestService.getOvertimeRequest(id);
        return ResponseEntity.ok(overtimeRequest);
    }
}
