package com.snownaul.study.feed_classes;

import java.util.ArrayList;

/**
 * Created by alfo6-11 on 2018-05-18.
 */

public class Comment {

    //DB
    private int commentID;
    private int feedID;
    private int userID;
    private String contents;
    private String date;

    private String imgPath;
    private boolean isLiked;
    private int likeCnt;
    private boolean isSubCommented;
    private int SubCommentCnt;

    private ArrayList<SubComment> subComments;

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
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

    public boolean isSubCommented() {
        return isSubCommented;
    }

    public void setSubCommented(boolean subCommented) {
        isSubCommented = subCommented;
    }

    public int getSubCommentCnt() {
        return SubCommentCnt;
    }

    public void setSubCommentCnt(int subCommentCnt) {
        SubCommentCnt = subCommentCnt;
    }

    public ArrayList<SubComment> getSubComments() {
        return subComments;
    }

    public void setSubComments(ArrayList<SubComment> subComments) {
        this.subComments = subComments;
    }
}
