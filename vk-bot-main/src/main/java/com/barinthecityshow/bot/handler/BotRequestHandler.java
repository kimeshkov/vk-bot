package com.barinthecityshow.bot.handler;

public interface BotRequestHandler {
    void handle(Long userId, String msg);
}
