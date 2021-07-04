package ru.reboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.reboot.dao.entity.MessageEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {

    List<MessageEntity> findAllBySenderAndRecipientAndMessageTimestampAfter(String sender, String recipient, LocalDateTime messageTime);

    List<MessageEntity> findAllByRecipient(String userId);

    long deleteBySenderAndRecipientIn(String sender, List<String> recipients);

    long deleteBySenderInAndRecipient(List<String> senders, String recipient);

    @Query("UPDATE MessageEntity m set m.wasRead = true where m.id in:messageIds")
    long updateWasReadById(Collection<String> messageIds);
}
