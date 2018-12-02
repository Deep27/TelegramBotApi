package io.deep27soft.deepanonymizerbot.logger;

public final class LogTemplate {

    public static final String MESSAGE_EXCEPTION = "User {} has caused an exception while sending message!";
    public static final String MESSAGE_PROCESSING = "Precessing user {}'s message.";
    public static final String MESSAGE_RECEIVED = "User {} has received message from another user {}.";
    public static final String MESSAGE_LOST = "User {} did not get message from another user {}.";

    public static final String COMMAND_PROCESSING = "User {} is executing '{}' command...";
    public static final String COMMAND_SUCCESS = "User {} has successfully executed '{}' command.";
    public static final String COMMAND_EXCEPTION = "User {} command '{}' has caused an exception!";

    private LogTemplate() {}
}
