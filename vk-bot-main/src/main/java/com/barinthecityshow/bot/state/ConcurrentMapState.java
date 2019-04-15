package com.barinthecityshow.bot.state;

import com.barinthecityshow.bot.chain.ChainElement;
import com.barinthecityshow.bot.dialog.QuestionAnswer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ConcurrentMapState implements State<Long, ChainElement<QuestionAnswer>> {
    INSTANCE;

    private Map<Long, ChainElement<QuestionAnswer>> state = new ConcurrentHashMap<>();


    @Override
    public void put(Long key, ChainElement<QuestionAnswer> value) {
        state.put(key, value);
    }

    @Override
    public ChainElement<QuestionAnswer> get(Long key) {
        return state.get(key);
    }

    @Override
    public boolean containsKey(Long key) {
        return state.containsKey(key);
    }
}
