package io.deep27soft.telegrambotexample.commands;

import io.deep27soft.telegrambotexample.model.Anonymouses;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public final class StopCommand extends BotCommand {

    private final Anonymouses mAnonymouses;

    public StopCommand(Anonymouses anonymouses) {
        super("stop", "remove yourself from bot users' list\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (mAnonymouses.removeUser(user)) {
            sb.append("You've been removed from bot's users list! Bye!");
        } else {
            sb.append("You were not in bot users' list. Bye!");
        }

        message.setText(sb.toString());

        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
