package example.oasystem.controller.schedule;

import example.oasystem.entries.Employee;
import example.oasystem.entries.Schedule;
import example.oasystem.entries.Task;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final EmployeeRepository employeeRepository;
    public TaskController(TaskService taskService, EmployeeRepository employeeRepository) {
        this.taskService = taskService;
        this.employeeRepository = employeeRepository;
    }

    // 获取任务列表
    @GetMapping
    private ResponseEntity<Page<Task>> getTasks(Principal principal,
                                                Pageable pageable,
                                                @RequestParam(required = false) String keyword) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        Page<Task> tasks;
        if (keyword != null) {
            tasks = taskService.searchTask(employee.getId(), pageable,keyword);
        } else {
            tasks = taskService.getTasks(employee.getId(),
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSort()));
        }
        return ResponseEntity.ok(tasks);
    }

    // 获取任务列表
    @GetMapping("/assignee")
    private ResponseEntity<List<Task>> getTasksByAssignee(Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        List<Task> taskByAssignee = taskService.getTaskByAssignee(employee.getId());
        return ResponseEntity.ok(taskByAssignee);
    }

    // 获取任务详细详细
    @GetMapping("/{id}")
    private ResponseEntity<Task> getTask(@PathVariable Integer id) {
        Task task = taskService.getTask(id);
        return ResponseEntity.ok(task);
    }

    // 新建任务
    @PostMapping
    private ResponseEntity<Void> createTask(Principal principal, @RequestBody Task task) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        taskService.createTask(task, employee.getId());
        return ResponseEntity.ok().build();
    }

    // 更新任务
    @PutMapping
    private ResponseEntity<Void> updateTask(@RequestBody Task task) {
        taskService.updateTask(task);
        return ResponseEntity.ok().build();
    }

    // 删除任务
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }






}
