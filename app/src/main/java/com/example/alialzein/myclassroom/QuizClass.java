package com.example.alialzein.myclassroom;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ALiAlzein on 3/14/2018.
 */

public class QuizClass {
    ArrayList<QuestionClass> questions = new ArrayList<QuestionClass>();
    int numOfQuestions;
    String quizID;

    public QuizClass(String quizID, ArrayList<QuestionClass> questions, int numOfQuestions) {
        this.questions = questions;
        this.numOfQuestions = numOfQuestions;
    }

    public QuizClass() {

    }


    public ArrayList<QuestionClass> getQuestions() {
        return questions;
    }

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public double QuizResult() {
        int NumcorrectAnswer = 0;
        for (int i = 0; i < numOfQuestions; i++) {
            Log.d("correctA", String.valueOf(NumcorrectAnswer));
            Log.d("correctQ", String.valueOf(questions.get(i).getQuestion()));
            Log.d("correctCA", String.valueOf(questions.get(i).getCorrectAnswer()));
            Log.d("correctSA", String.valueOf(questions.get(i).getStudentAnswer()));


            if (questions.get(i).correctQuestion()) {
                NumcorrectAnswer++;

                Log.d("correctif", String.valueOf(NumcorrectAnswer));
            }



        }
        double weightOfOneQuestion = 100 / numOfQuestions;
        double finalAnswer = weightOfOneQuestion * NumcorrectAnswer;
        return finalAnswer;
    }

    public String getQuizID() {
        return quizID;
    }
}
