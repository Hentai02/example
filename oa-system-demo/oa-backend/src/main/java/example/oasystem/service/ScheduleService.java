package example.oasystem.service;

import example.oasystem.entries.Employee;
import example.oasystem.entries.Schedule;
import example.oasystem.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 获取日程
//    public List<Schedule> getSchedules(Pageable pageable,Integer employeeId) {
//        return scheduleRepository.findByEmployeeId(employeeId);
//
//    }

    // 获取所有日程
    public Page<Schedule> getSchedules(Pageable pageable, Integer employeeId) {
        Page<Schedule> iterable = scheduleRepository.findByEmployeeId(pageable,employeeId);
        return iterable;
    }

    public List<Schedule> getAllSchedules() {
        List<Schedule> iterable = new ArrayList<>();
        scheduleRepository.findAll().forEach(iterable::add);
        return iterable;

    }

    public Page<Schedule> searchSchedule(String keyword, Pageable pageable,Integer employeeId) {
        Page<Schedule> schedules = scheduleRepository
                .findByTitleContainingAndEmployeeId(keyword, pageable,employeeId);

        return schedules;
    }


    // 创建日程
    public Schedule createSchedule(Schedule schedule,Integer employeeId) {
        schedule.setEmployeeId(employeeId);
        return scheduleRepository.save(schedule);
    }

    // 删除日程
    public void deleteSchedule(Integer id) {
        scheduleRepository.deleteById(id);
    }

    // 更新日程
    public void updateSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }





}
