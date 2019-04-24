package com.barinthecityshow.bot;

import com.barinthecityshow.bot.dialog.QuestionAnswer;
import com.barinthecityshow.bot.dialog.chain.DialogChain;
import com.barinthecityshow.bot.dialog.chain.StickerDialogChain;
import com.barinthecityshow.bot.handler.QuestionAnswerStateMachine;
import com.barinthecityshow.bot.service.VkApiService;
import com.google.gson.Gson;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(VkApiService.class);

    private final static String PROPERTIES_FILE = "config.properties";

    public static void main(String[] args) throws Exception {
        LOG.info("Start");
        Properties properties = readProperties();

        HttpTransportClient client = new HttpTransportClient();
        VkApiClient apiClient = new VkApiClient(client);

        GroupActor actor = initVkApi(apiClient, readProperties());
        VkApiService vkApiService = new VkApiService(apiClient, actor);

        QuestionAnswerStateMachine botHandler = new QuestionAnswerStateMachine(vkApiService, initDialogChain());

        Server server = new Server(8080);
        server.setHandler(new RequestHandler(botHandler, properties.getProperty("confirmationCode")));
        server.start();

        LOG.info("Started");
        server.join();
    }

    private static DialogChain initDialogChain() {
        Path path = Paths.get("qa.json");

        LOG.info("Try reading qa from file");
        try {
            String json = Files.readAllLines(path).stream().collect(Collectors.joining());

            Gson gson = new Gson();
            List<QuestionAnswer> questionAnswers = Arrays.stream(gson.fromJson(json, QuestionAnswer[].class))
                    .collect(Collectors.toList());

            return new StickerDialogChain(questionAnswers);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static GroupActor initVkApi(VkApiClient apiClient, Properties properties) {
        int groupId = Integer.parseInt(properties.getProperty("groupId"));
        String token = properties.getProperty("token");
        int serverId = Integer.parseInt(properties.getProperty("serverId"));
        if (groupId == 0 || token == null || serverId == 0) throw new RuntimeException("Params are not set");
        GroupActor actor = new GroupActor(groupId, token);

        try {
            apiClient.groups().setCallbackSettings(actor, serverId).messageNew(true).execute();
        } catch (ApiException e) {
            throw new RuntimeException("Api error during init", e);
        } catch (ClientException e) {
            throw new RuntimeException("Client error during init", e);
        }

        return actor;
    }

    private static Properties readProperties() throws FileNotFoundException {
        InputStream inputStream = Application.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (inputStream == null)
            throw new FileNotFoundException("property file '" + PROPERTIES_FILE + "' not found in the classpath");

        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Incorrect properties file");
        }
    }
}
