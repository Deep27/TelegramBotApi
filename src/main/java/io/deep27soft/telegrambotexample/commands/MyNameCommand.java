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

public final class MyNameCommand extends AnonymizerCommand {

    private final Anonymouses mAnonymouses;

    public MyNameCommand(Anonymouses anonymouses) {
        super("my_name", "show your current name that will be displayed with your messages\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        log.info(LogTemplate.USER_IS_EXECITING_COMMAND, user.hashCode(), getCommandIdentifier());

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!mAnonymouses.hasUser(user)) {

            sb.append("You are not in bot users' list! Send /start command!");
            log.log(Level.getLevel(LogLevel.STRANGE_USER), "User {} is trying to execute '{}' without starting the bot.", user.hashCode(), getCommandIdentifier());

        } else if(mAnonymouses.getDisplayedName(user) == null) {

            sb.append("Currently you don't have a name.\nSet it using command:\n'/set_name <displayed_name>'");
            log.log(Level.getLevel(LogLevel.STRANGE_USER), "User {} is trying to execute '{}' without having a name.", user.hashCode(), getCommandIdentifier());

        } else {

            log.info("User {} is executing '{}'. Name is '{}'.", user.hashCode(), getCommandIdentifier(), mAnonymouses.getDisplayedName(user));
            sb.append("Your current name: ").append(mAnonymouses.getDisplayedName(user));
        }

        message.setText(sb.toString());
        sendMessageToUser(absSender, message, user);
    }
}
