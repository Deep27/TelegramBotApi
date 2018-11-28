package io.deep27soft.telegrambotexample;

import io.deep27soft.telegrambotexample.commands.*;
import io.deep27soft.telegrambotexample.model.Anonymouses;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Stream;

public final class AnonymizerBot extends TelegramLongPollingCommandBot {

    private static final String BOT_NAME = "Deep27Bot";
    private static final String BOT_TOKEN = "704317455:AAEXxxIUsX9MY8Zl2Ht05o_xGwzC6hAixq0";

    private final Anonymouses mAnonymouses;

    public AnonymizerBot(DefaultBotOptions botOptions) {

        super(botOptions, BOT_NAME);

        mAnonymouses = new Anonymouses();

        register(new StartCommand(mAnonymouses));
        register(new SetNameCommand(mAnonymouses));
        register(new StopCommand(mAnonymouses));
        register(new MyNameCommand(mAnonymouses));
        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

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

        if (!update.hasMessage()) {
            throw new IllegalStateException("Update doesn't have a body!");
        }

        Message msg = update.getMessage();
        User user = msg.getFrom();

        if (!canSendMessage(user, msg)) {
            return;
        }

        String messageText = String.format("%s:\n%s", mAnonymouses.getDisplayedName(user), msg.getText());

        SendMessage answer = new SendMessage();
        answer.setText(messageText);

        Stream<Chat> chats = mAnonymouses.getChatsForUsers();
        chats.forEach(chat -> {
            answer.setChatId(chat.getId());
            sendMessage(answer);
        });
    }

    private boolean canSendMessage(User user, Message msg) {

        SendMessage answer = new SendMessage();
        answer.setChatId(msg.getChatId());

        if (!msg.hasText() || msg.getText().trim().length() == 0) {
            answer.setText("You shouldn't send empty messages!");
            sendMessage(answer);
            return false;
        }

        if(!mAnonymouses.hasUser(user)) {
            answer.setText("Firstly you should start bot! Use /start command!");
            sendMessage(answer);
            return false;
        }

        if (!mAnonymouses.userHasName(user)) {
            answer.setText("You must set a name before sending messages.\nUse '/set_name <displayed_name>' command.");
            sendMessage(answer);
            return false;
        }

        return true;
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
