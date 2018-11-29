package io.deep27soft.deepanonymizerbot.model;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public final class Anonymouses {

    private final Set<Anonymous> mAnonymouses;

    public Anonymouses() {
        mAnonymouses = new HashSet<>();
    }

    public boolean setUserDisplayedName(User user, String name) {

        if (isDisplayedNameTaken(name)) {
            return false;
        } else {
            mAnonymouses.stream().filter(a -> a.getUser().equals(user)).forEach(a -> a.setDisplayedName(name));
            return true;
        }
    }

    public boolean removeUser(User user) {
        return mAnonymouses.removeIf(a -> a.getUser().equals(user));
    }

    public Stream<Chat> getChatsForUsers() {
        return mAnonymouses.stream().filter(a -> a.getDisplayedName() != null).map(Anonymous::getChat);
    }

    public Stream<String> getDisplayedNames() {
        return mAnonymouses.stream().map(Anonymous::getDisplayedName);
    }

    public boolean userHasName(User u) {

        Anonymous anonymous = mAnonymouses.stream().filter(a -> a.getUser().equals(u)).findFirst().orElse(null);
        if (anonymous == null) {
            return false;
        }

        return anonymous.getDisplayedName() != null;
    }

    public boolean addAnonymous(Anonymous anonymous) {
        if (mAnonymouses.stream().noneMatch(a -> a.getUser().equals(anonymous.getUser()))) {
            mAnonymouses.add(anonymous);
            return true;
        }
        return false;
    }

    public boolean hasUser(User u) {
        return mAnonymouses.stream().filter(a -> a.getUser().equals(u)).findFirst().orElse(null) != null;
    }

    public String getDisplayedName(User u) {
        Anonymous anonymous = mAnonymouses.stream().filter(a -> a.getUser().equals(u)).findFirst().orElse(null);
        String displayedName = null;
        if (anonymous != null) {
            displayedName = anonymous.getDisplayedName();
        }
        return displayedName;
    }

    public Stream<Anonymous> anonymouses() {
        return mAnonymouses.stream();
    }

    private boolean isDisplayedNameTaken(String name) {
        return getDisplayedNames().anyMatch(n -> n != null && n.equals(name));
    }
}
