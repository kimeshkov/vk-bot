package com.barinthecityshow.bot.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class VkApiService {
    private static final Logger LOG = LoggerFactory.getLogger(VkApiService.class);

    private final VkApiClient apiClient;
    private final GroupActor actor;
    private final Random random = new Random();


    public VkApiService(VkApiClient apiClient, GroupActor actor) {
        this.apiClient = apiClient;
        this.actor = actor;
    }

    public void sendMessage(Integer userId, String msg) {
        try {
            apiClient.messages()
                    .send(actor)
                    .message(msg)
                    .userId(userId)
                    .randomId(random.nextInt())
                    .execute();

        } catch (ApiException e) {
            LOG.error("INVALID REQUEST", e);
        } catch (ClientException e) {
            LOG.error("NETWORK ERROR", e);
        }
    }
}
