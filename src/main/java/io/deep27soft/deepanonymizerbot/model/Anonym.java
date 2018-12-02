package io.deep27soft.deepanonymizerbot.model;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public final class Anonym extends User {

    private final Chat mChat;
    private String mDisplayedName;

    public Anonym(Chat chat) {
        mChat = chat;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Anonym && super.equals(obj);
    }

    public Chat getChat() {
        return mChat;
    }

    public String getDisplayedName() {
        return mDisplayedName;
    }

    public void setDisplayedName(String displayedName) {
        mDisplayedName = displayedName;
    }
}
