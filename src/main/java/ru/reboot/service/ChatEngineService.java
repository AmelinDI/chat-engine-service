package ru.reboot.service;

import ru.reboot.dto.ChatInfo;
import ru.reboot.dto.MessageInfo;
import ru.reboot.dto.UserInfo;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatEngineService {

    /**
     * Authorize user by user id.
     * - append user to online user cache.
     * - load all messages with current user and put them to table recent_messages
     *
     * @param userId - user id
     */
    void authorize(String userId);

    /**
     * Logout
     * - remove user from online users cache.
     * - remove all messages where current sender and receiver are offline.
     *
     * @param userId - user id.
     */
    void logout(String userId);

    /**
     * Send message from user to recipient
     *
     * @param message - message
     */
    MessageInfo send(MessageInfo message);

    /**
     * Get messages form specific time up to current/
     *
     * @param sender       - sender user id
     * @param recipient    - recipient user id
     */
    List<MessageInfo> getMessages(String sender, String recipient);

    /**
     * Get messages form specific time up to current/
     *
     * @param sender       - sender user id
     * @param recipient    - recipient user id
     * @param lastSyncTime - last sync time
     */
    List<MessageInfo> getMessages(String sender, String recipient, LocalDateTime lastSyncTime);

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
