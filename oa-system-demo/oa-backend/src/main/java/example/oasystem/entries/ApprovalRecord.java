package example.oasystem.entries;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

// 请假审批记录表
@Table(name = "approval_records")
public class ApprovalRecord {
    @Id
    private Integer id;
    private Integer requestId;
    private Integer approverId;
    private String approvalStatus;
    private String approvalComments;
    private String approvalType;

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalComments() {
        return approvalComments;
    }

    public void setApprovalComments(String approvalComments) {
        this.approvalComments = approvalComments;
    }

    public static ApprovalRecord requestApproval(Integer requestId,
                                                 String reason,
                                                 String approvalType,
                                                 Integer approverId) {
        ApprovalRecord record = new ApprovalRecord();
        record.setRequestId(requestId);
        record.setApproverId(approverId);
        record.setApprovalType(approvalType);
        record.setApprovalStatus("待审批");
        record.setApprovalComments(reason);
        return record;
    }

}
