package com.snownaul.study.Activities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.StoQuestionsAdapter;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;

import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

public class StudyStorageActivity extends AppCompatActivity {

    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();
    JellyRefreshLayout jelly;

    RecyclerView recyclerView;
    StoQuestionsAdapter stoQuestionsAdapter;

    EditText etTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_storage);

        jelly=findViewById(R.id.jelly);
        jelly.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MyTag","1 REFRESH!!!!!!!!");
                        loadQuestions();
                        jelly.setRefreshing(false);
                    }
                },2000);
            }
        });

        View loadingView= LayoutInflater.from(this).inflate(R.layout.jelly_loading,null);
        jelly.setLoadingView(loadingView);


        helper.setEdgeMode(true).setParallaxMode(true).setParallaxRatio(3).setNeedBackgroundShadow(true).init(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.recycler);
        stoQuestionsAdapter=new StoQuestionsAdapter(this);
        recyclerView.setAdapter(stoQuestionsAdapter);

        etTitle=findViewById(R.id.et_title);

        G.hideKeyboard(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        G.hideKeyboard(this);
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
                },2000);
                return true;

        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        helper.finish();
    }

    public void loadQuestions(){
        //TODO: 문제들 받아오기...
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Set/loadAllQuestions.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("MyTag","STORAGGGGGG : "+response);

                String[] questionInfos=response.split("&Q&");
                String[] setInfos=questionInfos[0].split("&");
                G.currentStudySet.getSgSet().setTitle(setInfos[0]);
                G.currentStudySet.getSgSet().setInfo(setInfos[1]);
                G.currentStudySet.setEditable(Boolean.parseBoolean(setInfos[2]));
                G.currentStudySet.getSgSet().setLikeCnt(Integer.parseInt(setInfos[3]));
                G.currentStudySet.setLiked(Boolean.parseBoolean(setInfos[4]));
                G.currentStudySet.setTriedCnt(Integer.parseInt(setInfos[5]));
                G.currentStudySet.setRecentDate(G.convertUTCToLocalTime(setInfos[6]));
                G.currentStudySet.setKeptCorrectionCnt(Integer.parseInt(setInfos[7]));

                G.currentStudySet.getSgSet().getQuestions().clear();

                for(int i=1;i<questionInfos.length;i++){
                    String[] answerInfos=questionInfos[i].split("&A&");
                    String[] questionDetailInfos=answerInfos[0].split("&");

                    Question q=new Question(Integer.parseInt(questionDetailInfos[1]));
                    q.setQuestionID(Integer.parseInt(questionDetailInfos[0]));
                    q.setQuestion(questionDetailInfos[2]);
                    q.setExplanation(questionDetailInfos[3]);
                    q.setRightOrWrong(questionDetailInfos[4].equals("1")?true:false);

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
                stoQuestionsAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        multiPartRequest.addStringParam("studySetID",G.currentStudySet.getStudySetId()+"");
        multiPartRequest.addStringParam("userID",G.getUserId()+"");
        multiPartRequest.addStringParam("sgSetID",G.currentStudySet.getSgSetID()+"");

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);

    }
}
