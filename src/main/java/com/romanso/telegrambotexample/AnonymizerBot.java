package com.romanso.telegrambotexample;

import com.romanso.telegrambotexample.commands.*;
import com.romanso.telegrambotexample.model.Anonymouses;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {

            Message msg = update.getMessage();

            if (msg.hasText()) {

                SendMessage message = new SendMessage();
                message.setChatId(msg.getChatId());
                message.setText("Hey: " + msg.getText());

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


}
