package org.example.javademobot.service;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotService {
    String handleStartCommand(Message msg);
    String handleGetTime();
    String handleHelp();
}
