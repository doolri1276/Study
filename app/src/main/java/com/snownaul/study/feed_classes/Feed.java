package com.snownaul.study.feed_classes;

import java.util.ArrayList;

/**
 * Created by alfo6-11 on 2018-05-18.
 */

public class Feed {

    //DB의 내용들..
    private int feedID;
    private int userID;
    private String contents;
    private String imgPath;
    private String date;

    private String profImgPath;
    private String nickname;
    private boolean isLiked;
    private int likeCnt;
    private boolean isCommented;
    private int commentCnt;
    private boolean isFeedMarked;

    private ArrayList<Comment> comments;

    public Feed(int feedID, int userID, String contents, String imgPath, String date, String profImgPath, String nickname, boolean isLiked, int likeCnt, boolean isCommented, int commentCnt, boolean isFeedMarked) {
        this.feedID = feedID;
        this.userID = userID;
        this.contents = contents;
        this.imgPath = imgPath;
        this.date = date;
        this.profImgPath = profImgPath;
        this.nickname = nickname;
        this.isLiked = isLiked;
        this.likeCnt = likeCnt;
        this.isCommented = isCommented;
        this.commentCnt = commentCnt;
        this.isFeedMarked = isFeedMarked;

        comments=new ArrayList<>();
    }

    public int getFeedID() {
        return feedID;
    }

    public void setFeedID(int feedID) {
        this.feedID = feedID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public boolean isCommented() {
        return isCommented;
    }

    public void setCommented(boolean commented) {
        isCommented = commented;
    }

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getProfImgPath() {
        return profImgPath;
    }

    public void setProfImgPath(String profImgPath) {
        this.profImgPath = profImgPath;
    }

    public boolean isFeedMarked() {
        return isFeedMarked;
    }

    public void setFeedMarked(boolean feedMarked) {
        isFeedMarked = feedMarked;
    }
}


