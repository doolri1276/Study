package com.snownaul.study.report_classes;

/**
 * Created by alfo6-11 on 2018-05-29.
 */

public class Test {

    private int testID;
    private int studySetID;
    private int questionCnt;
    private int correctionCnt;
    private int timeLength;
    private String date;


    private int score;
    private String grade;

    public Test(int testID, int studySetID, int questionCnt, int correctionCnt, int timeLength, String date) {
        this.testID = testID;
        this.studySetID = studySetID;
        this.questionCnt = questionCnt;
        this.correctionCnt = correctionCnt;
        this.timeLength = timeLength;
        this.date = date;

        score=correctionCnt*100/questionCnt;

        if(score>96){
            grade="A+";
        }else if(score>93){
            grade="A ";
        }else if(score>89){
            grade="A-";
        }else if(score>86){
            grade="B+";
        }else if(score>83){
            grade="B ";
        }else if(score>79){
            grade="B-";
        }else if(score>76){
            grade="C+";
        }else if(score>73){
            grade="C ";
        }else if(score>69){
            grade="C-";
        }else if(score>66){
            grade="D+";
        }else if(score>63){
            grade="D ";
        }else if(score>59){
            grade="D-";
        }else{
            grade="F ";
        }

    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public int getStudySetID() {
        return studySetID;
    }

    public void setStudySetID(int studySetID) {
        this.studySetID = studySetID;
    }

    public int getQuestionCnt() {
        return questionCnt;
    }

    public void setQuestionCnt(int questionCnt) {
        this.questionCnt = questionCnt;
    }

    public int getCorrectionCnt() {
        return correctionCnt;
    }

    public void setCorrectionCnt(int correctionCnt) {
        this.correctionCnt = correctionCnt;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
