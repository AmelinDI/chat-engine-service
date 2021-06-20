package ru.reboot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.reboot.dto.ChatInfo;
import ru.reboot.dto.MessageInfo;
import ru.reboot.dto.UserInfo;
import ru.reboot.service.ChatEngineService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Chat engine controller.
 */
@RestController
@RequestMapping(path = "chat")
public class ChatEngineControllerImpl implements ChatEngineController {

    private static final Logger logger = LogManager.getLogger(ChatEngineControllerImpl.class);

    private ChatEngineService chatEngineService;

    @Autowired
    public void setChatEngineService(ChatEngineService chatEngineService) {
        this.chatEngineService = chatEngineService;
    }

    @GetMapping("info")
    public String info() {
        logger.info("method .info invoked");
        return "ChatEngineController " + new Date();
    }

    @Override
    public void authorize(String userId) {
        chatEngineService.authorize(userId);
    }

    @Override
    public void logout(String userId) {
        chatEngineService.logout(userId);
    }

    @Override
    public List<MessageInfo> getMessages(String sender, String recipient, LocalDateTime lastSyncTime) {
        return chatEngineService.getMessages(sender, recipient, lastSyncTime);
    }

    @Override
    public MessageInfo send(MessageInfo message) {
        return chatEngineService.send(message);
    }

    @Override
    public void commitMessages(List<String> messageIds) {
        chatEngineService.commitMessages(messageIds);
    }

    @Override
    public List<ChatInfo> getChatsInfo(String userId) {
        return chatEngineService.getChatsInfo(userId);
    }

    @Override
    public List<UserInfo> getAllUsers() {
        return chatEngineService.getAllUsers();
    }
}