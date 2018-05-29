package com.snownaul.study.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.StudyReportTestsAdapter;
import com.snownaul.study.report_classes.Test;

import java.util.ArrayList;

import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

public class StudyReportActivity extends AppCompatActivity {

    Toolbar toolbar;
    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();
    JellyRefreshLayout jelly;

    RecyclerView recyclerView;

    ArrayList<Test> tests;
    StudyReportTestsAdapter studyReportTestsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_report);

        jelly=findViewById(R.id.jelly);
        jelly.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MyTag","1 REFRESH!!!!!!!!");

                        loadTestResults();
                        jelly.setRefreshing(false);
                    }
                },2000);
            }
        });

        View loadingView= LayoutInflater.from(this).inflate(R.layout.jelly_loading,null);
        jelly.setLoadingView(loadingView);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        helper.setEdgeMode(true).setParallaxMode(true).setParallaxRatio(3).setNeedBackgroundShadow(true).init(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.report_testscores));

        recyclerView=findViewById(R.id.recycler);

        tests=new ArrayList<>();

        loadTestResults();
        studyReportTestsAdapter=new StudyReportTestsAdapter(this,tests);
        recyclerView.setAdapter(studyReportTestsAdapter);

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

    public void loadTestResults(){
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Report/loadAllTests.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("MyTag","loadAllTests : "+response);
                tests.clear();
                if(response==null||response.length()==0) return;
                String[] testsList=response.split("&T&");

                for(int i=0;i<testsList.length;i++){
                    String[] tt=testsList[i].split("&");

                    if(tt[0]==null||tt[0].length()==0) return;
                    tests.add(new Test(Integer.parseInt(tt[0]),Integer.parseInt(tt[1]),Integer.parseInt(tt[2]),Integer.parseInt(tt[3]),Integer.parseInt(tt[4]),G.convertUTCToLocalTime(tt[5])));

                }

                studyReportTestsAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyTag","loadAllTests : "+error);
            }
        });

        multiPartRequest.addStringParam("studySetID",G.currentStudySet.getStudySetId()+"");

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);
    }
}
