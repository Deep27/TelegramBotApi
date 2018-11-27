package com.romanso.telegrambotexample.commands;

import com.romanso.telegrambotexample.model.Anonymouses;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

public final class SetNameCommand extends BotCommand {

    private static final String ERROR_EMPTY_NAME = "You should set non-empty name!\n(/set_name <displayed_name>)";

    private final Anonymouses mAnonymouses;

    public SetNameCommand(Anonymouses anonymouses) {
        super("set_name", "set or change name that will be displayed with your messages\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder sb = new StringBuilder();

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());

        if (strings == null || strings.length == 0) {
            sb.append(ERROR_EMPTY_NAME);

        } else {

            Arrays.stream(strings).forEach(s -> sb.append(s).append(" "));
            sb.setLength(sb.length() - 1);

            String displayedName = sb.toString();
            sb.setLength(0);

            if (displayedName.trim().length() == 0) {
                sb.append(ERROR_EMPTY_NAME);
            } else {
                mAnonymouses.setUserDisplayedName(user, displayedName);
                sb.append("Your new displayed name: '").append(displayedName)
                        .append("'. Now you can send messages to bot!");
            }
        }

        answer.setText(sb.toString());

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

