package com.romanso.telegrambotexample.model;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class Anonymouses {

    private final Map<User, String> mAnonymouses;

    public Anonymouses() {
        mAnonymouses = new HashMap<>();
    }

    public AnonymousQueryStatus setUserDisplayedName(User user, String name) {

        AnonymousQueryStatus status;

        if (isDisplayedNameTaken(name)) {
            status = AnonymousQueryStatus.NAME_IS_TAKEN;
        } else {
            status = mAnonymouses.containsKey(user) ? AnonymousQueryStatus.NAME_CHANGED : AnonymousQueryStatus.NAME_SET;
            mAnonymouses.put(user, name);
        }

        return status;
    }

    public AnonymousQueryStatus removeUser(User user) {

        if (mAnonymouses.containsKey(user)) {
            mAnonymouses.remove(user);
            return AnonymousQueryStatus.USER_REMOVED;
        }

        return AnonymousQueryStatus.NO_SUCH_USER;
    }

    public Stream<User> getUsers() {
        return mAnonymouses.keySet().stream();
    }

    public void addUser(User u) {
        mAnonymouses.put(u, null);
    }

    public boolean hasUser(User u) {
        return mAnonymouses.containsKey(u);
    }

    public String getDisplayedName(User u) {
        return mAnonymouses.get(u);
    }

    private boolean isDisplayedNameTaken(String name) {
        return mAnonymouses.entrySet().stream().anyMatch(a -> a.getValue().equals(name));
    }

    enum AnonymousQueryStatus {
        NAME_IS_TAKEN, NAME_SET, NAME_CHANGED, USER_REMOVED, NO_SUCH_USER
    }
}
