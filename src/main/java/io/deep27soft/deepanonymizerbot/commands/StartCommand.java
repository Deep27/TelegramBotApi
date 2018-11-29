package io.deep27soft.deepanonymizerbot.commands;

import io.deep27soft.deepanonymizerbot.logger.LogLevel;
import io.deep27soft.deepanonymizerbot.logger.LogTemplate;
import io.deep27soft.deepanonymizerbot.model.Anonymous;
import io.deep27soft.deepanonymizerbot.model.Anonymouses;
import org.apache.logging.log4j.Level;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class StartCommand extends AnonymizerCommand {

    private final Anonymouses mAnonymouses;

    public StartCommand(Anonymouses anonymouses) {
        super("start", "start using bot\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        log.info(LogTemplate.USER_IS_EXECITING_COMMAND, user.hashCode(), getCommandIdentifier());

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (mAnonymouses.addAnonymous(new Anonymous(user, chat))) {
            log.info("User {} is trying to execute '{}' the first time. Added to users' list.", user.hashCode(), getCommandIdentifier());
            sb.append("Hi, ").append(user.getUserName()).append("! You've been added to bot users' list!\n")
                    .append("Please execute command:\n'/set_name <displayed_name>'\nwhere <displayed_name> is the name you want to use to hide your real name.");
        } else {
            log.log(Level.getLevel(LogLevel.STRANGE_USER), "User {} has already executed '{}'. Is he trying to do it one more time?", user.hashCode(), getCommandIdentifier());
            sb.append("You've already started bot! You can send messages if you have set your name.");
        }

        message.setText(sb.toString());
        sendMessageToUser(absSender, message, user);
    }
}
