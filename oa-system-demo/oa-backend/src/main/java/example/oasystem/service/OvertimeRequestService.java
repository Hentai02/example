package example.oasystem.service;

import example.oasystem.entries.*;
import example.oasystem.repository.ApprovalRecordRepository;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.repository.MessageRepository;
import example.oasystem.repository.OvertimeRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OvertimeRequestService {

    private final OvertimeRequestRepository overtimeRequestRepository;
    private final ApprovalRecordRepository approvalRecordRepository;
    private final MessageRepository messageRepository;
    final EmployeeRepository employeeRepository;


    public OvertimeRequestService(OvertimeRequestRepository overtimeRequestRepository,
                                  ApprovalRecordRepository approvalRecordRepository,
                                  MessageRepository messageRepository,
                                  EmployeeRepository employeeRepository) {
        this.overtimeRequestRepository = overtimeRequestRepository;
        this.approvalRecordRepository = approvalRecordRepository;
        this.messageRepository = messageRepository;
        this.employeeRepository = employeeRepository;
    }

    // 获取审批记录
    public List<ApprovalRecord> getApprovalRecords(Employee employee) {
        List<ApprovalRecord> records;
        if (employee.getPositionId() == 7){
            records = new ArrayList<>();
            approvalRecordRepository.findAll().forEach(item ->{
                if (item.getApprovalType().equals("overtime")){
                    records.add(item);
                }
            });
            return records;
        }else {
            records = approvalRecordRepository
                    .findApprovalRecordsByApprovalTypeAndApproverId("overtime",
                            employee.getId());
            return records;
        }
    }

    // 加班申请
    public void createOvertimeRequest(OvertimeRequest overtimeRequest,
                                      Integer employeeId) {
        // 提交加班申请
        overtimeRequest.setEmployeeId(employeeId);
        overtimeRequest.setStatus("待审批");
        OvertimeRequest request = overtimeRequestRepository.save(overtimeRequest);

        // 发送通知
        messageRepository.save(Message.overtimeMessage(employeeId,5));
        employeeRepository.findAll().forEach(employee -> {
            if (employee.getPositionId() == 4){// 向生产主管发送通知
                messageRepository.save(Message.overtimeMessage(employeeId,employee.getId()));
            }
        });
        // 提交审批
        employeeRepository.findAll().forEach(employee -> {
            if (employee.getPositionId() == 4){
                approvalRecordRepository.save(
                        ApprovalRecord.requestApproval(request.getId(),
                                overtimeRequest.getReason(),"overtime",employee.getId()));
            }
        });

    }

    // 审批加班请求
    public void updateApprovalRecord(Integer id,
                                     String approvalComments,
                                     String approvalStatus) {

        // 更新审批记录
        ApprovalRecord approvalRecord = approvalRecordRepository
                .findApprovalRecordByIdAndApprovalType(id,"overtime");
        approvalRecord.setApprovalStatus(approvalStatus);
//        approvalRecord.setApprovalComments(approvalComments);
        approvalRecordRepository.save(approvalRecord);


        // 同步更新加班申请的状态
        OvertimeRequest overtimeRequest = overtimeRequestRepository
                .findById(approvalRecord.getRequestId()).get();
        overtimeRequest.setStatus(approvalStatus);
        overtimeRequestRepository.save(overtimeRequest);

        Message message = new Message();
        message.setSenderId(approvalRecord.getApproverId());
        message.setReceiverId(overtimeRequest.getEmployeeId());
        message.setRead(false);
        message.setContent(approvalComments);
        message.setSubject(overtimeRequest.getOvertimeDate() + "加班申请" + approvalRecord.getApprovalStatus());
        messageRepository.save(message);
    }

    // 获取加班申请详细信息
    public OvertimeRequest getOvertimeRequest(Integer id) {
        return overtimeRequestRepository.findById(id).get();
    }

    // 获取加班记录
    public List<OvertimeRequest> getOvertimeRequests(Integer employeeId) {
        List<OvertimeRequest> overtimeRequests = overtimeRequestRepository
                .findByEmployeeId(employeeId);
        return overtimeRequests;
    }




}
