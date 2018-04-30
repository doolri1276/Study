package com.snownaul.study;

import android.content.SharedPreferences;

import com.snownaul.study.Activities.IntroActivity;

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


    public static void openG(SharedPreferences pref){

        G.PREF=pref;

        USER_ID=pref.getInt("userID",-99);
        USER_SNS=pref.getString("userSns",null);
        USER_SNSID=pref.getLong("userSnsId",-99);
        USER_NICKNAME=pref.getString("userNickname",null);
        USER_AGE=pref.getInt("userAge",-99);
        USER_PROFILEPIC=pref.getString("userProfilePic",null);

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
