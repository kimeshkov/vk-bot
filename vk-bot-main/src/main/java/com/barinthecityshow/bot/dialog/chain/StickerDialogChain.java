package com.barinthecityshow.bot.dialog.chain;

import com.barinthecityshow.bot.chain.ChainElement;
import com.barinthecityshow.bot.dialog.QuestionAnswer;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

public class StickerDialogChain implements DialogChain {
    private static final List<QuestionAnswer> QUESTION_ANSWERS = initQuestionAnswers();

    private final Random random;

    public StickerDialogChain() {
        random = new Random();
    }

    @Override
    public ChainElement<QuestionAnswer> getFirst() {
        return randomQuestion();
    }

    private ChainElement<QuestionAnswer> randomQuestion() {
        return new QuestionAnswerChainElement(QUESTION_ANSWERS.get(random.nextInt(QUESTION_ANSWERS.size())));

    }

    private static List<QuestionAnswer> initQuestionAnswers() {
        return Lists.newArrayList(
                QuestionAnswer.builder()
                        .question("Сколько раз Бандерас выступал пьяным?")
                        .addCorrectAnswer("2")
                        .addCorrectAnswer("два")
                        .addCorrectAnswer("два раза")
                        .addCorrectAnswer("дважды")
                        .build(),
                QuestionAnswer.builder()
                        .question("Во сколько лет Нечаев сделал первую пародию?")
                        .addCorrectAnswer("6")
                        .addCorrectAnswer("шесть")
                        .addCorrectAnswer("в шесть лет")
                        .addCorrectAnswer("в шесть")
                        .addCorrectAnswer("в 6 лет")
                        .addCorrectAnswer("в 6")
                        .build()
        );

    }

}
