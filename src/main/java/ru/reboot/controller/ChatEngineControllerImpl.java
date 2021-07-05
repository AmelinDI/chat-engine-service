package ru.reboot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    @PostMapping("/user/authorize")
    public void authorize(@RequestParam String userId) {
        chatEngineService.authorize(userId);
    }

    @PostMapping("user/logout")
    @Override
    public void logout(@RequestParam("userId") String userId) {
        chatEngineService.logout(userId);
    }

    @Override
    @GetMapping("/message/sinceTime")
    public List<MessageInfo> getMessages(@RequestParam String sender, @RequestParam String recipient, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        return chatEngineService.getMessages(sender, recipient, timestamp);
    }

    @Override
    @PostMapping("/message")
    public MessageInfo send(@RequestBody MessageInfo message) {
        return chatEngineService.send(message);
    }

    @Override
    @PutMapping("/message/commit")
    public void commitMessages(@RequestBody List<String> messageIds) {
        chatEngineService.commitMessages(messageIds);
    }

    @Override
    @GetMapping("/all")
    public List<ChatInfo> getChatsInfo(@RequestParam String userId) {
        return chatEngineService.getChatsInfo(userId);
    }

    @GetMapping("user/all")
    @Override
    public List<UserInfo> getAllUsers() {
        return chatEngineService.getAllUsers();
    }
}