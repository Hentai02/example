package example.oasystem.repository;

import example.oasystem.entries.FileItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileRepository extends CrudRepository<FileItem, Integer> {
    FileItem findByName(String name);
}
