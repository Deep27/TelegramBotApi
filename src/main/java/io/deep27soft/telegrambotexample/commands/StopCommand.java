package io.deep27soft.telegrambotexample.commands;

import io.deep27soft.telegrambotexample.bot.AnonymizerCommand;
import io.deep27soft.telegrambotexample.logger.LogLevel;
import io.deep27soft.telegrambotexample.logger.LogTemplate;
import io.deep27soft.telegrambotexample.model.Anonymouses;
import org.apache.logging.log4j.Level;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class StopCommand extends AnonymizerCommand {

    private final Anonymouses mAnonymouses;

    public StopCommand(Anonymouses anonymouses) {
        super("stop", "remove yourself from bot users' list\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        log.info(LogTemplate.USER_IS_EXECITING_COMMAND, user.hashCode(), getCommandIdentifier());

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (mAnonymouses.removeUser(user)) {
            log.info("User {} has been removed from users list!", user.hashCode());
            sb.append("You've been removed from bot's users list! Bye!");
        } else {
            log.log(Level.getLevel(LogLevel.STRANGE_USER), "User {} is trying to execute '{}' without having executed 'start' before!", user.hashCode(), getCommandIdentifier());
            sb.append("You were not in bot users' list. Bye!");
        }

        message.setText(sb.toString());
        sendMessageToUser(absSender, message, user);
    }
}
