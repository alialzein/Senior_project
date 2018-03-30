package com.example.alialzein.myclassroom;

/**
 * Created by ALiAlzein on 3/14/2018.
 */

public class QuestionClass {
     String question;
    String[] choices=new String[4];
    int correctAnswer;
    int studentAnswer;

    public QuestionClass(String question, String[] choices, int correctAnswer) {
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    public QuestionClass(String question, String[] choices, int correctAnswer, int studentAnswer) {
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
        this.studentAnswer = studentAnswer;
    }

    public QuestionClass() {
    }

    public String getQuestion() {
        return question;
    }

    public String[] getChoices() {
        return choices;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean correctQuestion(){
        return studentAnswer==correctAnswer;

    }

}
