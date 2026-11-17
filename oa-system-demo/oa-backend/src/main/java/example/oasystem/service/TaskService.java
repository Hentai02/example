package example.oasystem.service;

import example.oasystem.entries.Task;
import example.oasystem.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 获取任务列表
    public Page<Task> getTasks(Integer employeeId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByCreatorId(employeeId,pageable);
        return tasks;
    }
    public Page<Task> searchTask(Integer employeeId, Pageable pageable,String keyword) {
        Page<Task> tasks = taskRepository
                .findByTitleContainingAndCreatorId(keyword, employeeId, pageable);
        return tasks;
    }
    public List<Task> getTaskByAssignee(Integer employeeId) {
        List<Task> tasks = taskRepository.findByAssigneeId(employeeId);
        return tasks;
    }

    // 创建任务
    public void createTask(Task task, Integer employeeId) {
        task.setCreatorId(employeeId);
        taskRepository.save(task);
    }

    // 更新任务
    public void updateTask(Task task) {
        taskRepository.save(task);
    }

    // 删除任务
    public void deleteTask(Integer taskId) {
        taskRepository.deleteById(taskId);
    }

    // 获取任务信息详情
    public Task getTask(Integer taskId) {
        taskRepository.findById(taskId);
        return taskRepository.findById(taskId).get();
    }

}
