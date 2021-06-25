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
    public void positiveGetMessagesTest() {
        MockitoAnnotations.initMocks(this);
        List<MessageEntity> messageEntities = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MessageEntity testMessage = new MessageEntity();
            testMessage.setId("id" + i);
            testMessage.setSender("sender");
            testMessage.setContent("content" + i);
            testMessage.setRecipient("recipient");
            testMessage.setMessageTimestamp(LocalDateTime.now());
            messageEntities.add(testMessage);
        }
        when(messageRepository
                .findAllBySenderAndRecipientAndMessageTimestampAfter("sender", "recipient", LocalDateTime.of(2001, 3, 3, 1, 1)))
                .thenReturn(messageEntities);

        List<MessageInfo> messageInfos = chatEngineService.getMessages("sender", "recipient", LocalDateTime.of(2001, 3, 3, 1, 1));
        Assert.assertEquals(3, (int) messageInfos
                .stream()
                .filter(a -> a.getSender().equals("sender"))
                .filter(a -> a.getRecipient().equals("recipient")).count());
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
            messageEntities.add(testMessage);
        }
        MessageEntity testMessage = new MessageEntity();
        testMessage.setId("id");
        testMessage.setSender("sender");
        testMessage.setContent("content");
        testMessage.setRecipient("recipient");
        testMessage.setWasRead(true);
        messageEntities.add(testMessage);

        List<MessageEntity> messageEntitiesFromSendr = new ArrayList<>();
        MessageEntity messageEntityFromSender = new MessageEntity();
        messageEntityFromSender.setSender("recipient");
        messageEntityFromSender.setRecipient("re");
        messageEntityFromSender.setWasRead(false);
        messageEntitiesFromSendr.add(messageEntityFromSender);

        messageEntities.stream().forEach(System.out::println);
        System.out.println("-----------------------------------");
        messageEntitiesFromSendr.forEach(System.out::println);

        when(messageRepository
                .findAllBySender("recipient"))
                .thenReturn(messageEntitiesFromSendr);

        when(messageRepository
                .findAllByRecipient("recipient"))
                .thenReturn(messageEntities);

        Assert.assertEquals(4, chatEngineService
                .getChatsInfo("recipient")
                .stream()
                .filter(a -> a.getUnreadMessagesCount() > 0)
                .count());
    }
}