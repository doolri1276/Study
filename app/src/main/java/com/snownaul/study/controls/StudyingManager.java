package com.snownaul.study.controls;

import com.snownaul.study.G;
import com.snownaul.study.study_classes.Question;
import com.snownaul.study.study_classes.StudySet;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by alfo6-11 on 2018-05-09.
 */

public class StudyingManager {

    StudySet studyingSet;
    ArrayList<Question> studyingQuestions;
    int minimumSolvedCnt;

    public void StartStudying(){
            studyingSet= G.currentStudySet;
            studyingQuestions=studyingSet.getSgSet().getQuestions();

            shuffleAndSort();


    }

    //Shuffle and Sort
    public void shuffleAndSort(){
        Collections.shuffle(studyingQuestions);
        Collections.sort(studyingQuestions);
        minimumSolvedCnt=studyingQuestions.get(0).getSolvedCnt();

    }
}
