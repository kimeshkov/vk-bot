package com.barinthecityshow.bot;

public class QuestionAnswer {
    private String question;
    private String answer;

    QuestionAnswer(String question) {
        this.question = question;
    }

    public QuestionAnswer(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public QuestionAnswer answer(String answer) {
        return new QuestionAnswer(question, answer);
    }

    public static QuestionAnswer forQuestion(String question) {
        return new QuestionAnswer(question);
    }




}
