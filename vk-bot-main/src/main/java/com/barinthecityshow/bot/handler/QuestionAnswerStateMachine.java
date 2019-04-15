package com.barinthecityshow.bot.handler;

import com.barinthecityshow.bot.chain.ChainElement;
import com.barinthecityshow.bot.dialog.QuestionAnswer;
import com.barinthecityshow.bot.dialog.chain.DialogChain;
import com.barinthecityshow.bot.dialog.chain.StickerDialogChain;
import com.barinthecityshow.bot.service.VkApiService;
import com.barinthecityshow.bot.state.ConcurrentMapState;
import com.barinthecityshow.bot.state.State;
import org.apache.commons.lang3.StringUtils;

public class QuestionAnswerStateMachine implements BotRequestHandler {
    private static final String WELCOME_MSG = "Привет, раз хочешь стикер, ответь на вопрос: ";
    private static final String SUBSCRIBE_MSG = "Подпишись и попробуй заново";
    private static final String CORRECT_ANS_MSG = "Правильно! Следующий вопрос: ";
    private static final String WRONG_ANS_MSG = "Эх, неправильно. Напиши СТОП, если сдаешься или попробуй еще раз!";
    private static final String WIN_MSG = "Ура, привильно! Держи стикер";
    private static final String BYE_MSG = "Ок, возвращайся потом";
    private static final String START_MSG = "Хочу стикер";
    private static final String STOP_MSG = "Стоп";


    private final State<Integer, ChainElement<QuestionAnswer>> state = ConcurrentMapState.INSTANCE;
    private final DialogChain dialogChain = new StickerDialogChain();
    private final VkApiService vkApiService;

    public QuestionAnswerStateMachine(VkApiService vkApiService) {
        this.vkApiService = vkApiService;
    }

    public void handle(Integer userId, String msg) {
        if (!state.containsKey(userId)) {
            if (!StringUtils.equalsIgnoreCase(msg, START_MSG)) {
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
        vkApiService.sendMessage(userId, SUBSCRIBE_MSG);
    }

    private void handleNew(Integer userId, QuestionAnswer first) {
        String msg = WELCOME_MSG.concat(first.getQuestion());
        vkApiService.sendMessage(userId, msg);
    }

    private boolean isStopMsg(String msg) {
        return StringUtils.equalsIgnoreCase(msg, STOP_MSG);
    }

    private boolean isCorrectAnswer(String msg, QuestionAnswer questionAnswer) {
        return questionAnswer.getCorrectAnswers()
                .stream()
                .anyMatch(s -> StringUtils.equalsIgnoreCase(s, msg));
    }

    private void handleNext(Integer userId, QuestionAnswer next) {
        String msg = CORRECT_ANS_MSG.concat(next.getQuestion());
        vkApiService.sendMessage(userId, msg);
    }

    private void handleWrong(Integer userId) {
        vkApiService.sendMessage(userId, WRONG_ANS_MSG);
    }

    private void handleWin(Integer userId) {
        vkApiService.sendMessage(userId, WIN_MSG);
        state.remove(userId);
    }

    private void handleStop(Integer userId) {
        vkApiService.sendMessage(userId, BYE_MSG);
        state.remove(userId);
    }
}
