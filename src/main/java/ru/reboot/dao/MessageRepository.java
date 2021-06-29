package ru.reboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.reboot.dao.entity.MessageEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {

    List<MessageEntity> findAllBySenderAndRecipientAndMessageTimestampAfter(String sender, String recipient, LocalDateTime messageTime);

    List<MessageEntity> findAllByRecipient(String userId);

    long deleteBySenderAndRecipientIn(String sender, List<String> recipients);

    long deleteBySenderInAndRecipient(List<String> senders, String recipient);
}
