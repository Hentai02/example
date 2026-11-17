package example.oasystem.controller;

import example.oasystem.entries.Employee;
import example.oasystem.entries.FileItem;
import example.oasystem.entries.FilePermission;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.service.FileItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileItemController {

    private String basePath = "D:\\Code\\IdeaProjects\\oa-system\\data\\";
    private final FileItemService fileItemService;
    private final EmployeeRepository employeeRepository;

    public FileItemController(FileItemService fileItemService,
                              EmployeeRepository employeeRepository) {
        this.fileItemService = fileItemService;
        this.employeeRepository = employeeRepository;
    }

    // 获取所有文件
    @GetMapping("/all")
    private ResponseEntity<List<FileItem>> getFileItems() {
        List<FileItem> fileItems = fileItemService.getFileItems();
        return ResponseEntity.ok(fileItems);
    }

    // 获取该员工文件列表
    @GetMapping()
    private ResponseEntity<List<FileItem>> getFileItems(
            @RequestParam(value = "path", required = false) String path,
                                                        Principal principal){

        Employee employee = employeeRepository
                .findByUsername(principal.getName());
        List<FileItem> fileItems = fileItemService.getFileItems(employee.getId(),path);
        return ResponseEntity.ok(fileItems);
    }

    // 创建文件
//    @PostMapping
//    private ResponseEntity<Void> createFileItem(@RequestBody FileItem fileItem,Principal principal) {
//        Employee employee = employeeRepository.findByUsername(principal.getName());
//        fileItemService.createFileItem(fileItem,employee.getId());
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/create-file")
    private ResponseEntity<Void> createFileItem(@RequestParam("fileName") String fileName,
                                                @RequestParam("path") String path,
                                                Principal principal) {
        Employee employee = employeeRepository.findByUsername(principal.getName());
        fileItemService.createFileItem(fileName,employee.getId(),path);
        return ResponseEntity.ok().build();
    }

    // 更新文件
    @PutMapping
    private ResponseEntity<Void> updateFileItem(@RequestBody FileItem fileItem) {
        fileItemService.updateFileItem(fileItem);
        return ResponseEntity.ok().build();
    }

    // 删除文件
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteFileItem(@PathVariable Integer id) {
        fileItemService.deleteFileItem(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping()
    private ResponseEntity<Void> deleteFileItem(@RequestParam("file-name") String fileName,@RequestParam("path") String path) {
        fileItemService.deleteFileItem(fileName,path);
        return ResponseEntity.ok().build();
    }

    // 分配权限
    @PostMapping("/assign-permissions")
    private ResponseEntity<Void> assignPermissions(@RequestBody FilePermission filePermission) {
        fileItemService.updateFilePermission(filePermission);
        return ResponseEntity.ok().build();
    }

    // 获取员工文件权限
    @GetMapping("/file-permissions")
    private ResponseEntity<FilePermission> getEmployeeFilePermissions(@RequestParam("employee_id") Integer employeeId,
                                                                      @RequestParam("file_id") Integer fileId) {
        FilePermission filePermission = fileItemService.getEmployeeFilePermission(employeeId, fileId);
        return ResponseEntity.ok(filePermission);
    }

    // 获取文件所有共享员工
    @GetMapping("/file-employees")
    private ResponseEntity<List<FilePermission>> getFileEmployees(@RequestParam("file_id") Integer fileId) {
        List<FilePermission> fileEmployees = fileItemService.getFileEmployees(fileId);
        return ResponseEntity.ok(fileEmployees);

    }
}
