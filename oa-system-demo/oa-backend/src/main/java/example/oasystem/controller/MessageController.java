package example.oasystem.controller;

import example.oasystem.entries.Employee;
import example.oasystem.entries.Message;
import example.oasystem.entries.Schedule;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final EmployeeRepository employeeRepository;

    public MessageController(MessageService messageService, EmployeeRepository employeeRepository) {
        this.messageService = messageService;
        this.employeeRepository = employeeRepository;
    }

    // 获取所有通知
    @GetMapping("/all")
    public ResponseEntity<List<Message>> getMessages() {
        List<Message> messages = messageService.getMessages();
        return ResponseEntity.ok(messages);
    }

    // 获取员工收发通知
    @GetMapping()
    public ResponseEntity<Page<Message>> getMessages(Principal principal, Pageable pageable,
                                                     @RequestParam(required = false) String keyword) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        Page<Message> messages;
        if (keyword != null) {
            messages = messageService.searchMessages(employee.getId(), keyword,pageable);
        } else {
            messages = messageService.getMessages(employee.getId(),
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSortOr(Sort.by(Sort.Direction.DESC, "created_at"))));
        }
        return ResponseEntity.ok(messages);
    }

    // 创建通知
    @PostMapping
    public ResponseEntity<Void> createMessage(@RequestBody Message message,
                                              Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        messageService.createMessage(message,employee.getId());
        return ResponseEntity.ok().build();
    }

    // 删除通知
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }

    // 更新通知
    @PutMapping
    public ResponseEntity<Void> updateMessage(@RequestBody Message message) {
        messageService.updateMessage(message);
        return ResponseEntity.ok().build();
    }
}
