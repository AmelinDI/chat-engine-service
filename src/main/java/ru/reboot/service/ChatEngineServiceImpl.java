package ru.reboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.ChatInfo;
import ru.reboot.dto.CommitMessageEvent;
import ru.reboot.dto.MessageInfo;
import ru.reboot.dto.UserInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ChatEngineServiceImpl implements ChatEngineService {

    @Value("${client.auth-service}")
    private String authServiceUrl;

    private static final Logger logger = LogManager.getLogger(ChatEngineServiceImpl.class);

    private ObjectMapper mapper;
    private KafkaTemplate<String, String> kafkaTemplate;
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
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
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
            logger.info("Method .authorize userId={}.", userId);

            if (Objects.isNull(userId) || userId.length() == 0) {
                throw new BusinessLogicException("userID parameter of .authorize method is null or empty", ErrorCode.ILLEGAL_ARGUMENT);
            }

            userCache.setOnlineFlag(userId, true);

            RestTemplate restTemplate = new RestTemplate();

            MessageInfo[] allUserMessages = restTemplate.getForObject(messageStorageServiceURL + "/storage/message/allByUser?userId={userId}",
                    MessageInfo[].class, userId);

            if (allUserMessages == null) {
                throw new BusinessLogicException("allUserMessages in .loadAllUsers gets null", ErrorCode.DATABASE_ERROR);
            }

            for (MessageInfo messageInfo : allUserMessages) {
                MessageEntity messageEntity = convertMessageInfoToMessageEntity(messageInfo);
                messageRepository.save(messageEntity);
            }
            logger.info("Method .authorize completed userId={}.", userId);
        } catch (Exception e) {
            logger.error("Error to .authorize error={}.", userId);
            throw e;
        }
    }

    /**
     * Logout of user
     *
     * @param userId - user id.
     */
    @Transactional
    @Override
    public void logout(String userId) {
        logger.info("Method .logout userId={}", userId);
        try {
            userCache.setOnlineFlag(userId, false);
            long deletedMessages = 0;
            deletedMessages += messageRepository.deleteBySenderAndRecipientIn(userId, userCache.getOfflineUserIds());
            deletedMessages += messageRepository.deleteBySenderInAndRecipient(userCache.getOfflineUserIds(), userId);
            logger.info("Method .logout complete userId={}, number of deleted messages={}", userId, deletedMessages);
        } catch (Exception e) {
            logger.error("Method .getAllUsers error={}", e.getMessage(), e);
            throw e;
        }

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
                        .map(this::convertMessageEntityToMessageInfo)
                        .collect(Collectors.toList());

                messageInfos.addAll(messageRepository
                        .findAllBySenderAndRecipientAndMessageTimestampAfter(recipient, sender, lastSyncTime)
                        .stream()
                        .map(this::convertMessageEntityToMessageInfo)
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
                        .map(this::convertMessageEntityToMessageInfo)
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
                            .filter(b -> b.getSender().equalsIgnoreCase(a) && !b.getWasRead())
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

    /**
     * Get all users from chat-engince-service DB
     *
     * @return Returns list of UserInfo instances
     */
    @Override
    public List<UserInfo> getAllUsers() {
        logger.info("Method .getAllUsers");
        List<UserInfo> allUsersList;
        try {
            allUsersList = userCache.getAllUsers();
            if (allUsersList.size() == 0) {
                throw new BusinessLogicException("No users found", ErrorCode.USER_NOT_FOUND);
            }
            logger.info("Method .getAllUsers completed result={}", allUsersList);
            return allUsersList;
        } catch (Exception e) {
            logger.error("Method .getAllUsers error={}", e.getMessage(), e);
            throw e;
        }
    }

    private void addMessageToRecentMessages(MessageInfo message) {
        MessageEntity messageEntity = convertMessageInfoToMessageEntity(message);
        messageRepository.save(messageEntity);
    }

    private MessageInfo addMessageToStorage(MessageInfo message) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<MessageInfo> requestEntity = new HttpEntity<>(message);
        HttpEntity<MessageInfo> result = restTemplate.exchange(messageStorageServiceURL + "/storage/message", HttpMethod.PUT, requestEntity, MessageInfo.class);
        return result.getBody();
    }

    @KafkaListener(topics = CommitMessageEvent.TOPIC, groupId = "chat-engine-service", autoStartup = "${kafka.autoStartup}")
    public void onCommitMessageEvent(String raw) {

//        CommitMessageEvent event = mapper.readValue(raw, CommitMessageEvent.class);
        logger.info("<< Received: {}", raw);
    }

    /**
     * Initialisation UserCache with RestTemplate by "/auth/user/all" from  auth-service
     */
    @PostConstruct
    public void init() {
        logger.info("Method .init");
        try {
            logger.info("Method .loadAllUsers");
            loadAllUsers();
            logger.info("Method .loadAllUsers completed");
            logger.info("Method .init completed");
        } catch (Exception e) {
            logger.error("Method .init error={}", e.getMessage(), e);
            throw e;
        }

    }

    private void loadAllUsers() {
        RestTemplate restTemplate = new RestTemplate();

        UserInfo[] authDBUsers = restTemplate.getForObject(authServiceURL + "/auth/user/all", UserInfo[].class);

        if (authDBUsers == null) {
            throw new BusinessLogicException("authDBUsers in .loadAllUsers gets null", ErrorCode.DATABASE_ERROR);
        }

        for (UserInfo userInfo : authDBUsers) {
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
                .setWasRead(info.getWasRead())
                .build();
    }
}