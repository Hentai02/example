package example.oasystem.repository;

import example.oasystem.entries.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository extends CrudRepository<LeaveRequest,Integer> {
//    LeaveRequest findById(int id);
    List<LeaveRequest> findByEmployeeId(Integer employeeId);

   Page<LeaveRequest> findAll(Pageable pageable);
    Page<LeaveRequest> findAllByEmployeeId(Integer employeeId, Pageable pageable);
}
