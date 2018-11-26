package com.romanso.telegrambotexample.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class HelloCommand extends BotCommand {

    public HelloCommand() {
        super("hello", "Say hello to this bot.");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        String userName = chat.getUserName();

        if (userName == null || userName.isEmpty()) {
            userName = user.getFirstName() + " " + user.getLastName();
        }

        StringBuilder message = new StringBuilder("Hello, ").append(userName);

        if (strings != null && strings.length > 0) {
            message.append("\nThank you!\n")
                    .append(String.join(" ", strings));
        }

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(message.toString());

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
