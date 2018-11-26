package com.romanso.telegrambotexample;

import com.romanso.telegrambotexample.commands.HelloCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CommandsHandler extends TelegramLongPollingCommandBot {

    public CommandsHandler(String botUsername) {

        super(botUsername);

        register(new HelloCommand());

        registerDefaultAction(((absSender, message) -> {
            SendMessage unknownCommand = new SendMessage();
            unknownCommand.setText(message.getText() + " command not found!");
            try {
                absSender.execute(unknownCommand);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {

            Message msg = update.getMessage();

            if (msg.hasText()) {
                SendMessage echoMessage = new SendMessage();
                echoMessage.setChatId(msg.getChatId());
                echoMessage.setText("Hey: " + msg.getText());

                try {
                    execute(echoMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return MyAmazingBot.BOT_TOKEN;
    }
}
