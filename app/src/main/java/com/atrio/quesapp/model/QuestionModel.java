package com.atrio.quesapp.model;

/**
 * Created by Arpita Patel on 14-06-2017.
 */

public class QuestionModel {
    public String quesNo;
    public String question;
    public String opA;
    public String opB;
    public String opC;
    public String opD;
    public String correctOp;

    public QuestionModel() {
    }

    public QuestionModel(String quesNo, String question, String opA, String opB, String opC, String opD, String correctOp) {
        this.quesNo = quesNo;
        this.question = question;
        this.opA = opA;
        this.opB = opB;
        this.opC = opC;
        this.opD = opD;
        this.correctOp = correctOp;
    }

    public String getQuesNo() {
        return quesNo;
    }

    public void setQuesNo(String quesNo) {
        this.quesNo = quesNo;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOpA() {
        return opA;
    }

    public void setOpA(String opA) {
        this.opA = opA;
    }

    public String getOpB() {
        return opB;
    }

    public void setOpB(String opB) {
        this.opB = opB;
    }

    public String getOpC() {
        return opC;
    }

    public void setOpC(String opC) {
        this.opC = opC;
    }

    public String getOpD() {
        return opD;
    }

    public void setOpD(String opD) {
        this.opD = opD;
    }

    public String getCorrectOp() {
        return correctOp;
    }

    public void setCorrectOp(String correctOp) {
        this.correctOp = correctOp;
    }
}
