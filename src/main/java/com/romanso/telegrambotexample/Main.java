package com.romanso.telegrambotexample;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

// import java.net.Authenticator;
// import java.net.PasswordAuthentication;

public class Main {

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

            ApiContextInitializer.init();

            TelegramBotsApi botsApi = new TelegramBotsApi();

            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS4);

            botsApi.registerBot(new AnonymizerBot(botOptions));
            System.out.println("Anonimyzer registered!");


        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
