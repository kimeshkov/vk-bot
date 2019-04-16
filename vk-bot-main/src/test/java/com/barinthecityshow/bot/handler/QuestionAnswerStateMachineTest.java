package com.barinthecityshow.bot.handler;

import com.barinthecityshow.bot.dialog.QuestionAnswer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class QuestionAnswerStateMachineTest {

    private String json = "[\n" +
            "  {\n" +
            "    \"question\": \"Кто ты из бара?\",\n" +
            "    \"correctAnswers\": [\n" +
            "      \"никто\",\n" +
            "      \"хз\",\n" +
            "      \"человек\",\n" +
            "      \"шац\"\n" +
            "    ],\n" +
            "    \"options\": [\n" +
            "      \"звер\",\n" +
            "      \"птица\",\n" +
            "      \"шац\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"question\": \"Кто ты из бара?\",\n" +
            "    \"correctAnswers\": [\n" +
            "      \"никто\",\n" +
            "      \"хз\",\n" +
            "      \"человек\",\n" +
            "      \"шац\"\n" +
            "    ],\n" +
            "    \"options\": [\n" +
            "      \"звер\",\n" +
            "      \"птица\",\n" +
            "      \"шац\"\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    @Test
    public void shouldDoNothing_WhenNotStickerMsg() throws Exception {
        QuestionAnswer questionAnswer = QuestionAnswer.builder()
                .question("Кто ты из бара?")
                .addCorrectAnswer("никто")
                .addCorrectAnswer("хз")
                .addCorrectAnswer("человек")
                .addCorrectAnswer("шац")
                .addOption("звер")
                .addOption("птица")
                .addOption("шац")
                .build();
        Gson gson = new GsonBuilder().create();
        System.out.println(gson.toJson(questionAnswer));

        gson.fromJson(json, QuestionAnswer[].class);

    }
}