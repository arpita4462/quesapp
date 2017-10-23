package com.atrio.quesapp.model;

/**
 * Created by Admin on 12-10-2017.
 */

public class QuessAnsModel {
    String question;
    String Answer;



    public QuessAnsModel(){

    }
    public QuessAnsModel(String question,String Answer){
        this.question = question;
        this.Answer = Answer;

    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }



}
