package example.oasystem.entries;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "message")
public class Message {
    @Id
    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private String subject;
    private Boolean read;
//    private LocalDateTime createdAt;
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public static Message leaveMessage(Integer senderId,Integer receiverId) {
        Message message = new Message();
        message.setSubject("来自工号：" + senderId + "的请假申请，请及时处理！");
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setRead(false);
        return message;
    }

    public static Message reimbursementMessage(Integer senderId,Integer receiverId) {
        Message message = new Message();
        message.setSubject("来自工号：" + senderId + "的报销申请，请及时处理！");
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setRead(false);
        return message;
    }

    public static Message overtimeMessage(Integer senderId,Integer receiverId) {
        Message message = new Message();
        message.setSubject("来自工号：" + senderId + "的加班申请，请及时处理！");
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setRead(false);
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
