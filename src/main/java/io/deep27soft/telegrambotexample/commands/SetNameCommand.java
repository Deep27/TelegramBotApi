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

import java.util.Arrays;

public final class SetNameCommand extends AnonymizerCommand {

    private final Anonymouses mAnonymouses;

    public SetNameCommand(Anonymouses anonymouses) {
        super("set_name", "set or change name that will be displayed with your messages\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        log.info(LogTemplate.USER_IS_EXECITING_COMMAND, user.hashCode(), getCommandIdentifier());

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!mAnonymouses.hasUser(user)) {
            log.log(Level.getLevel(LogLevel.STRANGE_USER), "User {} is trying to execute '{}' without starting bot!", user.hashCode(), getCommandIdentifier());
            message.setText("Firstly you should start bot! Send /start command!");
            sendMessageToUser(absSender, message, user);
            return;
        }

        String displayedName = getName(strings);

        if (displayedName == null) {
            log.log(Level.getLevel(LogLevel.STRANGE_USER), "User {} is trying to set empty name.", user.hashCode());
            message.setText("You should use non-empty name!");
            sendMessageToUser(absSender, message, user);
            return;
        }

        StringBuilder sb = new StringBuilder();

        if (mAnonymouses.setUserDisplayedName(user, displayedName)) {

            if (mAnonymouses.getDisplayedName(user) == null) {
                log.info("User {} set a name '{}'", user.hashCode(), displayedName);
                sb.append("Your displayed name: '").append(displayedName)
                        .append("'. Now you can send messages to bot!");
            } else {
                log.info("User {} has changed name to '{}'", user.hashCode(), displayedName);
                sb.append("Your new displayed name: '").append(displayedName).append("'.");
            }
        } else {
            log.log(Level.getLevel(LogLevel.STRANGE_USER), "User {} is trying to set taken name '{}'", user.hashCode(), displayedName);
            sb.append("Name ").append(displayedName).append(" is already in use! Choose another name!");
        }

        message.setText(sb.toString());
        sendMessageToUser(absSender, message, user);
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
}
