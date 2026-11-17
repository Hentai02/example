package example.oasystem.repository;

import example.oasystem.entries.ApprovalRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApprovalRecordRepository extends CrudRepository<ApprovalRecord, Integer> {
    ApprovalRecord findApprovalRecordByIdAndApprovalType(Integer id, String approvalType);
    List<ApprovalRecord> findApprovalRecordsByApprovalTypeAndApproverId(String approvalType, Integer approverId);
    ApprovalRecord findByRequestIdAndApprovalType(Integer requestId, String approvalType);
}
