package ru.reboot;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.reboot.dao.MessageRepository;
import ru.reboot.dto.UserInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;
import ru.reboot.service.ChatEngineServiceImpl;
import ru.reboot.service.UserCache;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetAllUsersTest {
    private ChatEngineServiceImpl chatEngineService;
    private MessageRepository messageRepository;
    private UserCache userCache;

    @Before
    public void Init(){
        messageRepository = Mockito.mock(MessageRepository.class);
        userCache = Mockito.mock(UserCache.class);
        chatEngineService = Mockito.spy(new ChatEngineServiceImpl());
        chatEngineService.setMessageRepository(messageRepository);
        chatEngineService.setUserCache(userCache);
    }

    @Test
    public void negativeNoUsers(){
        List<UserInfo> userInfoList = new ArrayList<>();
        Mockito.when(userCache.getAllUsers()).thenReturn(userInfoList);
        try{
            ArrayList<UserInfo> infoArrayList = (ArrayList<UserInfo>) chatEngineService.getAllUsers();
            Assert.fail();
        }
        catch (BusinessLogicException exception){
            Assert.assertEquals(exception.getCode(), ErrorCode.USER_NOT_FOUND);
        }

    }

    @Test
    public void positiveGetAllUsers(){

        List<UserInfo> userInfoList = new ArrayList<>();
        UserInfo userInfo1 = new UserInfo.Builder()
                .setUserID("UserID1")
                .setFirstName("FirstName1")
                .setLastName("LastName1")
                .setSecondName("SecondName1")
                .setBirthDate(LocalDate.now())
                .setLogin("Login1")
                .setPassword("Password1")
                .setRoles(new ArrayList<>())
                .build();

        UserInfo userInfo2 = new UserInfo.Builder()
                .setUserID("UserID2")
                .setFirstName("FirstName2")
                .setLastName("LastName2")
                .setSecondName("SecondName2")
                .setBirthDate(LocalDate.now())
                .setLogin("Login2")
                .setPassword("Password2")
                .setRoles(new ArrayList<>())
                .build();

        userInfoList.add(userInfo1);
        userInfoList.add(userInfo2);

        Mockito.when(userCache.getAllUsers()).thenReturn(userInfoList);
        ArrayList<UserInfo> infoArrayList = (ArrayList<UserInfo>) chatEngineService.getAllUsers();

        for(int i=0;i<=infoArrayList.size()-1;i++){
            Assert.assertEquals(infoArrayList.get(i).getUserId(),userInfoList.get(i).getUserId());
            Assert.assertEquals(infoArrayList.get(i).getFirstName(),userInfoList.get(i).getFirstName());
            Assert.assertEquals(infoArrayList.get(i).getLastName(),userInfoList.get(i).getLastName());
            Assert.assertEquals(infoArrayList.get(i).getSecondName(),userInfoList.get(i).getSecondName());
            Assert.assertEquals(infoArrayList.get(i).getBirthDate(),userInfoList.get(i).getBirthDate());
            Assert.assertEquals(infoArrayList.get(i).getLogin(),userInfoList.get(i).getLogin());
            Assert.assertEquals(infoArrayList.get(i).getPassword(),userInfoList.get(i).getPassword());
            Assert.assertEquals(infoArrayList.get(i).getRoles(),userInfoList.get(i).getRoles());
        }

    }

}
