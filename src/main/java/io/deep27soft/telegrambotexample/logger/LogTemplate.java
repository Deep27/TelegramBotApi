package io.deep27soft.telegrambotexample.logger;

public abstract class LogTemplate {

    public static final String USER_HAS_SEND_MESSAGE = "User {} has send a message: ==='{}'===";
    public static final String USER_MESSAGE_CAUSED_EXCEPTION = "User {} has caused an exception while sending message!";
    public static final String USER_HAS_RECEIVED_MESSAGE_FROM_ANOTHER_USER = "User {} has received message from another user {}.";
    public static final String USER_COULD_NOT_RECEIVE_MESSAGE_FROM_ANOTHER_USER = "User {} did not get message from another user {}.";
    public static final String USER_IS_EXECITING_COMMAND = "User {} is executing '{}'...";
    public static final String USER_EXECUTED_COMMAND = "User {} has executed '{}'.";
    public static final String USER_COMMAND_CAUSED_EXCEPTION = "User {} has caused an exception while executing '{}'!";
}
