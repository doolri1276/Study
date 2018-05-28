package com.snownaul.study;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.snownaul.study.feed_classes.Comment;
import com.snownaul.study.feed_classes.Feed;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;
import com.snownaul.study.study_classes.SgSet;
import com.snownaul.study.study_classes.StudySet;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by alfo6-11 on 2018-04-27.
 */

public class G {

    public static int USER_ID;
    public static String USER_SNS;
    public static long USER_SNSID;
    public static String USER_NICKNAME;
    public static int USER_AGE;
    public static String USER_PROFILEPIC;

    public static int TEST_RGQ;
    public static int TEST_RGQ_DEFAULT;
    public static int TEST_RGT;
    public static int TEST_RGT_DEFAULT;
    public static int TEST_TYPING;

    public static SharedPreferences PREF;

    //Study에 관련
    public static SgSet newSgSet;
    public static ArrayList<Question> newQuestions;

    public static ArrayList<StudySet> studySets;

    public static StudySet currentStudySet;
    public static SgSet updateSgSet;
    public static ArrayList<Question> deletedQuestions;
    public static ArrayList<Answer> deletedAnswers;

    //Feed관련...
    public static ArrayList<Feed> feeds;
    public static Feed currentFeed;
    public static Comment currentComment;


    public static void openG(SharedPreferences pref){

        G.PREF=pref;

        USER_ID=pref.getInt("userID",-99);
        USER_SNS=pref.getString("userSns",null);
        USER_SNSID=pref.getLong("userSnsId",-99);
        USER_NICKNAME=pref.getString("userNickname",null);
        USER_AGE=pref.getInt("userAge",-99);
        USER_PROFILEPIC=pref.getString("userProfilePic",null);

        studySets=new ArrayList<>();

        TEST_RGQ=pref.getInt("testRgq",0);
        TEST_RGQ_DEFAULT=pref.getInt("testRgqDefault",25);
        TEST_RGT=pref.getInt("testRgt",0);
        TEST_RGT_DEFAULT=pref.getInt("testRgtDefault",25);
        TEST_TYPING=pref.getInt("testTyping",0);

        feeds=new ArrayList<>();

    }


    public static long convertLocalTimeToUTC(long pv_localDateTime){
        long lv_UTCTime=pv_localDateTime;

        TimeZone z=TimeZone.getDefault();

        int offset=z.getOffset(pv_localDateTime);
        lv_UTCTime=pv_localDateTime-offset;
        return lv_UTCTime;
    }

