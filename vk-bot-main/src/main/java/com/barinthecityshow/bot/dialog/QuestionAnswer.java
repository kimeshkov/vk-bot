package com.barinthecityshow.bot.dialog;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswer {
    private String question;
    private List<String> correctAnswers;
    private List<String> options = new ArrayList<>();

    public QuestionAnswer(String question, List<String> correctAnswers) {
        this.question = question;
        this.correctAnswers = correctAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }


    public List<String> getOptions() {
        return options;
    }
}
