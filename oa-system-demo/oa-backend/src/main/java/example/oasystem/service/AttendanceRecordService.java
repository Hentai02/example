package example.oasystem.service;

import example.oasystem.entries.AttendanceRecord;
import example.oasystem.entries.Employee;
import example.oasystem.repository.AttendanceRecordRepository;
import example.oasystem.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service// 考勤管理
public class AttendanceRecordService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceRecordService(AttendanceRecordRepository attendanceRecordRepository,
                                   EmployeeRepository employeeRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.employeeRepository = employeeRepository;
    }

    // 获取考勤记录
    public List<AttendanceRecord> getAttendanceRecords(Integer employeeId) {
        return attendanceRecordRepository.findByEmployeeId(employeeId);
    }

    // 获取所有考勤记录
    public Page<AttendanceRecord> getAttendanceRecords(Pageable pageable,Integer employeeId) {
        Page<AttendanceRecord> all = attendanceRecordRepository.findAllByEmployeeId(employeeId,pageable);
        return all;
    }
    public Page<AttendanceRecord> getAttendanceRecords(Pageable pageable) {
        return attendanceRecordRepository.findAll(pageable);
    }

    // 获取当日考勤记录
    public AttendanceRecord getAttendanceRecord(Integer employeeId) {
        return attendanceRecordRepository.findTodayRecordsByEmployeeId(employeeId);
    }

    // 新增考勤记录
    @PreAuthorize("hasRole('管理员')")
    public void createAttendanceRecord(AttendanceRecord attendanceRecord) {
        attendanceRecordRepository.save(attendanceRecord);
    }

    // 更新考勤记录
    @PreAuthorize("hasRole('管理员')")
    public void updateAttendanceRecord(AttendanceRecord attendanceRecord) {
        attendanceRecordRepository.save(attendanceRecord);
    }

    // 删除考勤记录
    @PreAuthorize("hasRole('管理员')")
    public void deleteAttendanceRecord(Integer id) {
        attendanceRecordRepository.deleteById(id);
    }

    // 签到
    public void checkIn(Integer employeeId,LocalDateTime checkInDate) {
        // 获取当日考勤记录
        AttendanceRecord attendanceRecord = attendanceRecordRepository.findTodayRecordsByEmployeeId(employeeId);
        if (attendanceRecord == null) attendanceRecord = new AttendanceRecord();
        //if (attendanceRecord.getCheckOutTime() != null) attendanceRecord = new AttendanceRecord();
        attendanceRecord.setEmployeeId(employeeId);
        if (attendanceRecord.getCheckInTime() == null) attendanceRecord.setCheckInTime(checkInDate);
        if (checkInDate.getHour() <= 8 && attendanceRecord.getCheckInTime() == null){
            attendanceRecord.setStatus("出勤");
        }else {
            attendanceRecord.setStatus("缺勤");
        }
        attendanceRecordRepository.save(attendanceRecord);
    }


    // 签退
    public void checkOut(Integer employeeId,LocalDateTime checkOutDate) {
        // 获取当日考勤记录
        AttendanceRecord attendanceRecord = attendanceRecordRepository
                .findTodayRecordsByEmployeeId(employeeId);
        if (attendanceRecord == null) return;
        attendanceRecord.setCheckOutTime(checkOutDate);
        if (checkOutDate.getHour() >= 18) {
            attendanceRecord.setStatus("出勤");
        }else {
            attendanceRecord.setStatus("迟到");
        }
        attendanceRecordRepository.save(attendanceRecord);
    }




}
