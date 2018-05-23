package com.snownaul.study.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.StoQuestionsAdapter;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;
import com.snownaul.study.study_classes.StudySet;

import java.util.Arrays;

import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

public class StudySetMainActivity extends AppCompatActivity {

    AdView adView;

    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();

    TextView tvTitle,tvInfo;
    ToggleButton tbFavor;
    TextView tvFavorCnt, tvStudiedTotalCnt, tvQuestionCnt;
    TextView tvSolvedCnt, tvTimeLength;

    JellyRefreshLayout jelly;

    boolean isSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_set_main);

        adView= findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        jelly=findViewById(R.id.jelly);
        jelly.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MyTag","1 REFRESH!!!!!!!!");
                        //G.loadCurrentSet(StudySetMainActivity.this);
                        loadCurrentSet();
                        //setView();
                        jelly.setRefreshing(false);
                    }
                },1500);
            }
        });

        View loadingView= LayoutInflater.from(this).inflate(R.layout.jelly_loading,null);
        jelly.setLoadingView(loadingView);


        helper.setEdgeMode(true).setParallaxMode(true).setParallaxRatio(3).setNeedBackgroundShadow(true).init(this);

        tvTitle=findViewById(R.id.tv_title);
        tvInfo=findViewById(R.id.tv_info);
        tbFavor=findViewById(R.id.tb_favor);
        tvFavorCnt=findViewById(R.id.tv_favor_cnt);
        tvStudiedTotalCnt=findViewById(R.id.tv_studied_totalcnt);
        tvQuestionCnt=findViewById(R.id.tv_question_cnt);
        tvSolvedCnt=findViewById(R.id.tv_solved_cnt);
        tvTimeLength=findViewById(R.id.tv_time_length);

        tbFavor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                final StudySet t=G.currentStudySet;
                if(t.isSetting()) return;
                String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Set/studySetFavorChanged.php";

                SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyTag","likeeeee checkkkkk : "+response);
                        t.setLiked(isChecked);
                        if(isChecked){
                            t.getSgSet().setLikeCnt(t.getSgSet().getLikeCnt()+1);
                        }else {
                            t.getSgSet().setLikeCnt(t.getSgSet().getLikeCnt()-1);
                        }

                        tvFavorCnt.setText(t.getSgSet().getLikeCnt()+"");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tbFavor.setChecked(!isChecked);

                    }
                });

                if(isChecked){
                    multiPartRequest.addStringParam("sgSetLike",isChecked+"");
                }else{
                    multiPartRequest.addStringParam("sgSetLike","");
                }

                //multiPartRequest.addStringParam("sgSetLike",isChecked+"");
                multiPartRequest.addStringParam("userID",G.getUserId()+"");
                multiPartRequest.addStringParam("sgSetID",t.getSgSet().getSgSetID()+"");

                RequestQueue requestQueue= Volley.newRequestQueue(StudySetMainActivity.this);
                requestQueue.add(multiPartRequest);
            }
        });
        setView();

        //G.loadCurrentSet(this);
        loadCurrentSet();
        tvStudiedTotalCnt.setText(G.currentStudySet.getTriedCnt()+"");



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void loadCurrentSet(){
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

                    Log.i("MyTag","Check check : "+ Arrays.toString(questionDetailInfos));
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

                setView();


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

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        Log.i("MyTag","loadCurrentSet을 했다... studySetID : "+G.currentStudySet.getStudySetId()+" userID : "+G.getUserId()+" sgSetID : "+G.currentStudySet.getSgSetID());

        requestQueue.add(multiPartRequest);
        Log.i("MyTag","보냈습니다.");

    }



    @Override
    protected void onResume() {
        super.onResume();
        setView();
    }

    public void setView(){
        isSetting=true;
        tvTitle.setText(G.currentStudySet.getSgSet().getTitle());
        tvInfo.setText(G.currentStudySet.getSgSet().getInfo());
        tbFavor.setChecked(G.currentStudySet.isLiked());
        tvFavorCnt.setText(G.currentStudySet.getSgSet().getLikeCnt()+"");
        tvStudiedTotalCnt.setText(G.currentStudySet.getTriedCnt()+"");
        tvQuestionCnt.setText(G.currentStudySet.getSgSet().getQuestions().size()+"");
        tvSolvedCnt.setText(G.currentStudySet.getTriedCnt()+"");
        tvTimeLength.setText(G.currentStudySet.getTimeLength()/60+"");


        isSetting=false;
        Log.i("MyTag","setView()를 했다..");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                helper.finish();
                return true;
            case R.id.action_refresh:
                    jelly.post(new Runnable() {
                        @Override
                        public void run() {
                            jelly.setRefreshing(true);
                            Log.i("MyTag","2 REFRESH!!!!!!!!");
                        }
                    });
                    jelly.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            jelly.setRefreshing(false);
                            Log.i("MyTag","3 REFRESH!!!!!!!!");
                        }
                    },1500);
                    return true;

        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        helper.finish();
    }

    public void clickStorage(View v){
        SwipeBackActivityHelper.startSwipeActivity(this,new Intent(this,StudyStorageActivity.class),true,true,true);

    }

    public void clickStudy(View v){
        SwipeBackActivityHelper.startSwipeActivity(this,new Intent(this,StudyStudyActivity.class),true,true,true);
    }

    public void clickPlay(View v){
        Toast.makeText(this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();

    }

    public void clickReport(View v){
        Toast.makeText(this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
    }
}
