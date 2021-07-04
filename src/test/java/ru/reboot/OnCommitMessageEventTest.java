package ru.reboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dto.CommitMessageEvent;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;
import ru.reboot.service.ChatEngineServiceImpl;
import ru.reboot.service.UserCache;


public class OnCommitMessageEventTest {
    private ChatEngineServiceImpl chatEngineService;
    private MessageRepository messageRepository;

    @Before
    public void Init(){
        messageRepository = Mockito.mock(MessageRepository.class);
        chatEngineService = Mockito.spy(new ChatEngineServiceImpl());
        chatEngineService.setMessageRepository(messageRepository);
    }

    @Test
    public void negativeNoMessageIds() throws JsonProcessingException {
        CommitMessageEvent commitMessageEvent = new CommitMessageEvent();
        ObjectMapper mapper = new ObjectMapper();
        try{
            String raw = mapper.writeValueAsString(commitMessageEvent);
            chatEngineService.onCommitMessageEvent(raw);
            Assert.fail();
        }
        catch (BusinessLogicException exp){
            Assert.assertEquals(exp.getCode(), ErrorCode.ILLEGAL_ARGUMENT);
        }

    }
}
