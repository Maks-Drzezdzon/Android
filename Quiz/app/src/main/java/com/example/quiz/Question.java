package com.example.quiz;

public class Question {
    private int answerId;
    private boolean answerTrue;

    public Question(int answerId, boolean answerTrue) {
        this.answerId = answerId;
        this.answerTrue = answerTrue;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }
}
