package com.barinthecityshow.bot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class BotRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(BotRequestHandler.class);

    private final VkApiClient apiClient;

    private final GroupActor actor;
    private final Random random = new Random();

    private Map<Integer, QuestionAnswer> state = new HashMap<>();

    BotRequestHandler(VkApiClient apiClient, GroupActor actor) {
        this.apiClient = apiClient;
        this.actor = actor;
    }

    void handle(int userId, String msg) {
        try {
            if (state.containsKey(userId)) {
                QuestionAnswer questionAnswer = state.get(userId);
                if (questionAnswer.getAnswer() == null) {
                    state.put(userId, questionAnswer.answer(msg));
                    apiClient.messages()
                            .send(actor)
                            .message("Принято!")
                            .userId(userId)
                            .randomId(random.nextInt())
                            .execute();

                } else {
                    apiClient.messages()
                            .send(actor)
                            .message("Ты уже ответил: " + questionAnswer.getAnswer())
                            .userId(userId)
                            .randomId(random.nextInt())
                            .execute();
                }

            } else {
                state.put(userId, new QuestionAnswer("Привет, хочешь стикер? Ответь на вопрос: Ты пидор?"));

                apiClient.messages()
                        .send(actor)
                        .message(state.get(userId).getQuestion())
                        .userId(userId)
                        .randomId(random.nextInt())
                        .execute();

            }

        } catch (ApiException e) {
            LOG.error("INVALID REQUEST", e);
        } catch (ClientException e) {
            LOG.error("NETWORK ERROR", e);
        }
    }
}
