package ru.reboot.service;

import org.springframework.stereotype.Component;
import ru.reboot.dto.UserInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import java.util.*;

/**
 * User cache.
 */
@Component
public class UserCache {

    private final Map<String, UserInfo> allUsers = new HashMap<>();
    private final Set<String> onlineUsers = new HashSet<>();

    public synchronized void addUser(UserInfo user) {
        allUsers.put(user.getUserId(), user);
    }


    public synchronized UserInfo getUser(String userId) {

        UserInfo user = allUsers.get(userId);
        if (Objects.isNull(user)) {
            throw new BusinessLogicException("User userId=" + userId + " not found", ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    public synchronized List<String> getOfflineUserIds(){
        Set<String> allUsersSet = new HashSet<>(allUsers.keySet());
        allUsersSet.removeAll(onlineUsers);
        return new ArrayList<>(allUsersSet);
    }

    public synchronized List<UserInfo> getAllUsers(){
        return new ArrayList<>(allUsers.values());
    }

    public synchronized void setOnlineFlag(String userId, boolean isOnline) {

        if (!allUsers.containsKey(userId)) {
            throw new BusinessLogicException("User userId=" + userId + " not found", ErrorCode.USER_NOT_FOUND);
        }

        if (isOnline) {
            onlineUsers.add(userId);
        } else {
            onlineUsers.remove(userId);
        }
    }

    public synchronized boolean isOnline(String userId) {
        return onlineUsers.contains(userId);
    }
}
