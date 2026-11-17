package example.oasystem.service;

import example.oasystem.entries.*;
import example.oasystem.repository.ApprovalRecordRepository;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.repository.MessageRepository;
import example.oasystem.repository.ReimbursementRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReimbursementRequestService {
    private final ReimbursementRequestRepository reimbursementRequestRepository;
    private final ApprovalRecordRepository approvalRecordRepository;
    private final MessageRepository messageRepository;
    final EmployeeRepository employeeRepository;


    public ReimbursementRequestService(ReimbursementRequestRepository reimbursementRequestRepository,
                                       ApprovalRecordRepository approvalRecordRepository,
                                       MessageRepository messageRepository,
                                       EmployeeRepository employeeRepository) {
        this.reimbursementRequestRepository = reimbursementRequestRepository;
        this.approvalRecordRepository = approvalRecordRepository;
        this.messageRepository = messageRepository;
        this.employeeRepository = employeeRepository;
    }

    // 获取报销审批记录
    public List<ApprovalRecord> getApprovalRecords(Employee employee) {
        List<ApprovalRecord> records;
        if (employee.getPositionId() == 7){
            records = new ArrayList<>();
            approvalRecordRepository.findAll().forEach(item ->{

                if (item.getApprovalType().equals("reimbursement")){
                    records.add(item);
                }
            });
            return records;
        }else {
            records = approvalRecordRepository
                    .findApprovalRecordsByApprovalTypeAndApproverId("reimbursement",
                            employee.getId());
            return records;
        }
    }
    public List<ReimbursementRequest> getReimbursementRequests(Integer employeeId) {
        List<ReimbursementRequest> byEmployeeId = reimbursementRequestRepository.findByEmployeeId(employeeId);
        return byEmployeeId;
    }

    // 报销申请
    public void createReimbursementRequest(ReimbursementRequest req,
                                           Integer employeeId) {
        // 提交报销申请
        req.setStatus("待审批");
        req.setEmployeeId(employeeId);
        ReimbursementRequest request = reimbursementRequestRepository.save(req);

        // 发送通知
        employeeRepository.findAll().forEach(employee -> {
            if (employee.getPositionId() == 2){// 向财务主管发送通知
                messageRepository.save(Message.reimbursementMessage(employeeId,employee.getId()));
            }
        });
        messageRepository.save(Message.reimbursementMessage(employeeId,5));
        // 提交审批
        employeeRepository.findAll().forEach(employee -> {
            if (employee.getPositionId() == 2){
                approvalRecordRepository.save(
                        ApprovalRecord.requestApproval(request.getId(),
                                request.getReimbursementReason(),"reimbursement",employee.getId()));
            }
        });

    }

    // 审批报销请求
    public void updateApprovalRecord(Integer id,
                                     String approvalComments,
                                     String approvalStatus) {

        // 更新审批记录
        ApprovalRecord approvalRecord = approvalRecordRepository
                .findApprovalRecordByIdAndApprovalType(id,"reimbursement");
        approvalRecord.setApprovalStatus(approvalStatus);
//        approvalRecord.setApprovalComments(approvalComments);
        approvalRecordRepository.save(approvalRecord);


        // 同步更新报销申请的状态
        ReimbursementRequest reimbursementRequest = reimbursementRequestRepository
                .findById(approvalRecord.getRequestId()).get();
        reimbursementRequest.setStatus(approvalStatus);
        reimbursementRequestRepository.save(reimbursementRequest);

        Message message = new Message();
        message.setSenderId(approvalRecord.getApproverId());
        message.setReceiverId(reimbursementRequest.getEmployeeId());
        message.setRead(false);
        message.setContent(approvalComments);
        message.setSubject(reimbursementRequest.getReimbursementType() + "报销申请" + approvalRecord.getApprovalStatus());
        messageRepository.save(message);
    }

    // 获取报销申请详细信息
    public ReimbursementRequest getReimbursementRequest(Integer id) {
        return reimbursementRequestRepository.findById(id).get();
    }





    // 删除报销申请
    public void deleteReimbursementRequest(Integer id) {
        reimbursementRequestRepository.deleteById(id);

    }

    // 修改报销申请
    public void updateReimbursementRequest(ReimbursementRequest req) {
        reimbursementRequestRepository.save(req);
    }

    // 获取所有报销申请
    public List<ReimbursementRequest> getAllReimbursementRequests() {
        Iterable<ReimbursementRequest> all = reimbursementRequestRepository.findAll();
        List<ReimbursementRequest> reqs = new ArrayList<>();
        for (ReimbursementRequest req : all) {
            reqs.add(req);
        }
        return reqs;
    }

    // 根据工号获取报销申请
    public List<ReimbursementRequest> getReimbursementRequestsByEmployeeId(
            Integer employeeId) {
        return reimbursementRequestRepository.findByEmployeeId(employeeId);
    }


}
