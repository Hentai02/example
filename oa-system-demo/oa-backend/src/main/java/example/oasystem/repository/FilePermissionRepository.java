package example.oasystem.repository;

import example.oasystem.entries.FilePermission;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FilePermissionRepository extends CrudRepository<FilePermission, Integer> {
    void deleteByFileId(Integer fileId);
    List<FilePermission> findByEmployeeId(Integer employeeId);

    List<FilePermission> findByFileId(Integer fileId);

//    FilePermission findByFileIdAnd(Integer fileId);

    FilePermission findByFileIdAndEmployeeId(Integer fileId, Integer employeeId);
}
