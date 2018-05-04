package com.snownaul.study.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.snownaul.study.R;

public class StudySetMainActivity extends AppCompatActivity {

    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_set_main);

        helper.setEdgeMode(true).setParallaxMode(true).setParallaxRatio(3).setNeedBackgroundShadow(true).init(this);


    }

    @Override
    public void onBackPressed() {
        helper.finish();
    }
}
