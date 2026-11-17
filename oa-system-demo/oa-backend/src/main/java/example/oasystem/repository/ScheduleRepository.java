package example.oasystem.repository;

import example.oasystem.entries.Employee;
import example.oasystem.entries.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleRepository extends CrudRepository<Schedule, Integer> {
    Page<Schedule> findByEmployeeId(Pageable pageable,Integer employeeId);
    Page<Schedule> findByTitleContaining(String title, Pageable pageable);
    Page<Schedule> findAll(Pageable pageable);
    Page<Schedule> findByTitleContainingAndEmployeeId(String title,Pageable pageable, Integer employeeId);
}
