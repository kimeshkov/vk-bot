package com.barinthecityshow.bot.dialog.chain;

import com.barinthecityshow.bot.chain.ChainElement;
import com.barinthecityshow.bot.dialog.QuestionAnswer;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

public class StickerDialogChain implements DialogChain {
    private final Random random;
    private final List<QuestionAnswer> questionAnswers;


    public StickerDialogChain(List<QuestionAnswer> questionAnswers) {
        random = new Random();
        this.questionAnswers = questionAnswers;
    }

    @Override
    public ChainElement<QuestionAnswer> getFirst() {
        return randomQuestion();
    }

    private ChainElement<QuestionAnswer> randomQuestion() {
        return new QuestionAnswerChainElement(questionAnswers.get(random.nextInt(questionAnswers.size())));

    }



}
