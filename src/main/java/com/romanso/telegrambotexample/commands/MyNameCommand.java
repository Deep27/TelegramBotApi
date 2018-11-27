package com.romanso.telegrambotexample.commands;

import com.romanso.telegrambotexample.model.Anonymouses;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public final class MyNameCommand extends BotCommand {

    private final Anonymouses mAnonymouses;

    public MyNameCommand(Anonymouses anonymouses) {
        super("my_name", "show your current name that will be displayed with your messages\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder sb = new StringBuilder();

        SendMessage welcomeMessage = new SendMessage();
        welcomeMessage.setChatId(chat.getId().toString());

        if (!mAnonymouses.hasUser(user)) {
            sb.append("You are not in bot users' list! Send /start command!");
        } else if(mAnonymouses.getDisplayedName(user) == null) {
            sb.append("Currently you don't have a name.\nSet it using command:\n'/set_name <displayed_name>'");
        } else {
            sb.append("Your current name: ").append(mAnonymouses.getDisplayedName(user));
        }

        welcomeMessage.setText(sb.toString());

        try {
            absSender.execute(welcomeMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
