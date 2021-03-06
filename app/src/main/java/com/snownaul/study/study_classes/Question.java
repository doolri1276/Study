package com.snownaul.study.study_classes;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by alfo6-11 on 2018-05-03.
 */

public class Question implements Comparable{
    private int QuestionID;
    private int sgSetID;

    private int questionType;
    private String question;
    private String explanation;
    private boolean rightOrWrong;
    private boolean isEditMode;
    private boolean isLiked;
    private String questionPic;

    private int triedCnt;
    private int solvedCnt;
    private int keptCorrection;
    private int timeLength;

    private String questionUri;

    public static final int TYPE_BASIC=0;
    public static final int TYPE_RIGHTORWRONG=1;
    public static final int TYPE_ONEANSWER=2;
    public static final int TYPE_ORDER=3;
    private ArrayList<Answer> answers;

    //테스트 관련 변수들
    private  boolean hasTestAnswer;
    private  boolean testCorrection;
    private  int testTimeLength;
    private String testOneAnswer;


    //업데이트 관련 변수들
    private int updateState;
    public static final int MODE_UNCHAGED=0;
    public static final int MODE_UPDATED=1;
    public static final int MODE_ADDED=2;
    public static final int MODE_DELETED=3;


    public Question(int questionType) {
        this.questionType = questionType;
        answers=new ArrayList<>();
        Answer a=new Answer();
        a.setQuestionID(getQuestionID());
        answers.add(a);
        if(questionType==TYPE_ONEANSWER||questionType==TYPE_ORDER){
            answers.get(0).setCorrect(true);
        }
    }

    public int getUpdateState() {
        return updateState;
    }

    public void setUpdateState(int updateState) {
        this.updateState = updateState;
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

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getTriedCnt() {
        return triedCnt;
    }

    public void setTriedCnt(int triedCnt) {
        this.triedCnt = triedCnt;
    }

    public int getSolvedCnt() {
        return solvedCnt;
    }

    public void setSolvedCnt(int solvedCnt) {
        this.solvedCnt = solvedCnt;
    }

    public int getKeptCorrection() {
        return keptCorrection;
    }

    public void setKeptCorrection(int keptCorrection) {
        this.keptCorrection = keptCorrection;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public boolean isHasTestAnswer() {
        return hasTestAnswer;
    }

    public void setHasTestAnswer(boolean hasTestAnswer) {
        this.hasTestAnswer = hasTestAnswer;
    }

    public boolean isTestCorrection() {
        return testCorrection;
    }

    public void setTestCorrection(boolean testCorrection) {
        this.testCorrection = testCorrection;
    }

    public int getTestTimeLength() {
        return testTimeLength;
    }

    public void setTestTimeLength(int testTimeLength) {
        this.testTimeLength = testTimeLength;
    }

    public String getTestOneAnswer() {
        return testOneAnswer;
    }

    public void setTestOneAnswer(String testOneAnswer) {
        this.testOneAnswer = testOneAnswer;
    }

    public String getQuestionPic() {
        return questionPic;
    }

    public void setQuestionPic(String questionPic) {
        this.questionPic = questionPic;
    }

    public String getQuestionUri() {
        return questionUri;
    }

    public void setQuestionUri(String questionUri) {
        this.questionUri = questionUri;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return solvedCnt-((Question)o).solvedCnt;
    }

    public boolean checkStudyCorrection(){
        boolean correct=true;
//        for(int i=0;i<ta.size();i++){
//            Answer t=ta.get(i);
//            if(t.isCorrect()!=t.isChecked()){
//                correct=false;
//            }
//        }

        return true;

    }
}
