package ru.reboot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.reboot.dto.ChatInfo;
import ru.reboot.dto.MessageInfo;
import ru.reboot.dto.UserInfo;
import ru.reboot.security.CustomUserDetails;
import ru.reboot.service.ChatEngineService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @PostMapping("user/authorize")
    public String authorize(@RequestBody String userId) {
        chatEngineService.authorize(userId);
        return "{\"authorize\":\"complete\"}";
    }

    @PostMapping("user/logout")
    @Override
    public String logout(@RequestBody String userId) {
        SecurityContextHolder.getContext().setAuthentication(null);
        chatEngineService.logout(userId);
        return "{\"logout\":\"completed\"}";
    }

    @GetMapping("user/logout")
    public String logout() {
        SecurityContext sc = SecurityContextHolder.getContext();
        CustomUserDetails user = (CustomUserDetails) sc.getAuthentication().getPrincipal();
        sc.setAuthentication(null);
        chatEngineService.logout(user.getUserId());
        return "{\"logout\":\"completed\"}";
    }

    @Override
    @GetMapping("message/all")
    public List<MessageInfo> getMessages(@RequestParam String sender, @RequestParam String recipient) {
        List<MessageInfo> messageInfos = chatEngineService.getMessages(sender, recipient);
        List<String> messageIds = messageInfos.stream()
                .filter(msg -> msg.getRecipient().equals(recipient))
                .map(MessageInfo::getId)
                .collect(Collectors.toList());

        if (messageIds.size() > 0) {
            chatEngineService.commitMessages(messageIds);
        }
        return messageInfos;
    }

    @Override
    @GetMapping("message/sinceTime")
    public List<MessageInfo> getMessages(@RequestParam String sender, @RequestParam String recipient, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        List<MessageInfo> messageInfos = chatEngineService.getMessages(sender, recipient, timestamp);
        List<String> messageIds = messageInfos.stream()
                .filter(msg -> msg.getRecipient().equals(recipient))
                .map(MessageInfo::getId)
                .collect(Collectors.toList());

        if (messageIds.size() > 0) {
            chatEngineService.commitMessages(messageIds);
        }
        return messageInfos;
    }

    @Override
    @PostMapping("message")
    public MessageInfo send(@RequestBody MessageInfo message) {
        return chatEngineService.send(message);
    }

    @Override
    @PutMapping("message/commit")
    public void commitMessages(@RequestBody List<String> messageIds) {
        chatEngineService.commitMessages(messageIds);
    }

    @Override
    @GetMapping("all")
    public List<ChatInfo> getChatsInfo(@RequestParam String userId) {
        List<ChatInfo> tmpList = chatEngineService.getChatsInfo(userId);
        tmpList.forEach(System.out::println);
        return tmpList;
    }

    @Override
    @GetMapping("user/all")
    public List<UserInfo> getAllUsers() {
        return chatEngineService.getAllUsers();
    }
}