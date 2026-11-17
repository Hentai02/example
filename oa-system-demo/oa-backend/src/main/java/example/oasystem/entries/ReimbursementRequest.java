package example.oasystem.entries;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "reimbursement_requests")
public class ReimbursementRequest {
    @Id
    private Integer id;      // 报销单ID
    private Integer employeeId;           // 员工ID
    private String reimbursementType;     // 报销类别
    private Double totalAmount;       // 报销总金额
    private String reimbursementReason;   // 报销事由
    private String status;                // 报销单状态
    private String attachmentUrl;         // 附件URL

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }


    public String getReimbursementType() {
        return reimbursementType;
    }

    public void setReimbursementType(String reimbursementType) {
        this.reimbursementType = reimbursementType;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReimbursementReason() {
        return reimbursementReason;
    }

    public void setReimbursementReason(String reimbursementReason) {
        this.reimbursementReason = reimbursementReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
}
