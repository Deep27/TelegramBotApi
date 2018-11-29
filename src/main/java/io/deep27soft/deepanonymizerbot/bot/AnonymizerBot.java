package io.deep27soft.deepanonymizerbot.bot;

import io.deep27soft.deepanonymizerbot.commands.*;
import io.deep27soft.deepanonymizerbot.logger.LogLevel;
import io.deep27soft.deepanonymizerbot.logger.LogTemplate;
import io.deep27soft.deepanonymizerbot.model.Anonymous;
import io.deep27soft.deepanonymizerbot.model.Anonymouses;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Stream;

public final class AnonymizerBot extends TelegramLongPollingCommandBot {

    private static final Logger LOG = LogManager.getLogger(AnonymizerBot.class);

    private static final String BOT_NAME = "Deep27Bot";
    private static final String BOT_TOKEN = "704317455:AAEXxxIUsX9MY8Zl2Ht05o_xGwzC6hAixq0";

    private final Anonymouses mAnonymouses;

    public AnonymizerBot(DefaultBotOptions botOptions) {

        super(botOptions, BOT_NAME);

        LOG.info("Initializing Anonymizer Bot...");

        LOG.info("Initializing anonymouses list...");
        mAnonymouses = new Anonymouses();

        LOG.info("Registering commands...");
        LOG.info("Registering '/start'...");
        register(new StartCommand(mAnonymouses));
        LOG.info("Registering '/set_name'...");
        register(new SetNameCommand(mAnonymouses));
        LOG.info("Registering '/stop'...");
        register(new StopCommand(mAnonymouses));
        LOG.info("Registering '/my_name'...");
        register(new MyNameCommand(mAnonymouses));
        HelpCommand helpCommand = new HelpCommand(this);
        LOG.info("Registering '/help'...");
        register(helpCommand);

        LOG.info("Registering default action'...");
        registerDefaultAction(((absSender, message) -> {

            SendMessage text = new SendMessage();
            text.setChatId(message.getChatId());
            text.setText(message.getText() + " command not found!");

            try {
                absSender.execute(text);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
        }));
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        LOG.info("Processing non-command update...");

        if (!update.hasMessage()) {
            LOG.error("Update doesn't have a body!");
            throw new IllegalStateException("Update doesn't have a body!");
        }

        Message msg = update.getMessage();
        User user = msg.getFrom();

        LOG.info("Processing user {} message...", user.hashCode());

        if (!canSendMessage(user, msg)) {
            return;
        }

        String clearMessage = msg.getText();
        String messageForUsers = String.format("%s:\n%s", mAnonymouses.getDisplayedName(user), msg.getText());

        SendMessage answer = new SendMessage();
        answer.setText(clearMessage);
        answer.setChatId(msg.getChatId());
        replyToUser(answer, user, clearMessage);


        answer.setText(messageForUsers);
        Stream<Anonymous> anonymouses = mAnonymouses.anonymouses();
        anonymouses.filter(a -> !a.getUser().equals(user))
                .forEach(a -> {
                    answer.setChatId(a.getChat().getId());
                    sendMessageToUser(answer, a.getUser(), user);
                });
    }

    private boolean canSendMessage(User user, Message msg) {

        SendMessage answer = new SendMessage();
        answer.setChatId(msg.getChatId());

        if (!msg.hasText() || msg.getText().trim().length() == 0) {
            LOG.log(Level.getLevel(LogLevel.STRANGE_USER), "User {} is trying to send empty message!", user.hashCode());
            answer.setText("You shouldn't send empty messages!");
            replyToUser(answer, user, msg.getText());
            return false;
        }

        if(!mAnonymouses.hasUser(user)) {
            LOG.log(Level.getLevel(LogLevel.STRANGE_USER), "User {} is trying to send message without starting the bot!", user.hashCode());
            answer.setText("Firstly you should start bot! Use /start command!");
            replyToUser(answer, user, msg.getText());
            return false;
        }

        if (!mAnonymouses.userHasName(user)) {
            LOG.log(Level.getLevel(LogLevel.STRANGE_USER), "User {} is trying to send message without setting a name!", user.hashCode());
            answer.setText("You must set a name before sending messages.\nUse '/set_name <displayed_name>' command.");
            replyToUser(answer, user, msg.getText());
            return false;
        }

        return true;
    }

    private void sendMessageToUser(SendMessage message, User receiver, User sender) {
        try {
            execute(message);
            LOG.log(Level.getLevel(LogLevel.SUCCESS_USER), LogTemplate.USER_HAS_RECEIVED_MESSAGE_FROM_ANOTHER_USER, receiver.hashCode(), sender.hashCode());
        } catch (TelegramApiException e) {
            LOG.error(LogTemplate.USER_COULD_NOT_RECEIVE_MESSAGE_FROM_ANOTHER_USER, receiver.hashCode(), sender.hashCode(), e);
            // @TODO handle exception
        }
    }

    private void replyToUser(SendMessage message, User user, String messageText) {
        try {
            execute(message);
            LOG.log(Level.getLevel(LogLevel.SUCCESS_USER), LogTemplate.USER_HAS_SEND_MESSAGE, user.hashCode(), messageText);
        } catch (TelegramApiException e) {
            LOG.error(LogTemplate.USER_MESSAGE_CAUSED_EXCEPTION, user.hashCode(), e);
            // @TODO handle exception
        }
    }
}
