package com.snownaul.study.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.study_classes.Question;

public class StudyTestActivity extends AppCompatActivity {

    AdView adView;

    Toolbar toolbar;

    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();

    RelativeLayout dialog;

    RadioButton rgQ01,rgQ02,rgT01,rgT02;
    RadioGroup rgQ,rgT;
    EditText etQ02,etT02;

    TextView tvQuestionCnt,tvTimeLimits;

    boolean isSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_test);

        adView= findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        helper.setEdgeMode(true).setParallaxMode(true).setParallaxRatio(3).setNeedBackgroundShadow(true).init(this);

        dialog=findViewById(R.id.dialog);
        tvQuestionCnt=findViewById(R.id.tv_question_cnt);
        tvTimeLimits=findViewById(R.id.tv_timelimits);

        rgQ=findViewById(R.id.rg_q);
        rgQ01=findViewById(R.id.rg_q01);
        rgQ02=findViewById(R.id.rg_q02);
        etQ02=findViewById(R.id.et_q02);
        rgQ.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rg_q01:
                        rgQ01.setTextColor(getResources().getColor(R.color.colorDeeperSkyBlue));
                        rgQ02.setTextColor(getResources().getColor(R.color.colorLightSkyBlue));
                        etQ02.setEnabled(false);
                        etQ02.setTextColor(getResources().getColor(R.color.colorLightSkyBlue));
                        G.setTestRgq(0);
                        break;
                    case R.id.rg_q02:
                        rgQ02.setTextColor(getResources().getColor(R.color.colorDeeperSkyBlue));
                        rgQ01.setTextColor(getResources().getColor(R.color.colorLightSkyBlue));
                        etQ02.setEnabled(true);
                        etQ02.setTextColor(getResources().getColor(R.color.colorDeeperSkyBlue));
                        G.setTestRgq(1);
                        break;
                }
            }
        });

        etQ02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s==null||s.length()==0) return;
                G.setTestRgqDefault(Integer.parseInt(s.toString()));
            }
        });

        if(G.TEST_RGQ_DEFAULT!=-99)
            etQ02.setText(G.TEST_RGQ_DEFAULT+"");

        switch (G.TEST_RGQ){
            case 0:
                rgQ01.setChecked(true);
                break;
            case 1:
                rgQ02.setChecked(true);
                break;

        }

        rgT=findViewById(R.id.rg_t);
        rgT01=findViewById(R.id.rg_t01);
        rgT02=findViewById(R.id.rg_t02);
        etT02=findViewById(R.id.et_t02);
        rgT.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rg_t01:
                        rgT01.setTextColor(getResources().getColor(R.color.colorDeeperSkyBlue));
                        rgT02.setTextColor(getResources().getColor(R.color.colorLightSkyBlue));
                        etT02.setEnabled(false);
                        etT02.setTextColor(getResources().getColor(R.color.colorLightSkyBlue));
                        G.setTestRgt(0);
                        break;
                    case R.id.rg_t02:
                        rgT02.setTextColor(getResources().getColor(R.color.colorDeeperSkyBlue));
                        rgT01.setTextColor(getResources().getColor(R.color.colorLightSkyBlue));
                        etT02.setEnabled(true);
                        etT02.setTextColor(getResources().getColor(R.color.colorDeeperSkyBlue));
                        G.setTestRgt(1);
                        break;
                }
            }
        });

        etT02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s==null||s.length()==0) return;
                G.setTestRgtDefault(Integer.parseInt(s.toString()));
            }
        });

        if(G.TEST_RGT_DEFAULT!=-99)
            etT02.setText(G.TEST_RGT_DEFAULT+"");

        switch (G.TEST_RGT){
            case 0:
                rgT01.setChecked(true);
                break;
            case 1:
                rgT02.setChecked(true);
                break;
        }


        setView();


    }

    public void setView(){
        switch (G.TEST_RGQ){
            case 0:
                tvQuestionCnt.setText(getResources().getString(R.string.testsetting_studyall));
                break;
            case 1:
                if(G.TEST_RGQ_DEFAULT!=-99)
                    tvQuestionCnt.setText(G.TEST_RGQ_DEFAULT+"");
                break;
            default:

                break;
        }

        switch (G.TEST_RGT){
            case 0:
                tvTimeLimits.setText(getString(R.string.testsetting_nolimits));
                break;
            case 1:
                if(G.TEST_RGT_DEFAULT!=-99)
                    tvTimeLimits.setText(G.TEST_RGT_DEFAULT+" "+getString(R.string.testsetting_min));
                break;
            default:

                break;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.studytest_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if(isSetting)return true;
                finish();
                return true;

            case R.id.setting:
                dialog.setVisibility(View.VISIBLE);
                isSetting=true;
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(dialog.getVisibility()==View.VISIBLE){
            dialog.setVisibility(View.GONE);
            setView();
            return;
        }

        helper.finish();
    }


    public void clickBtn(View v){
        if(isSetting) return;
        Intent intent=new Intent(this,StudyTestingPageActivity.class);
        startActivity(intent);
    }

    public void clickClear(View v){
        dialog.setVisibility(View.GONE);
        isSetting=false;

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        setView();
    }
}
