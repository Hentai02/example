package example.oasystem.service;

import example.oasystem.entries.*;
import example.oasystem.repository.ApprovalRecordRepository;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.repository.LeaveRequestRepository;
import example.oasystem.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final ApprovalRecordRepository approvalRecordRepository;
    private final MessageRepository messageRepository;
    private final AttendanceRecordService attendanceRecordService;
    final EmployeeRepository employeeRepository;

    @Autowired
    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               ApprovalRecordRepository approvalRecordRepository,
                               MessageRepository messageRepository,
                               AttendanceRecordService attendanceRecordService,
                               EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.approvalRecordRepository = approvalRecordRepository;
        this.messageRepository = messageRepository;
        this.attendanceRecordService = attendanceRecordService;
        this.employeeRepository = employeeRepository;
    }

    // 获取请假审批记录
//    @PreAuthorize("hasAnyRole('ROLE_管理员', 'ROLE_人事主管')")
    public List<ApprovalRecord> getApprovals(Employee employee) {
        List<ApprovalRecord> records;
        if (employee.getPositionId() == 7){
            records = new ArrayList<>();
            approvalRecordRepository.findAll().forEach(record -> {
                System.out.println(record.getApprovalType());
                if (record.getApprovalType().equals("leave")){
                    records.add(record);
                }

            });
//            approvalRecordRepository.findAll().forEach(item ->{
//                if (item.getApprovalType().equals("leave")){
//                    records.add(item);
//                }
//            });
            return records;
        }else {
            records = approvalRecordRepository
                    .findApprovalRecordsByApprovalTypeAndApproverId("leave",
                            employee.getId());
            return records;
        }
//        return records;
    }

    // 请假申请
    public void createLeaveRequest(LeaveRequest leaveRequest,
                              Integer employeeId) {

        // 提交请假申请
//        leaveRequest.setEmployeeId(employeeId);
        leaveRequest.setStatus("待审批");
        leaveRequest.setEmployeeId(employeeId);
        leaveRequestRepository.save(leaveRequest);

        // 发送通知
        employeeRepository.findAll().forEach(employee -> {
            if (employee.getPositionId() == 4){// 向生产主管发送通知
                messageRepository.save(Message.leaveMessage(employeeId,employee.getId()));
            }
        });
        messageRepository.save(Message.leaveMessage(employeeId,5));// 向管理员发送通知
        // 提交审批
        employeeRepository.findAll().forEach(employee -> {
            if (employee.getPositionId() == 4){
                approvalRecordRepository.save(
                        ApprovalRecord
                                .requestApproval(leaveRequest.getId(),
                                        leaveRequest.getReason(),"leave",
                                        employee.getId()));
            }
        });

    }

    // 审批请假请求
    @PreAuthorize("hasAnyRole('ROLE_管理员', 'ROLE_人事主管')")
    public void updateApprovalRecord(Integer id,
                                String approvalComments,
                                String approvalStatus) {

        // 更新审批记录
        ApprovalRecord approvalRecord = approvalRecordRepository
                .findApprovalRecordByIdAndApprovalType(id,"leave");
        approvalRecord.setApprovalStatus(approvalStatus);
//        approvalRecord.setApprovalComments(approvalComments);
        approvalRecordRepository.save(approvalRecord);


        // 同步更新请假申请的状态
        LeaveRequest leaveRequest = leaveRequestRepository
                .findById(approvalRecord.getRequestId()).get();
        leaveRequest.setStatus(approvalStatus);
        // 更新考勤记录
        if (approvalStatus.equals("批准")) {
            AttendanceRecord attendanceRecord = new AttendanceRecord();
            attendanceRecord.setCheckInTime(LocalDateTime.now());
            attendanceRecord.setCheckOutTime(LocalDateTime.now());
            attendanceRecord.setStatus("请假");
            attendanceRecord.setEmployeeId(leaveRequest.getEmployeeId());
            attendanceRecordService.createAttendanceRecord(attendanceRecord);
        }

        // 发送通知
        Message message = new Message();
        message.setSenderId(approvalRecord.getApproverId());
        message.setReceiverId(leaveRequest.getEmployeeId());
        message.setSubject(leaveRequest.getStartDate() + "请假申请" + approvalRecord.getApprovalStatus());
        message.setRead(false);
        message.setContent(approvalComments);
        messageRepository.save(message);

        leaveRequestRepository.save(leaveRequest);
    }

    // 获取请假申请详细信息
    public LeaveRequest getLeaveRequest(Integer leaveRequestId) {
        return leaveRequestRepository.findById(leaveRequestId).get();
    }


    // 获取请假请求
    public List<LeaveRequest> getLeaveRequests(Integer employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    // 获取所有请假请求
    public Page<LeaveRequest> getLeaveRequests(Pageable pageable, Integer employeeId) {
        Page<LeaveRequest> allByEmployeeId = leaveRequestRepository.findAllByEmployeeId(employeeId, pageable);
        return allByEmployeeId;
    }
    public Page<LeaveRequest> getLeaveRequests(Pageable pageable) {
        Page<LeaveRequest> allByEmployeeId = leaveRequestRepository.findAll(pageable);
        return allByEmployeeId;
    }

    // 删除请假请求
    public void deleteLeaveRequest(Integer leaveRequestId) {
        leaveRequestRepository.deleteById(leaveRequestId);

        // 同时删除审批记录
        ApprovalRecord approvalRecord = approvalRecordRepository
                .findByRequestIdAndApprovalType(leaveRequestId, "leave");
        approvalRecordRepository.delete(approvalRecord);
    }

    // 新建请求请求
    public void createLeaveRequest(LeaveRequest leaveRequest){
        leaveRequestRepository.save(leaveRequest);
    }

    // 更新请假请求
    public void updateLeaveRequest(LeaveRequest leaveRequest){
        leaveRequestRepository.save(leaveRequest);
    }
}
