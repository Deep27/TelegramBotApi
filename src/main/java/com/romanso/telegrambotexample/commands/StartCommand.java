package com.romanso.telegrambotexample.commands;

import com.romanso.telegrambotexample.model.Anonymouses;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public final class StartCommand extends BotCommand {

    private final Anonymouses mAnonymouses;

    public StartCommand(Anonymouses anonymouses) {
        super("start", "start using bot\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        if (!mAnonymouses.hasUser(user)) {

            StringBuilder welcomeMessageBuilder = new StringBuilder();
            welcomeMessageBuilder.append("Hi, ").append(user.getUserName()).append("!\n")
                    .append("Please execute command:\n'/set_name <displayed_name>'\nwhere <displayed_name> is the name you want to use to hide your real name.");

            SendMessage welcomeMessage = new SendMessage();
            welcomeMessage.setChatId(chat.getId().toString());
            welcomeMessage.setText(welcomeMessageBuilder.toString());

            try {
                absSender.execute(welcomeMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
