package com.snownaul.study.controls;

import android.util.Log;

import com.snownaul.study.G;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;
import com.snownaul.study.study_classes.StudySet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by alfo6-11 on 2018-05-09.
 */

public class StudyingManager {

    private StudySet studyingSet;
    private ArrayList<Question> studyingQuestions;
    private int minimumSolvedCnt;
    private ArrayList<Question> questionsParts;

    private int numOfQuestionsPerStudy;
    private int i=0;
    private int percentageI;


    public static final int MODE_OPENING=0;
    public static final int MODE_STUDYING=1;
    public static final int MODE_ANSWER=2;
    public static final int MODE_PAUSE=3;
    public static final int MODE_ONEANSWER_CHECK=4;

    public StudySet getStudyingSet() {
        return studyingSet;
    }

    public void setStudyingSet(StudySet studyingSet) {
        this.studyingSet = studyingSet;
    }

    public ArrayList<Question> getStudyingQuestions() {
        return studyingQuestions;
    }

    public void setStudyingQuestions(ArrayList<Question> studyingQuestions) {
        this.studyingQuestions = studyingQuestions;
    }

    public int getMinimumSolvedCnt() {
        return minimumSolvedCnt;
    }

    public void setMinimumSolvedCnt(int minimumSolvedCnt) {
        this.minimumSolvedCnt = minimumSolvedCnt;
    }

    public ArrayList<Question> getQuestionsParts() {
        return questionsParts;
    }

    public void setQuestionsParts(ArrayList<Question> questionsParts) {
        this.questionsParts = questionsParts;
    }

    public int getNumOfQuestionsPerStudy() {
        return numOfQuestionsPerStudy;
    }

    public void setNumOfQuestionsPerStudy(int numOfQuestionsPerStudy) {
        this.numOfQuestionsPerStudy = numOfQuestionsPerStudy;
    }

    public void startStudying(){
            studyingSet= G.currentStudySet;
            studyingQuestions=studyingSet.getSgSet().getQuestions();

            questionsParts=new ArrayList<>();
            shuffleAndSort();

    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getPercentageI() {
        return percentageI;
    }

    public void setPercentageI(int percentageI) {
        this.percentageI = percentageI;
    }

    public Question getQuestion(){
        if(questionsParts.size()>0) {
            if (i >= numOfQuestionsPerStudy) i = 0;
            if(i>=questionsParts.size()) i=0;

            if(percentageI>=numOfQuestionsPerStudy)
                percentageI=0;

            Question t=questionsParts.get(i);
            Log.i("MyTag","TEST : i="+i+" mininumSolved : "+minimumSolvedCnt+" numOfQuestionsPerStudy : "+numOfQuestionsPerStudy+" percentageI : "+percentageI+" " +t.getQuestion());
            i++;

            ArrayList<Answer> ta=t.getAnswers();

            for(int i=0;i<ta.size();i++){
                ta.get(i).setChecked(false);
            }
            Collections.shuffle(ta);

            switch (t.getQuestionType()){
                case Question.TYPE_RIGHTORWRONG:
                    if(new Random().nextInt(10)<5){
                        t.setRightOrWrong(!t.isRightOrWrong());

                        for(int i=0;i<ta.size();i++){
                            ta.get(i).setCorrect(!ta.get(i).isCorrect());
                        }
                    }

                    break;

            }


            return t;

        }else{
            shuffleAndSort();
            return getQuestion();

        }
    }

    //Shuffle and Sort
    public void shuffleAndSort(){
        Collections.shuffle(studyingQuestions);
        Collections.sort(studyingQuestions);
        minimumSolvedCnt=studyingQuestions.get(0).getSolvedCnt();

        for(int i=0;i<studyingQuestions.size();i++){
            if(studyingQuestions.get(i).getSolvedCnt()==minimumSolvedCnt){
                Collections.shuffle(studyingQuestions.get(i).getAnswers());
                questionsParts.add(studyingQuestions.get(i));
            }else break;
        }

        switch (minimumSolvedCnt){
            case 0:
                numOfQuestionsPerStudy=5;
                break;
            case 1:
                numOfQuestionsPerStudy=7;
                break;
            case 2:
                numOfQuestionsPerStudy=10;
                break;
            case 3:
                numOfQuestionsPerStudy=12;
                break;
            case 4:
                numOfQuestionsPerStudy=15;
                break;
            case 5:
                numOfQuestionsPerStudy=20;
                break;
            default:
                numOfQuestionsPerStudy=25;
                break;
        }

        if(studyingQuestions.size()<numOfQuestionsPerStudy){
            //Log.i("MyTag", "비교 ㅣ "+studyingQuestions.size()+"<"+numOfQuestionsPerStudy+"무엇인가요?");
            numOfQuestionsPerStudy=studyingQuestions.size();
            //Log.i("MyTag","더 작아 나는 바꿀래 "+numOfQuestionsPerStudy);
        }



        i=0;

    }

    public boolean checkCorrection(Question q){

        boolean correct=true;

        if(q.getQuestionType()==Question.TYPE_ONEANSWER){
            correct=q.isTestCorrection();
        }else{
            ArrayList<Answer> ta=q.getAnswers();


            for(int i=0;i<ta.size();i++){
                Answer t=ta.get(i);
                if(t.isCorrect()!=t.isChecked()){
                    correct=false;
                }
            }
        }



        if(correct){
            q.setSolvedCnt(q.getSolvedCnt()+1);
            questionsParts.remove(q);
            percentageI++;
            if(i>0)i--;
        }

        return correct;
    }
}
