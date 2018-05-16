package com.snownaul.study.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.StoAnswersAdapter;
import com.snownaul.study.adapters.StudyAnswersAdapter;
import com.snownaul.study.controls.StudyingManager;
import com.snownaul.study.study_classes.Question;

public class StudyStudyingPageActivity extends AppCompatActivity {

    AdView adView;

    Toolbar toolbar;

    RelativeLayout modeOpening, modeStudying;

    StudyingManager manager;
    int studyingMode;

    TextView tvRorW;
    Button btnTest;

    View[] percentage=new View[30];
    LinearLayout type2;
    ToggleButton tbRorW;

    TextView tvQuestion;
    RecyclerView recyclerView;
    StudyAnswersAdapter studyAnswersAdapter;

    long startTime;
    long endTime;
    boolean correction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_studying_page);

        adView= findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Studying Page");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modeOpening=findViewById(R.id.mode_opening);
        modeStudying=findViewById(R.id.mode_studying);

        tvRorW=findViewById(R.id.tv_rorw);
        btnTest=findViewById(R.id.btn_test);

        for(int i=0;i<percentage.length;i++){
            percentage[i]=findViewById(R.id.percentage01+i);
        }

        type2=findViewById(R.id.type2);
        tbRorW=findViewById(R.id.tb_rorw);
        tvQuestion=findViewById(R.id.tv_question);
        recyclerView=findViewById(R.id.recycler);



        manager=new StudyingManager();
        studyingMode= StudyingManager.MODE_OPENING;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void clickBtn(View v){

        switch (studyingMode){
            case StudyingManager.MODE_OPENING:
                modeOpening.setVisibility(View.GONE);
                modeStudying.setVisibility(View.VISIBLE);


                manager.startStudying();

                setStudyPage();

                break;
            case StudyingManager.MODE_STUDYING:
                setAnswerPage();
                break;
            case StudyingManager.MODE_ANSWER:
                setStudyPage();
                break;
        }


    }
    Question t;
    public void setStudyPage(){
        studyingMode=StudyingManager.MODE_STUDYING;

        tvRorW.setVisibility(View.GONE);
        btnTest.setBackgroundResource(R.drawable.testbtn_n);
        for(int i=0;i<percentage.length;i++){
            percentage[i].setBackgroundColor(getResources().getColor(R.color.colorDeepSkyBlue));
        }




        t=manager.getQuestion();
        tvQuestion.setText(t.getQuestion());

        if(t.getQuestionType()==Question.TYPE_RIGHTORWRONG){
            type2.setVisibility(View.VISIBLE);
            tbRorW.setChecked(t.isRightOrWrong());


        }else{
            type2.setVisibility(View.GONE);

        }

        for(int i=0;i<percentage.length;i++){

            if(i<manager.getNumOfQuestionsPerStudy()){
                if(i<manager.getPercentageI()){
                    percentage[i].setVisibility(View.VISIBLE);
                    //Log.i("MyTag","Percentage : "+i+" VISIBLE");
                }else{
                    percentage[i].setVisibility(View.INVISIBLE);
                    //Log.i("MyTag","Percentage : "+i+" INVISIBLE");
                }
            }else{
                percentage[i].setVisibility(View.GONE);
                //Log.i("MyTag","Percentage : "+i+" GONE");
            }


        }

        studyAnswersAdapter= new StudyAnswersAdapter(this,t.getAnswers(),t.getQuestionType(),studyingMode);
        recyclerView.setAdapter(studyAnswersAdapter);

        startTime=System.currentTimeMillis()/1000;

    }

    public void setAnswerPage(){
        studyingMode=StudyingManager.MODE_ANSWER;

        studyAnswersAdapter.setStudyingMode(studyingMode);



        //맞췄는지 틀렸는지 확인..
        correction=manager.checkCorrection(t);

        G.currentStudySet.setTriedCnt(G.currentStudySet.getTriedCnt()+1);

        if(correction){
            for(int i=0;i<percentage.length;i++){
                percentage[i].setBackgroundColor(getResources().getColor(R.color.colorGreen));
            }
            btnTest.setBackgroundResource(R.drawable.testbtn_r);
            tvRorW.setText(getString(R.string.study_tvrorw_right));
            tvRorW.setTextColor(getResources().getColor(R.color.colorGreen));
            G.currentStudySet.setKeptCorrectionCnt(G.currentStudySet.getKeptCorrectionCnt()+1);

        }else{
            for(int i=0;i<percentage.length;i++){
                percentage[i].setBackgroundColor(getResources().getColor(R.color.colorRed));
            }
            btnTest.setBackgroundResource(R.drawable.testbtn_w);
            tvRorW.setText(getString(R.string.study_tvrorw_wrong));
            tvRorW.setTextColor(getResources().getColor(R.color.colorRed));
            G.currentStudySet.setKeptCorrectionCnt(0);
        }

        tvRorW.setVisibility(View.VISIBLE);

        for(int i=0;i<percentage.length;i++){

            if(i<manager.getNumOfQuestionsPerStudy()){
                if(i<manager.getPercentageI()){
                    percentage[i].setVisibility(View.VISIBLE);
                   // Log.i("MyTag","Percentage : "+i+" VISIBLE");
                }else{
                    percentage[i].setVisibility(View.INVISIBLE);
                    //Log.i("MyTag","Percentage : "+i+" INVISIBLE");
                }
            }else{
                percentage[i].setVisibility(View.GONE);
                //Log.i("MyTag","Percentage : "+i+" GONE");
            }


        }



        studyAnswersAdapter.notifyDataSetChanged();

        endTime=System.currentTimeMillis()/1000;




        sendQuestionResultToServer();

        //percentage.setBackgroundColor(getResources().getColor(R.color.color));
    }

    public void sendQuestionResultToServer(){
        int time=(int)(endTime-startTime);

        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Study/submitQuestionResult.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        multiPartRequest.addStringParam("studySetID",G.currentStudySet.getStudySetId()+"");
        multiPartRequest.addStringParam("questionID",t.getQuestionID()+"");
        multiPartRequest.addStringParam("isSolved",(correction?1:0)+"");
        multiPartRequest.addStringParam("timeLength",time+"");

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);

    }




}
