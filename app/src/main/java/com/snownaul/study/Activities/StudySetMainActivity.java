package com.snownaul.study.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.snownaul.study.R;

public class StudySetMainActivity extends AppCompatActivity {

    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();

    TextView tvTitle,tvInfo;
    ToggleButton tbFavor;
    TextView tvFavorCnt, tvStudiedTotalCnt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_set_main);

        helper.setEdgeMode(true).setParallaxMode(true).setParallaxRatio(3).setNeedBackgroundShadow(true).init(this);

        tvTitle=findViewById(R.id.tv_title);
        tvInfo=findViewById(R.id.tv_info);
        tbFavor=findViewById(R.id.tb_favor);
        tvFavorCnt=findViewById(R.id.tv_favor_cnt);
        tvStudiedTotalCnt=findViewById(R.id.tv_studied_totalcnt);

    }

    @Override
    public void onBackPressed() {
        helper.finish();
    }

    public void clickStorage(View v){

    }

    public void clickStudy(View v){

    }

    public void clickPlay(View v){

    }

    public void clickReport(View v){

    }
}
