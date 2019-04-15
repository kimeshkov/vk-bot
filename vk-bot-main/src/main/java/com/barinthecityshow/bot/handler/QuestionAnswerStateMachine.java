package com.barinthecityshow.bot.handler;

import com.barinthecityshow.bot.state.ConcurrentMapState;
import com.barinthecityshow.bot.state.State;
import com.barinthecityshow.bot.chain.ChainElement;
import com.barinthecityshow.bot.dialog.QuestionAnswer;
import com.barinthecityshow.bot.dialog.chain.DialogChain;
import com.barinthecityshow.bot.dialog.chain.StickerDialogChain;
import com.barinthecityshow.bot.service.VkApiService;

import java.util.Objects;

public class QuestionAnswerStateMachine implements BotRequestHandler {
    private static final String WELCOME_MSG = "Привет, хочешь стикер? Тогда ответь на вопрос: ";

    private final State<Integer, ChainElement<QuestionAnswer>> state = ConcurrentMapState.INSTANCE;
    private final DialogChain dialogChain = new StickerDialogChain();
    private final VkApiService vkApiService;

    public QuestionAnswerStateMachine(VkApiService vkApiService) {
        this.vkApiService = vkApiService;
    }

    public void handle(Integer userId, String msg) {
        if (state.containsKey(userId)) {
            ChainElement<QuestionAnswer> first = dialogChain.getFirst();
            handleNew(userId, first.current());
            state.put(userId, first);

        } else {
            ChainElement<QuestionAnswer> chainElement = state.get(userId);
            if (isCorrectAnswer(msg, chainElement.current())) {
                if (chainElement.next().isPresent()) {
                    ChainElement<QuestionAnswer> next = chainElement.next().get();
                    handleCorrect(userId, next.current());
                    state.put(userId, next);
                } else {
                    handleStop(userId);
                }
            } else {
                handleWrong();
            }
        }

    }

    private void handleNew(Integer userId, QuestionAnswer first) {
        String msg = WELCOME_MSG.concat(first.getQuestion());
        vkApiService.sendMessage(userId, msg);

    }

    private boolean isCorrectAnswer(String msg, QuestionAnswer questionAnswer) {
        return questionAnswer.getCorrectAnswers()
                .stream()
                .anyMatch(s -> Objects.equals(s, msg));
    }

    private void handleCorrect(Integer userId, QuestionAnswer current) {

    }

    private void handleWrong() {

    }

    private void handleStop(Integer userId) {
    }
}
