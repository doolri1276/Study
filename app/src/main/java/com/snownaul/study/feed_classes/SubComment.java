package com.snownaul.study.feed_classes;

/**
 * Created by alfo6-11 on 2018-05-18.
 */

public class SubComment {

    //DB
    private int subCommentID;
    private int commentID;
    private int userID;
    private String contents;
    private String date;

    private String imgPath;
    private boolean isLiked;
    private int likeCnt;

    public int getSubCommentID() {
        return subCommentID;
    }

    public void setSubCommentID(int subCommentID) {
        this.subCommentID = subCommentID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
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
}
