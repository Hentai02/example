package example.oasystem.repository;

import example.oasystem.entries.OvertimeRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OvertimeRequestRepository extends CrudRepository<OvertimeRequest, Integer> {
    List<OvertimeRequest> findByEmployeeId(Integer employeeId);
}
