package ru.reboot.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ChatEngineServiceImpl implements ChatEngineService {

    @Value("${client.auth-service}")
    private String authServiceUrl;

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

    /**
     * Logout of user
     * @param userId - user id.
     */
    @Transactional
    @Override
    public void logout(String userId) {
        logger.info("Method .logout userId={}",userId);
        try{
            userCache.setOnlineFlag(userId,false);
            long deletedMessages=0;
            deletedMessages+=messageRepository.deleteBySenderAndRecipientIn(userId,userCache.getOfflineUserIds());
            deletedMessages+=messageRepository.deleteBySenderInAndRecipient(userCache.getOfflineUserIds(),userId);
            logger.info("Method .logout complete userId={}, number of deleted messages={}",userId,deletedMessages);
        }
        catch (Exception e){
            logger.error("Method .getAllUsers error={}",e.getMessage(),e);
            throw e;
        }

    }

    @Override
    public MessageInfo send(MessageInfo message) {

        String receiver = message.getRecipient();

        message = addMessageToStorage(message);
        addMessageToRecentMessages(message);

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

    /**
     * Get all users from chat-engince-service DB
     * @return Returns list of UserInfo instances
     */
    @Override
    public List<UserInfo> getAllUsers() {
        logger.info("Method .getAllUsers");
        List<UserInfo> allUsersList;
        try{
            allUsersList = userCache.getAllUsers();
            if(allUsersList.size()==0){
                throw new BusinessLogicException("No users found", ErrorCode.USER_NOT_FOUND);
            }
            logger.info("Method .getAllUsers completed result={}",allUsersList);
            return allUsersList;
        }
        catch (Exception e){
            logger.error("Method .getAllUsers error={}",e.getMessage(),e);
            throw e;
        }
    }

    private void addMessageToRecentMessages(MessageInfo message) {
    }

    private MessageInfo addMessageToStorage(MessageInfo message) {
        return null;
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
        }
        catch (Exception e){
            logger.error("Method .init error={}",e.getMessage(),e);
            throw e;
        }

    }

    private void loadAllUsers() {
        RestTemplate restTemplate = new RestTemplate();
        String url = authServiceUrl + "/auth/user/all";
        ResponseEntity<UserInfo[]> responseEntity = restTemplate.getForEntity(url,UserInfo[].class);
        UserInfo[] userInfoArray = responseEntity.getBody();
        if(userInfoArray==null){
            throw new BusinessLogicException("Zero users was loaded",ErrorCode.USER_NOT_FOUND);
        }
        Arrays.stream(userInfoArray)
                .forEach(userInfoInstance -> userCache.addUser(userInfoInstance));

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