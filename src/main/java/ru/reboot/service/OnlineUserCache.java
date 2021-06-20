package ru.reboot.service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * User cache.
 * Stores online users.
 */
@Component
public class OnlineUserCache {

    private final Set<String> onlineUsers = Collections.synchronizedSet(new HashSet<>());

    public void put(String userId) {
        onlineUsers.add(userId);
    }

    public void remove(String userId) {
        onlineUsers.remove(userId);
    }

    public boolean isOnline(String userId) {
        return onlineUsers.contains(userId);
    }
}
