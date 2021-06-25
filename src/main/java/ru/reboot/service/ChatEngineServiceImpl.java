package ru.reboot.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.ChatInfo;
import ru.reboot.dto.MessageInfo;
import ru.reboot.dto.UserInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        addMessageToRecentMessages(message);

        return message;
    }

    /**
     * Get messages form specific time up to current/
     *
     * @param sender       - sender user id
     * @param recipient    - recipient user id
     * @param lastSyncTime - last sync time
     */
    @Override
    public List<MessageInfo> getMessages(String sender, String recipient, LocalDateTime lastSyncTime) {
        logger.info("Method .getMessages sender={}, recipient={}, lastSyncTime={}.", sender, recipient, lastSyncTime);
        try {
            if (sender == null || sender.isEmpty() || recipient == null || recipient.isEmpty() || lastSyncTime == null) {
                throw new BusinessLogicException("Parameters are null or empty", ErrorCode.ILLEGAL_ARGUMENT);
            } else {
                List<MessageInfo> messageInfos = messageRepository
                        .findAllBySenderAndRecipientAndMessageTimestampAfter(sender, recipient, lastSyncTime)
                        .stream()
                        .map(ChatEngineServiceImpl::convertMessageEntityToMessageInfo)
                        .collect(Collectors.toList());

                messageInfos.addAll(messageRepository
                        .findAllBySenderAndRecipientAndMessageTimestampAfter(recipient, sender, lastSyncTime)
                        .stream()
                        .map(ChatEngineServiceImpl::convertMessageEntityToMessageInfo)
                        .collect(Collectors.toList()));
                logger.info("Method .getChatsInfo completed chats messages={}", messageInfos);
                return messageInfos;
            }
        } catch (Exception e) {
            logger.error("Error to .getAllMessages error = {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void commitMessages(List<String> messageIds) {

    }

    /**
     * Get operation information about user chats.
     *
     * @param userId - user id
     */
    @Override
    public List<ChatInfo> getChatsInfo(String userId) {
        logger.info("Method .getChatsInfo userId={} ", userId);
        try {
            if (Objects.isNull(userId) || userId.isEmpty()) {
                throw new BusinessLogicException("User id is empty or null", ErrorCode.ILLEGAL_ARGUMENT);
            } else {
                List<MessageInfo> messageInfos = messageRepository
                        .findAllByRecipient(userId)
                        .stream()
                        .map(ChatEngineServiceImpl::convertMessageEntityToMessageInfo)
                        .collect(Collectors.toList());
                Set<String> senderId = messageInfos
                        .stream()
                        .map(MessageInfo::getSender)
                        .collect(Collectors.toSet());
                List<ChatInfo> result = senderId.stream().map(a -> {
                    ChatInfo chatInfo = new ChatInfo();
                    chatInfo.setCompanion(a);
                    chatInfo.setUnreadMessagesCount(Math.toIntExact(messageInfos
                            .stream()
                            .filter(b -> b.getSender().equalsIgnoreCase(a) && !b.wasRead())
                            .count()));
                    return chatInfo;
                }).collect(Collectors.toList());
                logger.info("Method .getChatsInfo completed chats result={}", result);
                return result;
            }
        } catch (Exception e) {
            logger.error("Error to .getChatsInfo error={}", e.getMessage(), e);
            throw e;

        }
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

    private static MessageInfo convertMessageEntityToMessageInfo(MessageEntity entity) {
        return new MessageInfo.Builder()
                .setId(entity.getId())
                .setSender(entity.getSender())
                .setRecipient(entity.getRecipient())
                .setContent(entity.getContent())
                .setMessageTimestamp(entity.getMessageTimestamp())
                .setLastAccessTime(entity.getLastAccessTime())
                .setReadTime(entity.getReadTime())
                .setWasRead(entity.wasRead())
                .build();
    }

    private MessageEntity convertMessageInfoToMessageEntity(MessageInfo info) {
        return new MessageEntity.Builder()
                .setId(info.getId())
                .setSender(info.getSender())
                .setRecipient(info.getRecipient())
                .setContent(info.getContent())
                .setMessageTimestamp(info.getMessageTimestamp())
                .setLastAccessTime(LocalDateTime.now()) // текущее время!!
                .setReadTime(info.getReadTime())
                .setWasRead(info.wasRead())
                .build();
    }
}