package com.barinthecityshow.bot.dialog.chain;

import com.barinthecityshow.bot.chain.ChainElement;
import com.barinthecityshow.bot.dialog.QuestionAnswer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.Map;

public class StickerDialogChain implements DialogChain {
    private static final Map<String, String> questionAns = ImmutableMap.<String, String>builder()
            .put("Ты лох?", "Да")
            .put("Ты нормальный?", "нет")
            .put("Ты Ира?", "Да")
            .build();

    @Override
    public ChainElement<QuestionAnswer> getFirst() {
        Map.Entry<String, String> qa = getRandomEntry();
        QuestionAnswer questionAnswer = new QuestionAnswer(qa.getKey(), Lists.newArrayList(qa.getValue()));
        return new QuestionAnswerChainElement(questionAnswer);
    }

    private Map.Entry<String, String> getRandomEntry() {
        return questionAns.entrySet().stream().findFirst().orElseThrow(RuntimeException::new);

    }
}
