package org.example.javademobot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;

@Slf4j
@Service
public class BotServiceImpl implements BotService {
    private final UserService userService;

    @Autowired
    public BotServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public String handleStartCommand(Message msg) {
        boolean isNewUser = userService.registerUser(msg);
        String name = msg.getChat().getFirstName();
        String greeting = "Hi, " + name + ", nice to meet you! " + EmojiParser.parseToUnicode(":blush:");
        return isNewUser ? greeting : "Welcome back, " + name + "!";
    }

    public String handleGetTime() {
        return "The current time of your request is " + LocalDateTime.now();
    }

    public String handleHelp() {
        return """
               This bot demonstrates Java Spring Boot with Telegram API.
               Commands:
               /start - Welcome message
               /mydata - View your data
               /help - Help info
               /get_time - Current date & time
               """;
    }
}
