package com.snownaul.study;

import android.content.SharedPreferences;

import com.snownaul.study.Activities.IntroActivity;

/**
 * Created by alfo6-11 on 2018-04-27.
 */

public class G {

    public static int USER_ID;

    public static SharedPreferences PREF;


    public static void openG(SharedPreferences pref){

        G.PREF=pref;

        USER_ID=pref.getInt("userID",-99);

    }

    public static void setUserId(int USER_ID){
        G.USER_ID=USER_ID;

        SharedPreferences.Editor editor=PREF.edit();

        editor.putInt("userID",USER_ID);
        editor.commit();
    }


}
