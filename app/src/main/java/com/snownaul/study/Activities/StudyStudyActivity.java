package com.snownaul.study.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.snownaul.study.R;

public class StudyStudyActivity extends AppCompatActivity {

    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_study);

        helper.setEdgeMode(true).setParallaxMode(true).setParallaxRatio(3).setNeedBackgroundShadow(true).init(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                helper.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickStudy(View v) {
        Intent intent=new Intent(this,StudyStudyingPageActivity.class);
        startActivity(intent);

    }

    public void clickTest(View v){

    }
}
