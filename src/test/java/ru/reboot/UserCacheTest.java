package ru.reboot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.reboot.dto.UserInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;
import ru.reboot.service.UserCache;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserCacheTest {
    private UserCache userCache;

    @Before
    public void Init(){
        userCache = Mockito.spy(UserCache.class);
    }

    @Test
    public void negativeSetOnlineFlagNoUser(){
        try{
            userCache.setOnlineFlag("userId",false);
            Assert.fail();
        }
        catch (BusinessLogicException e){
            Assert.assertEquals(e.getCode(), ErrorCode.USER_NOT_FOUND);
        }
    }

    @Test
    public void positiveSetOnlineFlagNoUser(){
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
        userCache.addUser(userInfo1);
        userCache.addUser(userInfo2);

        Assert.assertEquals(userCache.getOfflineUserIds().size(),2);
        userCache.setOnlineFlag(userInfo1.getUserId(),true);
        Assert.assertEquals(userCache.getOfflineUserIds().size(),1);
        Assert.assertEquals(userCache.getOfflineUserIds().get(0),userInfo2.getUserId());
        userCache.setOnlineFlag(userInfo1.getUserId(),false);
        Assert.assertEquals(userCache.getOfflineUserIds().size(),2);



    }

    @Test
    public void negativeGetUserNoUser(){
        try{
            userCache.getUser("userId");
            Assert.fail();
        }
        catch (BusinessLogicException e){
            Assert.assertEquals(e.getCode(), ErrorCode.USER_NOT_FOUND);
        }
    }

    @Test
    public void positiveAddUserAndGetUser(){
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
        userCache.addUser(userInfo1);
        Assert.assertEquals(userCache.getUser(userInfo1.getUserId()).getUserId(),userInfo1.getUserId());
        Assert.assertEquals(userCache.getUser(userInfo1.getUserId()).getFirstName(),userInfo1.getFirstName());
        Assert.assertEquals(userCache.getUser(userInfo1.getUserId()).getLastName(),userInfo1.getLastName());
        Assert.assertEquals(userCache.getUser(userInfo1.getUserId()).getSecondName(),userInfo1.getSecondName());
        Assert.assertEquals(userCache.getUser(userInfo1.getUserId()).getBirthDate(),userInfo1.getBirthDate());
        Assert.assertEquals(userCache.getUser(userInfo1.getUserId()).getLogin(),userInfo1.getLogin());
        Assert.assertEquals(userCache.getUser(userInfo1.getUserId()).getPassword(),userInfo1.getPassword());
        Assert.assertEquals(userCache.getUser(userInfo1.getUserId()).getRoles(),userInfo1.getRoles());
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
        userInfoList.add(userInfo1);
        userCache.addUser(userInfo1);
        Assert.assertEquals(userInfoList,userCache.getAllUsers());
    }
}
