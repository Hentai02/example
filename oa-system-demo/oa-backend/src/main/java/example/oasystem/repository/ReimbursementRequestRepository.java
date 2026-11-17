package example.oasystem.repository;

import example.oasystem.entries.ReimbursementRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReimbursementRequestRepository extends CrudRepository<ReimbursementRequest, Integer> {
    List<ReimbursementRequest> findByEmployeeId(Integer employeeId);
}
