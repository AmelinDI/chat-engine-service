package ru.reboot.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
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
import java.util.UUID;

@Component
public class ChatEngineServiceImpl implements ChatEngineService {

    private static final Logger logger = LogManager.getLogger(ChatEngineServiceImpl.class);

    private UserCache userCache;
    private MessageRepository messageRepository;

    @Value("${client.auth-service}")
    private String authServiceURL;

    @Value("${client.message-storage-service}")
    private String messageStorageServiceURL;


    @Autowired
    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Sets OnLine flag for user
     * Loads messages from messageStorageService to messageRepository
     *
     * @param userId - user id
     */
    @Override
    public void authorize(String userId) {
        try {
            logger.info("Method .authorize userId={}.",userId);

            if (Objects.isNull(userId) || userId.length() == 0) {
                throw new BusinessLogicException("userID parameter of .authorize method is null or empty", ErrorCode.ILLEGAL_ARGUMENT);
            }

            userCache.setOnlineFlag(userId, true);

            RestTemplate restTemplate = new RestTemplate();

            MessageInfo[] allUserMessages = restTemplate.getForObject(messageStorageServiceURL+"/storage/message/allByUser?userId={userId}",
                    MessageInfo[].class, userId);

            if (allUserMessages == null) {
                throw new BusinessLogicException("allUserMessages in .loadAllUsers gets null", ErrorCode.DATABASE_ERROR);
            }

            for (MessageInfo messageInfo: allUserMessages) {
                MessageEntity messageEntity = convertMessageInfoToMessageEntity(messageInfo);
                messageRepository.save(messageEntity);
            }
            logger.info("Method .authorize completed userId={}.",userId);
        } catch (Exception e) {
            logger.error("Error to .authorize error={}.",userId);
            throw e;
        }
    }

    @Override
    public void logout(String userId) {

    }

    /**
     * Sends message to Storage DB & to RecentMessage storage
     *
     * @param message - message
     * @return - MessageInfo or
     */
    @Override
    public MessageInfo send(MessageInfo message) {
        try {
            logger.info("Method .sent message={}.", message);

            if (Objects.isNull(message)) {
                throw new BusinessLogicException("Message parameter of .send method is null", ErrorCode.ILLEGAL_ARGUMENT);
            }
            String receiver = message.getRecipient();

            MessageInfo result = addMessageToStorage(message);
            addMessageToRecentMessages(message);

            logger.info("Method .send(MessageInfo message) completed userId={},result={}", message, result);
            return result;

        } catch (Exception e) {
            logger.error("Error to .send(MessageInfo message) error = {}", e.getMessage(), e);
            throw e;
        }

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
        MessageEntity messageEntity = convertMessageInfoToMessageEntity(message);
        messageRepository.save(messageEntity);
    }

    private MessageInfo addMessageToStorage(MessageInfo message) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<MessageInfo> requestEntity = new HttpEntity<>(message);
        HttpEntity<MessageInfo> result = restTemplate.exchange(messageStorageServiceURL+"/storage/message", HttpMethod.PUT, requestEntity, MessageInfo.class);
        return result.getBody();
    }

    @PostConstruct
    public void init() {
        loadAllUsers();
    }

    private void loadAllUsers() {
        RestTemplate restTemplate = new RestTemplate();

        UserInfo[] authDBUsers = restTemplate.getForObject(authServiceURL+"/auth/user/all", UserInfo[].class);

        if (authDBUsers == null) {
            throw new BusinessLogicException("authDBUsers in .loadAllUsers gets null", ErrorCode.DATABASE_ERROR);
        }

        for (UserInfo userInfo: authDBUsers) {
            userCache.addUser(userInfo);
        }
    }

    private MessageInfo convertMessageEntityToMessageInfo(MessageEntity entity) {
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

        String messageId = info.getId();
        if (Objects.isNull(messageId)) {
            messageId = UUID.randomUUID().toString();
        }
        return new MessageEntity.Builder()
                .setId(messageId)
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