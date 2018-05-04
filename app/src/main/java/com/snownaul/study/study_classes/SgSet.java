package com.snownaul.study.study_classes;

/**
 * Created by alfo6-11 on 2018-05-03.
 */

public class SgSet {

    private int sgSetID;
    private int subjectID;
    private int userID;//유저의 아이디를 불러올 필요가 있는가?
    private String title;
    private String info;

    private String user_nickname;//

    public SgSet(int userID, String user_nickname) {
        this.userID = userID;
        this.user_nickname = user_nickname;
    }

    public int getSgSetID() {
        return sgSetID;
    }

    public void setSgSetID(int sgSetID) {
        this.sgSetID = sgSetID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }
}
