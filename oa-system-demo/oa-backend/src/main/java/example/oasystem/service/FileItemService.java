package example.oasystem.service;

import example.oasystem.entries.Employee;
import example.oasystem.entries.FileItem;
import example.oasystem.entries.FilePermission;
import example.oasystem.repository.EmployeeRepository;
import example.oasystem.repository.FileRepository;
import example.oasystem.repository.FilePermissionRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileItemService {
    private String filePath = "D:\\Code\\IdeaProjects\\oa-system\\data";
    private final FileRepository fileItemRepository;
    private final FilePermissionRepository filePermissionRepository;
    private final EmployeeRepository employeeRepository;
    public FileItemService(FileRepository fileItemRepository,
                           FilePermissionRepository filePermissionRepository,
                           EmployeeRepository employeeRepository) {
        this.fileItemRepository = fileItemRepository;
        this.filePermissionRepository = filePermissionRepository;
        this.employeeRepository = employeeRepository;
    }

    //获取所有文件列表
    public List<FileItem> getFileItems() {
        Iterable<FileItem> iterable = fileItemRepository.findAll();
        List<FileItem> fileItems = new ArrayList<FileItem>();
        iterable.forEach(fileItems::add);
        return fileItems;
    }

    // 获取文件列表
    public List<FileItem> getFileItems(Integer employeeId,String path) {
        List<FileItem> fileItems = new ArrayList<>();
        File file = new File(filePath + path);
        File[] files = file.listFiles();
        if (path.equals("/")){
            List<FilePermission> filePermissions = filePermissionRepository
                    .findByEmployeeId(employeeId);
            filePermissions.forEach(filePermission -> {
                fileItemRepository.findById(filePermission.getFileId()).ifPresent(fileItem -> {
                    fileItems.add(fileItem);
                });
            });
            return fileItems;

        }else {
            for (File f : files) {
                FileItem fileItem = new FileItem();
                fileItem.setFilePath(path + "//" + f.getName());
                fileItem.setFileSize(f.length());
                fileItem.setIsFile(f.isFile());
                fileItem.setName(f.getName());
                fileItems.add(fileItem);
            }
            return fileItems;
        }
    }

    // 获取文件详情
    public FileItem getFileItem(Integer fileItemId) {
        return fileItemRepository.findById(fileItemId).get();
    }

    // 创建文件
    public void createFileItem(FileItem fileItem,
                                   Integer uploadEmployeeId) {
        File file = new File(fileItem.getFilePath());
        file.mkdirs();

        Integer fileId = fileItemRepository.save(fileItem).getId();

        FilePermission filePermission = new FilePermission();
        filePermission.setFileId(fileId);
        filePermission.setRead(true);
        filePermission.setWrite(true);
        filePermissionRepository.save(filePermission);

    }

    @PreAuthorize("hasAnyRole('管理员', '组长')")
    public void createFileItem(String fileName,Integer employeeId,String path) {
        File file = new File(filePath + "\\" + path + "\\" +   fileName);
        file.mkdirs();
        if (path.equals("/")){
            FileItem fileItem = new FileItem();
            fileItem.setFilePath("//" + fileName);
            fileItem.setFileSize(file.length());
            fileItem.setIsFile(file.isFile());
            fileItem.setName(fileName);
//        fileItem.setCreatedAt(LocalDateTime.now());
            FileItem saved = fileItemRepository.save(fileItem);

            FilePermission filePermission = new FilePermission();
            filePermission.setFileId(saved.getId());
            filePermission.setRead(true);
            filePermission.setWrite(true);
            filePermission.setEmployeeId(employeeId);
            filePermissionRepository.save(filePermission);
        }

    }
    // 判断是否是根目录的方法
    private boolean isRootDirectory(String filePath) {
        // 假设根目录是 "/"
        return "/data/".equals(filePath);
    }

    // 更新文件
    public void updateFileItem(FileItem fileItem){
        FileItem oldFileItem = fileItemRepository.findById(fileItem.getId()).get();
        String newFilename = fileItem.getName();
        String oldFilename = oldFileItem.getName();
        if (newFilename.equals(oldFilename)) {
            File file = new File(oldFileItem.getFilePath());
            file.renameTo(new File(newFilename));
        }
        fileItemRepository.save(fileItem);
    }

    // 删除文件
    @PreAuthorize("hasAnyRole('ROLE_管理员', 'ROLE_组长','ROLE_生产主管')")
    public void deleteFileItem(Integer fileItemId) {
        FileItem fileItem = fileItemRepository.findById(fileItemId).get();
        File file = new File(filePath + fileItem.getFilePath());
        file.delete();
//        filePermissionRepository.deleteByFileId(fileItemId);
        filePermissionRepository.findByFileId(fileItemId).forEach(filePermission -> {
            if (filePermission.getFileId().equals(fileItemId)) {
                filePermissionRepository.delete(filePermission);
            }
        });

        fileItemRepository.deleteById(fileItemId);

    }
    @PreAuthorize("hasAnyRole('ROLE_管理员', 'ROLE_组长','ROLE_生产主管')")
    public void deleteFileItem(String fileName,String path) {
        File file = new File(filePath + "/" + path + "/" + fileName);
        file.delete();
    }

    // 更新权限
    @PreAuthorize("hasAnyRole('ROLE_管理员', 'ROLE_组长','ROLE_生产主管')")
    public void updateFilePermission(FilePermission filePermission){
        if (filePermission.getRead() && filePermission.getWrite()){
            filePermissionRepository.save(filePermission);
        }else {
            filePermissionRepository.delete(filePermission);
        }

    }

    // 获取员工文件的权限
    public FilePermission getEmployeeFilePermission(Integer employeeId,Integer fileId) {
        FilePermission filePermission = filePermissionRepository.findByFileIdAndEmployeeId(fileId, employeeId);
        return filePermission;
    }

    // 获取文件所有访问员工
    public List<FilePermission> getFileEmployees(Integer fileId){
        List<FilePermission> filePermissions = filePermissionRepository
                .findByFileId(fileId);
        return filePermissions;
    }
}
