package ru.reboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.reboot.dao.entity.MessageEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {
    @Transactional
    long deleteBySenderAndRecipientIn(String sender, List<String> recipients);
    @Transactional
    long deleteBySenderInAndRecipient(List<String> senders, String recipient);
}
