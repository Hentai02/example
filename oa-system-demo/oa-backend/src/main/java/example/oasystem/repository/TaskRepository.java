package example.oasystem.repository;

import example.oasystem.entries.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Integer> {

    Page<Task> findByCreatorId(Integer creatorId,Pageable pageable);

    Page<Task> findByTitleContainingAndCreatorId(String title, Integer creatorId, Pageable pageable);

    List<Task> findByAssigneeId(Integer assigneeId);
}
