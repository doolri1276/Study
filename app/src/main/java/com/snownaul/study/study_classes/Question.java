package com.snownaul.study.study_classes;

import java.util.ArrayList;

/**
 * Created by alfo6-11 on 2018-05-03.
 */

public class Question {
    private int QuestionID;
    private int sgSetID;

    private int questionType;
    private String question;
    private String explanation;
    private boolean rightOrWrong;

    public static final int TYPE_BASIC=0;
    public static final int TYPE_RIGHTORWRONG=1;
    public static final int TYPE_ONEANSWER=2;
    public static final int TYPE_ORDER=3;

    private ArrayList<Answer> answers;

    public Question(int questionType) {
        this.questionType = questionType;
        answers=new ArrayList<>();
        answers.add(new Answer());
        if(questionType==TYPE_ONEANSWER||questionType==TYPE_ORDER){
            answers.get(0).setCorrect(true);
        }
    }

    public int getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(int questionID) {
        QuestionID = questionID;
    }

    public int getSgSetID() {
        return sgSetID;
    }

    public void setSgSetID(int sgSetID) {
        this.sgSetID = sgSetID;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public boolean isRightOrWrong() {
        return rightOrWrong;
    }

    public void setRightOrWrong(boolean rightOrWrong) {
        this.rightOrWrong = rightOrWrong;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
}
