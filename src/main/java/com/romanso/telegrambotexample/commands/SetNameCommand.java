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

    private final Anonymouses mAnonymouses;

    public SetNameCommand(Anonymouses anonymouses) {
        super("set_name", "set or change name that will be displayed with your messages\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        StringBuilder sb = new StringBuilder();

        if (!mAnonymouses.hasUser(user)) {
            message.setText("Firstly you should start bot! Send /start command!");
            sendMessage(absSender, message);
            return;
        }

        String displayedName = getName(strings);

        if (mAnonymouses.setUserDisplayedName(user, displayedName)) {
            sb.append("Your new displayed name: '").append(displayedName)
                    .append("'. Now you can send messages to bot!");
        } else {
            sb.append("Name ").append(displayedName).append(" is already in use! Choose another name!");
        }

        message.setText(sb.toString());
        sendMessage(absSender, message);
    }

    private String getName(String[] strings) {

        if (strings == null || strings.length == 0) {
            return null;
        }

        StringBuilder nameBuilder = new StringBuilder();
        Arrays.stream(strings).forEach(s -> nameBuilder.append(s).append(" "));
        nameBuilder.setLength(nameBuilder.length() - 1);

        return nameBuilder.toString().trim().length() != 0 ? nameBuilder.toString() : null;
    }

    private void sendMessage(AbsSender sender, SendMessage message) {
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

