package ru.reboot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.reboot.dto.UserInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;
import ru.reboot.service.ChatEngineServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateUserChatEngineService {
    private ChatEngineServiceImpl chatEngineService;

    @Before
    public void Init(){
        chatEngineService = Mockito.mock(ChatEngineServiceImpl.class);
    }


    @Test
    public void negativeUserNoLogin(){
        UserInfo user = new UserInfo.Builder().setUserID("1").setPassword("pass").setRoles(new ArrayList<String>(Arrays.asList("role1"))).build();
        try{
            chatEngineService.createUser(user);
            Assert.fail();
        }
        catch (BusinessLogicException exception){
            Assert.assertEquals(exception.getCode(), ErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    @Test
    public void negativeUserNoUserid(){
        UserInfo user = new UserInfo.Builder().setLogin("login").setPassword("pass").setRoles(new ArrayList<String>(Arrays.asList("role1"))).build();
        try{
            chatEngineService.createUser(user);
            Assert.fail();
        }
        catch (BusinessLogicException exception){
            Assert.assertEquals(exception.getCode(),ErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    @Test
    public void negativeUserNoPassword(){
        UserInfo user = new UserInfo.Builder().setLogin("login").setUserID("1").setRoles(new ArrayList<String>(Arrays.asList("role1"))).build();
        try{
            chatEngineService.createUser(user);
            Assert.fail();
        }
        catch (BusinessLogicException exception){
            Assert.assertEquals(exception.getCode(),ErrorCode.ILLEGAL_ARGUMENT);
        }
    }
}
