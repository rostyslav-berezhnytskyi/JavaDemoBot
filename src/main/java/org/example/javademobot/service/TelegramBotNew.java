package org.example.javademobot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

public class TelegramBotNew implements LongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public BotOptions getOptions() {
        return null;
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {

    }

    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public String getBotToken() {
        return "";
    }
}
