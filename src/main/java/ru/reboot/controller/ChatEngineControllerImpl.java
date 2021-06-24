package ru.reboot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    @GetMapping("/message/sinceTime")
    public List<MessageInfo> getMessages(@RequestParam String sender, @RequestParam String recipient, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timestamp) {
        return chatEngineService.getMessages(sender, recipient, timestamp);
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
    @GetMapping ("/all")
    public List<ChatInfo> getChatsInfo(@RequestParam String userId) {
        return chatEngineService.getChatsInfo(userId);
    }

    @Override
    public List<UserInfo> getAllUsers() {
        return chatEngineService.getAllUsers();
    }
}