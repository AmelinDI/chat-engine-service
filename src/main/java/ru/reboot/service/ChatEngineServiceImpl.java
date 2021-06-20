package ru.reboot.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dto.ChatInfo;
import ru.reboot.dto.MessageInfo;
import ru.reboot.dto.UserInfo;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ChatEngineServiceImpl implements ChatEngineService {

    private static final Logger logger = LogManager.getLogger(ChatEngineServiceImpl.class);

    private UserCache userCache;
    private MessageRepository messageRepository;

    @Autowired
    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void authorize(String userId) {

    }

    @Override
    public void logout(String userId) {

    }

    @Override
    public MessageInfo send(MessageInfo message) {

        String receiver = message.getRecipient();

        message = addMessageToStorage(message);
        if (!userCache.isOnline(receiver)) {
            addMessageToRecentMessages(message);
        }

        return message;
    }

    @Override
    public List<MessageInfo> getMessages(String sender, String recipient, LocalDateTime lastSyncTime) {
        return null;
    }

    @Override
    public void commitMessages(List<String> messageIds) {

    }

    @Override
    public List<ChatInfo> getChatsInfo(String userId) {
        return null;
    }

    @Override
    public List<UserInfo> getAllUsers() {
        return null;
    }

    private void addMessageToRecentMessages(MessageInfo message) {
    }

    private MessageInfo addMessageToStorage(MessageInfo message) {
        return null;
    }

    @PostConstruct
    public void init() {
        loadAllUsers();
    }

    private void loadAllUsers() {
    }
}