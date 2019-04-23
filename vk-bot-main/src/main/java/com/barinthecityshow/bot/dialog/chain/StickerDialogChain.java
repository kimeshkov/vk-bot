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
                        .question("Что ела Клава Кока из своих ботинок?")
                        .addCorrectAnswer("Курицу")
                        .addCorrectAnswer("Курица")
                        .addCorrectAnswer("Курочку")
                        .addCorrectAnswer("Курочка")
                        .addCorrectAnswer("Мясо")
                        .build(),
                QuestionAnswer.builder()
                        .question("Кто первым показал мем ББПЕ Валерию?")
                        .addCorrectAnswer("Мама")
                        .addCorrectAnswer("Мать")
                        .addCorrectAnswer("Ма")
                        .addCorrectAnswer("Родители")
                        .build(),
                QuestionAnswer.builder()
                        .question("За что Kyivstoner получил президентскую награду?")
                        .addCorrectAnswer("За волонтёрство")
                        .addCorrectAnswer("за волонтерство")
                        .addCorrectAnswer("волонтерство")
                        .addCorrectAnswer("волонтёрство")
                        .addCorrectAnswer("волонтёрство")
                        .addCorrectAnswer("волонтёрскую деятельность")
                        .addCorrectAnswer("за волонтёрскую деятельность")
                        .addCorrectAnswer("волонтерскую деятельность")

                        .build()
        );

    }

}
