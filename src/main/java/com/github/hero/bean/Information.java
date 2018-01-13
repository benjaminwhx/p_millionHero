package com.github.hero.bean;

import java.util.Arrays;

/**
 * User: benjamin.wuhaixu
 * Date: 2018-01-13
 * Time: 00:59 am
 */
public class Information {
    private String question;
    private String[] answers;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Information{" +
                "question='" + question + '\'' +
                ", answers=" + Arrays.toString(answers) +
                '}';
    }
}
