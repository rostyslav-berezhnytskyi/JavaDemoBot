package org.example.javademobot.service;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotService {
    String handleStartCommand(Message msg);
    String handleGetTime(Message msg);
    String handleHelp(Message msg);
    String handleDeleteMyDataCommand(Message msg);
}
