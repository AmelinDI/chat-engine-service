package ru.reboot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.reboot.dto.ChatInfo;
import ru.reboot.dto.MessageInfo;
import ru.reboot.dto.UserInfo;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatEngineController {

    /**
     * Authorize user by user id.
     *
     * @param userId - user id
     */
    void authorize(String userId);

    /**
     * Logout
     *
     * @param userId - user id.
     */
    void logout(String userId);

    /**
     * Get messages form specific time up to current/
     *
     * @param sender       - sender user id
     * @param recipient    - recipient user id
     * @param lastSyncTime - last sync time
     */
    List<MessageInfo> getMessages(String sender, String recipient, LocalDateTime lastSyncTime);

    /**
     * Send message from user to recipient
     *
     * @param message - message
     */
    MessageInfo send(MessageInfo message);

    /**
     * Commit messages. Mark them as read.
     *
     * @param messageIds - list of message ids
     */
    void commitMessages(List<String> messageIds);

    /**
     * Get operation information about user chats.
     *
     * @param userId - user id
     */
    List<ChatInfo> getChatsInfo(String userId);

    /**
     * Get all users.
     */
    List<UserInfo> getAllUsers();

    UserInfo createUser(UserInfo user);
}
