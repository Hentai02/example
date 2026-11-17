package example.oasystem.repository;

import example.oasystem.entries.Message;
import example.oasystem.entries.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    List<Message> findBySenderId(Integer senderId);

    Iterable<Message> findByReceiverId(Integer receiverId);

    Page<Message> findBySubjectContainingAndReceiverIdOrSenderId(String subject, Integer receiverId, Integer senderId, Pageable pageable);

    Page<Message> findBySenderIdOrReceiverId(Integer senderId, Integer receiverId, Pageable pageable);
}
