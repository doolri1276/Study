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
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

public class StudyReportActivity extends AppCompatActivity {

    Toolbar toolbar;
    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();
    JellyRefreshLayout jelly;

    RecyclerView recyclerView;

    ArrayList<Test> tests;
    StudyReportTestsAdapter studyReportTestsAdapter;

    //차트 관련 것들
    private LineChartView lcTest;
    private int numberOfPoints=12;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    private boolean hasGradientToTransparent = false;

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
        lcTest=findViewById(R.id.lc_test);

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
                    Log.i("MyTag","REPORT : "+ Arrays.toString(tt));

                    if(tt[0]==null||tt[0].length()==0) return;
                    tests.add(new Test(Integer.parseInt(tt[0]),Integer.parseInt(tt[1]),Integer.parseInt(tt[2]),Integer.parseInt(tt[3]),Integer.parseInt(tt[4]),G.convertUTCToLocalTime(tt[5])));
                    Log.i("MyTag","REPORT : 추가했다. "+tests.size());
                }

                studyReportTestsAdapter.notifyDataSetChanged();

                generateLC1Data();

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

    private void generateLC1Data(){
        List<PointValue> values=new ArrayList<>();
        for(int i=0;i<tests.size();i++){
            values.add(new PointValue(i,tests.get(i).getScore()));

        }

        Line line=new Line(values).setColor(getResources().getColor(R.color.colorDeepSkyBlue)).setCubic(isCubic);
        line.setShape(ValueShape.DIAMOND);
        line.setHasLabels(hasLabels);
        line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(hasLines);
        line.setHasPoints(hasPoints);


        List<Line> lines=new ArrayList<>();
        lines.add(line);
        LineChartData data=new LineChartData();

        if(hasAxes){
            Axis axisX=new Axis();
            Axis axisY=new Axis().setHasLines(true);



            if(hasAxesNames){
                axisX.setName("횟수");
                axisY.setName("점수");
            }

            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        }else{
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }


        data.setLines(lines);


        lcTest.setLineChartData(data);
        lcTest.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);


    }
}
