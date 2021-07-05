package ru.reboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.reboot.dao.entity.MessageEntity;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {

    List<MessageEntity> findAllBySenderAndRecipientAndMessageTimestampAfter(String sender, String recipient, LocalDateTime messageTime);

    List<MessageEntity> findAllByRecipient(String userId);

    long deleteBySenderAndRecipientIn(String sender, List<String> recipients);

    long deleteBySenderInAndRecipient(List<String> senders, String recipient);

    @Modifying
    @Transactional
    @Query("UPDATE MessageEntity m SET m.wasRead =TRUE ,m.readTime=CURRENT_TIMESTAMP where  m.id IN (:messageIds)")
    void updateWasReadByIds(@Param("messageIds") Collection<String> messageIds);
}
