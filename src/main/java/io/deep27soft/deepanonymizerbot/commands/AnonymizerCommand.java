package io.deep27soft.deepanonymizerbot.commands;

import io.deep27soft.deepanonymizerbot.logger.LogLevel;
import io.deep27soft.deepanonymizerbot.logger.LogTemplate;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class AnonymizerCommand extends BotCommand {

    protected final Logger log = LogManager.getLogger(getClass());

    protected AnonymizerCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    // @TODO make abstract and implement in children
    protected void processTelegramApiException() {}

    protected void sendMessageToUser(AbsSender sender, SendMessage message, User user) {
        try {
            sender.execute(message);
            log.log(Level.getLevel(LogLevel.SUCCESS_USER), LogTemplate.USER_EXECUTED_COMMAND, user.hashCode(), getCommandIdentifier());
        } catch (TelegramApiException e) {
            log.error(LogTemplate.USER_COMMAND_CAUSED_EXCEPTION, user.hashCode(), getCommandIdentifier(), e);
            processTelegramApiException();
        }
    }
}
