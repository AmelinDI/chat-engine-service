package ru.reboot;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.reboot.dao.MessageRepository;
import ru.reboot.service.ChatEngineServiceImpl;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class AppTest {

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void mockTest() {

        List<String> mockList = Mockito.mock(List.class);
        Mockito.when(mockList.get(10)).thenReturn("Hello");

        Assert.assertEquals("Hello", mockList.get(10));
        Assert.assertNull(mockList.get(11));

        Mockito.verify(mockList).get(10);
        Mockito.verify(mockList).get(11);
    }

    // FIXME
    @Test
    public void mockRepositoryTest() {

        // prepare
        MessageRepository repositoryMock = Mockito.mock(MessageRepository.class);
//        Mockito.when(repositoryMock.getMessage(Mockito.anyString())).thenReturn(new MessageEntity());

        ChatEngineServiceImpl service = new ChatEngineServiceImpl();
        service.setMessageRepository(repositoryMock);

        // act
//        service.someMethod(....

        // verify
//        Assert.assertEquals(...
//        Assert.assertEquals(...
    }

    @Test
    public void testException() {

        String s = null;
        try {
            s.toLowerCase();
            Assert.fail();
        } catch (NullPointerException ex) {
            // expect null pointer exception
        }
    }
}
