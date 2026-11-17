package example.oasystem.repository;

import example.oasystem.entries.AttendanceRecord;
import example.oasystem.entries.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRecordRepository extends CrudRepository<AttendanceRecord,Integer> {
    AttendanceRecord findByCheckInTimeContains(LocalDateTime checkInTime);

    // 获取当日考勤记录
    @Query("SELECT * FROM attendance_records WHERE employee_id = :employeeId AND DATE(check_in_time) = CURRENT_DATE")
    AttendanceRecord findTodayRecordsByEmployeeId(@Param("employeeId") Integer employeeId);

    List<AttendanceRecord> findByEmployeeId(Integer employeeId);


    Page<AttendanceRecord> findByEmployeeIdContaining(String keyword, Pageable pageable);

    Page<AttendanceRecord> findAll(Pageable pageable);

    Page<AttendanceRecord> findAllByEmployeeId(Integer employeeId, Pageable pageable);
}
