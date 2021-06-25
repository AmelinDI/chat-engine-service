package ru.reboot.service;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dao.entity.MessageEntity;
import ru.reboot.dto.MessageInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class GetMessagesAndGetChatInfoChatEngineServiceImplTest {
    @InjectMocks
    private ChatEngineServiceImpl chatEngineService;

    @Mock
    private MessageRepository messageRepository;

    @Test
    public void negativeGetMessagesTestOneStringParamIsEmpty() {

        MockitoAnnotations.initMocks(this);
        try {
            chatEngineService.getMessages("", "ddddd", LocalDateTime.now());
            Assert.fail();
        } catch (BusinessLogicException ex) {
            Assert.assertEquals(ErrorCode.ILLEGAL_ARGUMENT, ex.getCode());
        }
    }

    @Test
    public void negativeTwoGetMessagesTestOneStringParamIsNull() {
        MockitoAnnotations.initMocks(this);
        try {
            chatEngineService.getMessages("aa", null, LocalDateTime.now());
            Assert.fail();
        } catch (BusinessLogicException ex) {
            Assert.assertEquals(ErrorCode.ILLEGAL_ARGUMENT, ex.getCode());
        }
    }


    @Test
    public void positiveGetMessagesOneTest() {
        MockitoAnnotations.initMocks(this);
        List<MessageEntity> messageEntitiesRecinerSender = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MessageEntity testMessage = new MessageEntity();
            testMessage.setId("id" + i);
            testMessage.setSender("sender");
            testMessage.setContent("content" + i);
            testMessage.setRecipient("recipient");
            testMessage.setLastAccessTime(LocalDateTime.now());
            messageEntitiesRecinerSender.add(testMessage);
        }
        List<MessageEntity> messageEntitiesSenderReciner = new ArrayList<>();
        MessageEntity testMessage = new MessageEntity();
        testMessage.setId("id");
        testMessage.setSender("recipient");
        testMessage.setContent("content");
        testMessage.setRecipient("sender");
        testMessage.setLastAccessTime(LocalDateTime.now());
        messageEntitiesSenderReciner.add(testMessage);

        when(messageRepository
                .findAllBySenderAndRecipientAndMessageTimestampAfter("sender", "recipient", LocalDateTime.of(2001, 3, 3, 1, 1)))
                .thenReturn(messageEntitiesRecinerSender);
        when(messageRepository
                .findAllBySenderAndRecipientAndMessageTimestampAfter("recipient", "sender", LocalDateTime.of(2001, 3, 3, 1, 1)))
                .thenReturn(messageEntitiesSenderReciner);

        List<MessageInfo> messageInfos = chatEngineService.getMessages("sender", "recipient", LocalDateTime.of(2001, 3, 3, 1, 1));
        Assert.assertEquals(4, messageInfos.size());
    }

    @Test
    public void positiveGetMessagesTwoTest() {
        MockitoAnnotations.initMocks(this);
        List<MessageEntity> messageEntitiesRecinerSender = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MessageEntity testMessage = new MessageEntity();
            testMessage.setId("id" + i);
            testMessage.setSender("sender");
            testMessage.setContent("content" + i);
            testMessage.setRecipient("recipient");
            testMessage.setMessageTimestamp(LocalDateTime.of(2021, 06, 24, 19, 50, 46));
            messageEntitiesRecinerSender.add(testMessage);
        }
        MessageEntity testMessage = new MessageEntity();
        testMessage.setId("id");
        testMessage.setSender("sender");
        testMessage.setContent("content");
        testMessage.setRecipient("recipient");
        testMessage.setMessageTimestamp(LocalDateTime.of(2021, 06, 21, 19, 55, 46));
        messageEntitiesRecinerSender.add(testMessage);

        when(messageRepository
                .findAllBySenderAndRecipientAndMessageTimestampAfter("sender", "recipient",  LocalDateTime.of(2021, 06, 24, 18, 55, 46)))
                .thenReturn(messageEntitiesRecinerSender);

        List<MessageInfo> messageInfos = chatEngineService.getMessages("sender", "recipient", LocalDateTime.of(2021, 06, 24, 18, 55, 46));
        Assert.assertEquals(4, messageInfos.size());
    }

    @Test
    public void negativeGetChatsInfoTest() {
        MockitoAnnotations.initMocks(this);
        try {
            chatEngineService.getChatsInfo("");
        } catch (BusinessLogicException ex) {
            Assert.assertEquals(ErrorCode.ILLEGAL_ARGUMENT, ex.getCode());
        }
    }


    @Test
    public void positiveGetChatsInfoTest() {
        MockitoAnnotations.initMocks(this);
        List<MessageEntity> messageEntities = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MessageEntity testMessage = new MessageEntity();
            testMessage.setId("id" + i);
            testMessage.setSender("sender" + i);
            testMessage.setContent("content" + i);
            testMessage.setRecipient("recipient");
            testMessage.setWasRead(false);
            testMessage.setLastAccessTime(LocalDateTime.now());
            messageEntities.add(testMessage);
        }

        MessageEntity testMessage = new MessageEntity();
        testMessage.setId("id");
        testMessage.setSender("sender");
        testMessage.setContent("content");
        testMessage.setRecipient("recipient");
        testMessage.setWasRead(true);
        testMessage.setLastAccessTime(LocalDateTime.now());
        messageEntities.add(testMessage);
        when(messageRepository
                .findAllByRecipient("recipient"))
                .thenReturn(messageEntities);
        Assert.assertEquals(3, chatEngineService
                .getChatsInfo("recipient")
                .stream()
                .filter(a -> a.getUnreadMessagesCount() > 0)
                .count());
    }
}