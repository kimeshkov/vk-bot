package com.barinthecityshow.bot.handler;

public interface BotRequestHandler {
    void handle(Integer userId, String msg);
}
