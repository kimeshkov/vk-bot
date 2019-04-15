package com.barinthecityshow.bot.dialog.chain;

import com.barinthecityshow.bot.chain.ChainElement;
import com.barinthecityshow.bot.dialog.QuestionAnswer;

import java.util.Optional;

public class QuestionAnswerChainElement implements ChainElement<QuestionAnswer> {

    private QuestionAnswerChainElement next;

    private QuestionAnswer current;

    QuestionAnswerChainElement(QuestionAnswer current) {
        this.current = current;
    }

    @Override
    public Optional<ChainElement<QuestionAnswer>> next() {
        return Optional.ofNullable(next);
    }

    @Override
    public QuestionAnswer current() {
        return current;
    }
}
