package com.snownaul.study.study_classes;

/**
 * Created by alfo6-11 on 2018-05-03.
 */

public class Answer {

    private int answerID;
    private int questionID;

    private String answer;
    private boolean isCorrect;
    private int sgOrder=-1;

    private boolean isChecked;

    //업데이트 관련 변수들
    private int updateState;
    public static final int MODE_UNCHAGED=0;
    public static final int MODE_UPDATED=1;
    public static final int MODE_ADDED=2;
    public static final int MODE_DELETED=3;
    public static final int MODE_ADDED_BYQUESTION=3;




    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getSgOrder() {
        return sgOrder;
    }

    public void setSgOrder(int sgOrder) {
        this.sgOrder = sgOrder;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getUpdateState() {
        return updateState;
    }

    public void setUpdateState(int updateState) {
        this.updateState = updateState;
    }
}
