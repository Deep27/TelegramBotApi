package io.deep27soft.deepanonymizerbot;

import io.deep27soft.deepanonymizerbot.bot.AnonymizerBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

// import java.net.Authenticator;
// import java.net.PasswordAuthentication;

public class BotInitializer {

    private static final Logger LOG = LogManager.getLogger(BotInitializer.class);

    private final static String PROXY_HOST = "80.11.200.161";
    private final static int PROXY_PORT = 9999;
    private final static String PROXY_USER = "...";
    private final static String PROXY_PASSWORD = "...";

    public static void main(String[] args) {

        try {

//            Authenticator.setDefault(new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
//                }
//            });

            LOG.info("Initializing API context...");
            ApiContextInitializer.init();

            TelegramBotsApi botsApi = new TelegramBotsApi();

            LOG.info("Configuring bot options...");
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS4);

            LOG.info("Registering Anonymizer...");
            botsApi.registerBot(new AnonymizerBot(botOptions));

            LOG.info("Anonymizer bot is ready for handling messages!");

        } catch (TelegramApiRequestException e) {
            LOG.error("Error while initializing bot!", e);
        }
    }
}
