package example.oasystem.controller.approval;

import example.oasystem.entries.ApprovalRecord;
import example.oasystem.entries.Employee;
import example.oasystem.entries.ReimbursementRequest;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.service.ReimbursementRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/reimbursement")
public class ReimbursementRequestController {

    private final ReimbursementRequestService reimbursementRequestService;
    private final EmployeeRepository employeeRepository;

    public ReimbursementRequestController(ReimbursementRequestService reimbursementRequestService,
                                          EmployeeRepository employeeRepository) {
        this.reimbursementRequestService = reimbursementRequestService;
        this.employeeRepository = employeeRepository;
    }

    // 获取报销审批记录
    @GetMapping("/approvals")
    private List<ApprovalRecord> getApprovals(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        List<ApprovalRecord> records = reimbursementRequestService
                .getApprovalRecords(employee);
        return records;
    }
//    @GetMapping
//    private List<ReimbursementRequest> getReimbursements(Principal principal) {
//        Employee employee = employeeRepository.findByUsername(principal.getName());
//        List<ReimbursementRequest> reimbursementRequests = reimbursementRequestService.getReimbursementRequests(employee.getId());
//        return reimbursementRequests;
//    }

    // 提交报销申请
    @PostMapping
    public ResponseEntity<Void> createReimbursementRequest(@RequestBody ReimbursementRequest reimbursementRequest,
                                                           Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        reimbursementRequestService.createReimbursementRequest(reimbursementRequest,employee.getId());
        return ResponseEntity.ok().build();
    }

    // 审批报销申请
    @PostMapping("/approve")
    public ResponseEntity<Void> approveReimbursementRequest(@RequestParam("id") Integer id ,
                                                            @RequestParam("approvalComments") String approvalComments,
                                                            @RequestParam("approvalStatus") String approvalStatus){
        reimbursementRequestService.updateApprovalRecord(id, approvalComments, approvalStatus);
        return ResponseEntity.ok().build();
    }

    // 获取报销详情信息
    @GetMapping("/{id}")
    private ResponseEntity<ReimbursementRequest> getReimbursementRequestById(@PathVariable Integer id) {
        return ResponseEntity.ok(reimbursementRequestService.getReimbursementRequest(id));
    }



    // 获取员工所有报销申请
    @GetMapping
    private ResponseEntity<List<ReimbursementRequest>> getReimbursementRequestByEmployeeId
            (Principal principal){
        Employee employee = employeeRepository.findByUsername(principal.getName());
        List<ReimbursementRequest> reimbursementRequests =
                reimbursementRequestService.getReimbursementRequestsByEmployeeId(employee.getId());
        return ResponseEntity.ok(reimbursementRequests);
    }

    // 获取所有审批记录
    @GetMapping("/getAll")
    public ResponseEntity<List<ReimbursementRequest>> getAllReimbursementRequest(){
        List<ReimbursementRequest> allReimbursementRequests =
                reimbursementRequestService.getAllReimbursementRequests();
        return ResponseEntity.ok(allReimbursementRequests);

    }

    // 更新报销申请
    @PutMapping("/{id}")
    public ResponseEntity<Void> putReimbursementRequest
            (@RequestBody ReimbursementRequest reimbursementRequest,
             @PathVariable Integer id){
//        reimbursementRequestService.createReimbursementRequest(reimbursementRequest);
        return ResponseEntity.ok().build();

    }

    // 删除报销申请
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReimbursementRequest(@PathVariable Integer id){
        reimbursementRequestService.deleteReimbursementRequest(id);
        return ResponseEntity.ok().build();
    }
}
