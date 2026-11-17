package example.oasystem.service;

import example.oasystem.entries.Message;
import example.oasystem.entries.Schedule;
import example.oasystem.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // 获取所有通知
    public List<Message> getMessages() {
        Iterable<Message> iterable = messageRepository.findAll();
        List<Message> messages = new ArrayList<>();
        iterable.forEach(messages::add);
        return messages;
    }

    // 获取员工收发的通知
    public Page<Message> getMessages(Integer receiverId,Pageable pageable) {
        Page<Message> messages = messageRepository
                .findBySenderIdOrReceiverId(receiverId, receiverId, pageable);
        return messages;
    }
    public Page<Message> searchMessages(Integer receiverId,
                                        String keyword,
                                        Pageable pageable) {
        Page<Message> messages = messageRepository
                .findBySubjectContainingAndReceiverIdOrSenderId(keyword, receiverId, receiverId, pageable);
        return messages;
    }

    // 获取通知详细
    public Message getMessage(Integer id) {
        Message message = messageRepository.findById(id).orElse(null);
        return message;
    }

    // 创建通知
    public void createMessage(Message message, Integer senderId) {
        message.setSenderId(senderId);
        messageRepository.save(message);
    }

    // 删除通知
    public void deleteMessage(Integer id) {
        messageRepository.deleteById(id);
    }

    // 更新通知
    public void updateMessage(Message message) {
        messageRepository.save(message);
    }




}
