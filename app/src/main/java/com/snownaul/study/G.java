package com.snownaul.study;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.snownaul.study.study_classes.Question;
import com.snownaul.study.study_classes.SgSet;
import com.snownaul.study.study_classes.StudySet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public static SharedPreferences PREF;

    //Study에 관련
    public static SgSet newSgSet;
    public static ArrayList<Question> newQuestions;

    public static ArrayList<StudySet> studySets;

    public static StudySet currentStudySet;


    public static void openG(SharedPreferences pref){

        G.PREF=pref;

        USER_ID=pref.getInt("userID",-99);
        USER_SNS=pref.getString("userSns",null);
        USER_SNSID=pref.getLong("userSnsId",-99);
        USER_NICKNAME=pref.getString("userNickname",null);
        USER_AGE=pref.getInt("userAge",-99);
        USER_PROFILEPIC=pref.getString("userProfilePic",null);

        studySets=new ArrayList<>();
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
