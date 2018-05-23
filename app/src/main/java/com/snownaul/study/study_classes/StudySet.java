package com.snownaul.study.study_classes;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alfo6-11 on 2018-05-03.
 */

public class StudySet implements Comparable{

    private int StudySetId;
    private int sgSetID;
    private int userID;//이건 무조건 내 아이디가 될건데 필요한가..... 아니다..
    private String recentDate;
    private int keptCorrectionCnt;

    private SgSet sgSet;
    private boolean isLiked;
    private int triedCnt;
    private int timeLength;
    private boolean isSetting;
    private boolean isEditable;
    private boolean isEditMode;



    public int getStudySetId() {
        return StudySetId;
    }

    public void setStudySetId(int studySetId) {
        StudySetId = studySetId;
    }

    public int getSgSetID() {
        return sgSetID;
    }

    public void setSgSetID(int sgSetID) {
        this.sgSetID = sgSetID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public SgSet getSgSet() {
        return sgSet;
    }

    public void setSgSet(SgSet sgSet) {
        this.sgSet = sgSet;
    }

    public String getRecentDate() {
        return recentDate;
    }

    public void setRecentDate(String recentDate) {
        this.recentDate = recentDate;
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

    public boolean isSetting() {
        return isSetting;
    }

    public void setSetting(boolean setting) {
        isSetting = setting;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public int getKeptCorrectionCnt() {
        return keptCorrectionCnt;
    }

    public void setKeptCorrectionCnt(int keptCorrectionCnt) {
        this.keptCorrectionCnt = keptCorrectionCnt;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            Date date1=sdf.parse(recentDate);
            long milliseconds=date1.getTime();

            Date date2=sdf.parse(((StudySet)o).recentDate);
            long milliseconds2=date2.getTime();
            return (int) (milliseconds2-milliseconds);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;



    }
}