    public static String convertUTCToLocalTime(String datetime){
        String locTime=null;

        TimeZone tz= TimeZone.getDefault();

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            Date parseDate=sdf.parse(datetime);
            long milliseconds=parseDate.getTime();
            int offset=tz.getOffset(milliseconds);
            locTime=sdf.format(milliseconds+offset);
            locTime=locTime.replace("+0000","");

        }catch (Exception e){
            e.printStackTrace();
        }
        return locTime;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void loadCurrentSet(Context context){
        //TODO: 문제들 받아오기...
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Set/loadAllQuestions.php";
        Log.i("MyTag",serverUrl+"에서 받아오려고 합니다.");

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("MyTag","받아왔습니다!! : "+response);

                String[] questionInfos=response.split("&Q&");
                String[] setInfos=questionInfos[0].split("&");
                G.currentStudySet.getSgSet().setTitle(setInfos[0]);
                G.currentStudySet.getSgSet().setInfo(setInfos[1]);
                G.currentStudySet.setEditable(Boolean.parseBoolean(setInfos[2]));
                G.currentStudySet.getSgSet().setLikeCnt(Integer.parseInt(setInfos[3]));
                G.currentStudySet.setLiked(Boolean.parseBoolean(setInfos[4]));
                G.currentStudySet.setTriedCnt(Integer.parseInt(setInfos[5]));
                G.currentStudySet.setTimeLength(Integer.parseInt(setInfos[6]));
                G.currentStudySet.setRecentDate(G.convertUTCToLocalTime(setInfos[7]));
                G.currentStudySet.setKeptCorrectionCnt(Integer.parseInt(setInfos[8]));

                G.currentStudySet.getSgSet().getQuestions().clear();

                for(int i=1;i<questionInfos.length;i++){
                    String[] answerInfos=questionInfos[i].split("&A&");
                    String[] questionDetailInfos=answerInfos[0].split("&");

                    Log.i("MyTag","Check check : "+Arrays.toString(questionDetailInfos));
                    Question q=new Question(Integer.parseInt(questionDetailInfos[1]));
                    q.setQuestionID(Integer.parseInt(questionDetailInfos[0]));
                    q.setQuestion(questionDetailInfos[2]);
                    q.setExplanation(questionDetailInfos[3]);
                    q.setRightOrWrong(Boolean.parseBoolean(questionDetailInfos[4]));

                    q.setTriedCnt(Integer.parseInt(questionDetailInfos[5]));
                    q.setSolvedCnt(Integer.parseInt(questionDetailInfos[6]));
                    q.setKeptCorrection(Integer.parseInt(questionDetailInfos[7]));
                    q.setTimeLength(Integer.parseInt(questionDetailInfos[8]));

                    q.getAnswers().clear();
                    for(int j=1;j<answerInfos.length;j++){
                        String[] answerDetailInfos=answerInfos[j].split("&");

                        Answer a=new Answer();
                        a.setAnswerID(Integer.parseInt(answerDetailInfos[0]));
                        a.setAnswer(answerDetailInfos[1]);
                        a.setCorrect(answerDetailInfos[2].equals("1")?true:false);
                        a.setSgOrder(Integer.parseInt(answerDetailInfos[3]));

                        q.getAnswers().add(q.getAnswers().size(),a);
                    }

                    G.currentStudySet.getSgSet().getQuestions().add(G.currentStudySet.getSgSet().getQuestions().size(),q);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("MyTag",error.getMessage()+"");
            }
        });

        multiPartRequest.addStringParam("studySetID",G.currentStudySet.getStudySetId()+"");
        multiPartRequest.addStringParam("userID",G.getUserId()+"");
        multiPartRequest.addStringParam("sgSetID",G.currentStudySet.getSgSetID()+"");

        RequestQueue requestQueue= Volley.newRequestQueue(context);

        Log.i("MyTag","loadCurrentSet을 했다... studySetID : "+G.currentStudySet.getStudySetId()+" userID : "+G.getUserId()+" sgSetID : "+G.currentStudySet.getSgSetID());

        requestQueue.add(multiPartRequest);
        Log.i("MyTag","보냈습니다.");

    }



    public static void setUserId(int USER_ID){
        G.USER_ID=USER_ID;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putInt("userID",USER_ID);
        editor.commit();
    }

    public static void setUserSns(String USER_SNS){
        G.USER_SNS=USER_SNS;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putString("userSns",USER_SNS);
        editor.commit();
    }

    public static void setUserSnsId(long USER_SNSID){
        G.USER_SNSID=USER_SNSID;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putLong("userSnsId",USER_SNSID);
        editor.commit();
    }

    public static void setUserNickname(String USER_NICKNAME){
        G.USER_NICKNAME=USER_NICKNAME;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putString("userNickname",USER_NICKNAME);
        editor.commit();
    }

    public static void setUserAge(int USER_AGE){
        G.USER_AGE=USER_AGE;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putInt("userAge",USER_AGE);
        editor.commit();
    }

    public static void setUserProfilepic(String USER_PROFILEPIC){
        G.USER_PROFILEPIC=USER_PROFILEPIC;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putString("userProfilePic",USER_PROFILEPIC);
        editor.commit();
    }

    public static void setUserSnsid(long userSnsid) {
        USER_SNSID = userSnsid;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putLong("userSnsId",USER_SNSID);
        editor.commit();
    }

    public static void setTestRgq(int testRgq) {
        TEST_RGQ = testRgq;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putInt("testRgq",TEST_RGQ);
        editor.commit();
    }

    public static void setTestRgqDefault(int testRgqDefault) {
        TEST_RGQ_DEFAULT = testRgqDefault;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putInt("testRgqDefault",TEST_RGQ_DEFAULT);
        editor.commit();
    }

    public static void setTestRgt(int testRgt) {
        TEST_RGT = testRgt;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putInt("testRgt",TEST_RGT);
        editor.commit();
    }

    public static void setTestRgtDefault(int testRgtDefault) {
        TEST_RGT_DEFAULT = testRgtDefault;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putInt("testRgtDefault",TEST_RGT_DEFAULT);
        editor.commit();
    }

    public static void setTestTyping(int testTyping) {
        TEST_TYPING = testTyping;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putInt("testTyping",TEST_TYPING);
        editor.commit();

    }

    public static int getTestTyping() {
        return TEST_TYPING;
    }

    public static int getUserId() {
        return USER_ID;
    }

    public static String getUserSns() {
        return USER_SNS;
    }

    public static long getUserSnsid() {
        return USER_SNSID;
    }

    public static int getUserAge() {
        return USER_AGE;
    }

    public static String getUserNickname() {
        return USER_NICKNAME;
    }

    public static String getUserProfilepic() {
        return USER_PROFILEPIC;
    }

}
