package com.barinthecityshow.bot.handler;

import com.barinthecityshow.bot.chain.ChainElement;
import com.barinthecityshow.bot.dialog.QuestionAnswer;
import com.barinthecityshow.bot.dialog.chain.DialogChain;
import com.barinthecityshow.bot.service.VkApiService;
import com.barinthecityshow.bot.state.ConcurrentMapState;
import com.barinthecityshow.bot.state.State;
import org.apache.commons.lang3.StringUtils;

public class QuestionAnswerStateMachine implements BotRequestHandler {

    private final State<Integer, ChainElement<QuestionAnswer>> state = ConcurrentMapState.INSTANCE;
    private final DialogChain dialogChain;
    private final VkApiService vkApiService;

    public QuestionAnswerStateMachine(VkApiService vkApiService, DialogChain dialogChain) {
        this.vkApiService = vkApiService;
        this.dialogChain = dialogChain;
    }

    public void handle(Integer userId, String msg) {
        if (!state.containsKey(userId)) {
            if (!StringUtils.equalsIgnoreCase(msg, Messages.START_MSG.getValue())) {
                return;
            }
            if (!vkApiService.isSubscribed(userId)) {
                handleNotSubscribed(userId);
                return;
            }

            ChainElement<QuestionAnswer> first = dialogChain.getFirst();
            handleNew(userId, first.current());
            state.put(userId, first);

        } else {
            ChainElement<QuestionAnswer> chainElement = state.get(userId);
            if (isStopMsg(msg)) {
                handleStop(userId);
                return;
            }
            if (isCorrectAnswer(msg, chainElement.current())) {
                if (chainElement.next().isPresent()) {
                    ChainElement<QuestionAnswer> next = chainElement.next().get();
                    handleNext(userId, next.current());
                    state.put(userId, next);
                } else {
                    handleWin(userId);
                }
            } else {
                handleWrong(userId);
            }
        }

    }

    private void handleNotSubscribed(Integer userId) {
        vkApiService.sendMessage(userId, Messages.SUBSCRIBE_MSG.getValue());
    }

    private void handleNew(Integer userId, QuestionAnswer first) {
        String msg = Messages.WELCOME_MSG.getValue().concat(first.getQuestion());
        vkApiService.sendMessage(userId, msg);
    }

    private boolean isStopMsg(String msg) {
        return StringUtils.equalsIgnoreCase(msg, Messages.STOP_MSG.getValue());
    }

    private boolean isCorrectAnswer(String msg, QuestionAnswer questionAnswer) {
        return questionAnswer.getCorrectAnswers()
                .stream()
                .anyMatch(s -> StringUtils.equalsIgnoreCase(s, msg));
    }

    private void handleNext(Integer userId, QuestionAnswer next) {
        String msg = Messages.CORRECT_ANS_MSG.getValue().concat(next.getQuestion());
        vkApiService.sendMessage(userId, msg);
    }

    private void handleWrong(Integer userId) {
        vkApiService.sendMessage(userId, Messages.WRONG_ANS_MSG.getValue());
    }

    private void handleWin(Integer userId) {
        vkApiService.sendMessage(userId, Messages.WIN_MSG.getValue());
        state.remove(userId);
    }

    private void handleStop(Integer userId) {
        vkApiService.sendMessage(userId, Messages.BYE_MSG.getValue());
        state.remove(userId);
    }
}
