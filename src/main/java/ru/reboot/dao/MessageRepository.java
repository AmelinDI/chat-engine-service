package ru.reboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.reboot.dao.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {
}
