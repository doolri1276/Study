package com.snownaul.study.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.snownaul.study.R;

public class LoginActivity extends AppCompatActivity {

    LinearLayout logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        logo=findViewById(R.id.logo);

        Animation ani= AnimationUtils.loadAnimation(this,R.anim.appear_logo);
        logo.startAnimation(ani);

    }

    public void clickFB(View v){

    }

    public void clickKakao(View v){

    }

    public void clickGoogle(View v){

    }
}
