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
    private String nickname;
    private boolean isLiked;
    private int likeCnt;

    public SubComment(int subCommentID, int commentID, int userID, String contents, String date, String imgPath, String nickname, boolean isLiked, int likeCnt) {
        this.subCommentID = subCommentID;
        this.commentID = commentID;
        this.userID = userID;
        this.contents = contents;
        this.date = date;
        this.imgPath = imgPath;
        this.nickname = nickname;
        this.isLiked = isLiked;
        this.likeCnt = likeCnt;
    }

    public SubComment(int subCommentID, int commentID, int userID, String contents, String date, String imgPath, boolean isLiked, int likeCnt) {
        this.subCommentID = subCommentID;
        this.commentID = commentID;
        this.userID = userID;
        this.contents = contents;
        this.date = date;
        this.imgPath = imgPath;
        this.isLiked = isLiked;
        this.likeCnt = likeCnt;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
