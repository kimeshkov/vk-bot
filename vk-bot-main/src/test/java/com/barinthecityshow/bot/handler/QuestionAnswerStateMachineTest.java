package com.barinthecityshow.bot.handler;

import com.barinthecityshow.bot.dialog.QuestionAnswer;
import com.barinthecityshow.bot.dialog.chain.DialogChain;
import com.barinthecityshow.bot.dialog.chain.QuestionAnswerChainElement;
import com.barinthecityshow.bot.service.VkApiService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionAnswerStateMachineTest {

    @Mock
    private VkApiService vkApiService;

    @Mock
    private DialogChain dialogChain;

    private QuestionAnswerStateMachine questionAnswerStateMachine;

    @Before
    public void init() throws Exception {
        reset(vkApiService, dialogChain);
        questionAnswerStateMachine = new QuestionAnswerStateMachine(vkApiService, dialogChain);
    }

    @Test
    public void shouldDoNothing_WhenNotStickerMsg() throws Exception {
        //arrange
        String msg = "Not sticker Msg";
        Integer userId = new Random().nextInt();

        //act
        questionAnswerStateMachine.handle(userId, msg);

        //verify
        verify(vkApiService, never()).sendMessage(anyInt(), anyString());

    }

    @Test
    public void shouldSendSubscribeMsg_WhenStickerMsgAndUserNotSubscribed() throws Exception {
        //arrange
        String msg = "Хочу стикер";
        Integer userId = new Random().nextInt();

        when(vkApiService.isSubscribed(userId)).thenReturn(false);

        //act
        questionAnswerStateMachine.handle(userId, msg);

        //verify
        verify(vkApiService).sendMessage(userId, Messages.SUBSCRIBE_MSG.getValue());

    }

    @Test
    public void shouldSendFirstQuestion_WhenStickerMsgAndUserSubscribed() throws Exception {
        //arrange
        String msg = "Хочу стикер";
        Integer userId = new Random().nextInt();

        QuestionAnswer questionAnswer = QuestionAnswer.builder()
                .question("What question")
                .build();

        QuestionAnswerChainElement chainElement = new QuestionAnswerChainElement(questionAnswer);


        when(vkApiService.isSubscribed(userId)).thenReturn(true);
        when(dialogChain.getFirst()).thenReturn(chainElement);

        //act
        questionAnswerStateMachine.handle(userId, msg);

        //verify
        String result = Messages.WELCOME_MSG.getValue().concat(questionAnswer.getQuestion());
        verify(vkApiService).sendMessage(userId, result);

    }

    @Test
    public void shouldSendTryAgain_WhenWrongAnswer() throws Exception {
        //arrange
        String msg = "Хочу стикер";
        String answer = "Wrong";
        Integer userId = new Random().nextInt();

        QuestionAnswer questionAnswer = QuestionAnswer.builder()
                .question("What question")
                .addCorrectAnswer("Correct")
                .build();

        QuestionAnswerChainElement chainElement = new QuestionAnswerChainElement(questionAnswer);


        when(vkApiService.isSubscribed(userId)).thenReturn(true);
        when(dialogChain.getFirst()).thenReturn(chainElement);

        //act
        questionAnswerStateMachine.handle(userId, msg);
        questionAnswerStateMachine.handle(userId, answer);

        //verify
        verify(vkApiService).sendMessage(userId, Messages.WRONG_ANS_MSG.getValue());

    }

    @Test
    public void shouldSendWinMsg_WhenCorrectAnswer_AndNoMoreQuestions() throws Exception {
        //arrange
        String msg = "Хочу стикер";
        String answer = "Correct";
        Integer userId = new Random().nextInt();

        QuestionAnswer questionAnswer = QuestionAnswer.builder()
                .question("What question")
                .addCorrectAnswer("Correct")
                .build();

        QuestionAnswerChainElement chainElement = new QuestionAnswerChainElement(questionAnswer);


        when(vkApiService.isSubscribed(userId)).thenReturn(true);
        when(dialogChain.getFirst()).thenReturn(chainElement);

        //act
        questionAnswerStateMachine.handle(userId, msg);
        questionAnswerStateMachine.handle(userId, answer);

        //verify
        verify(vkApiService).sendMessage(userId, Messages.WIN_MSG.getValue());

    }

    @Test
    public void не_Должно_Быть_Разницы_Между_Е_И_Ё() throws Exception {
        //arrange
        String msg = "Хочу стикер";
        String answer = "небо";
        Integer userId = new Random().nextInt();

        QuestionAnswer questionAnswer = QuestionAnswer.builder()
                .question("What question")
                .addCorrectAnswer("Нёбо")
                .build();

        QuestionAnswerChainElement chainElement = new QuestionAnswerChainElement(questionAnswer);


        when(vkApiService.isSubscribed(userId)).thenReturn(true);
        when(dialogChain.getFirst()).thenReturn(chainElement);

        //act
        questionAnswerStateMachine.handle(userId, msg);
        questionAnswerStateMachine.handle(userId, answer);

        //verify
        verify(vkApiService).sendMessage(userId, Messages.WIN_MSG.getValue());

    }
}