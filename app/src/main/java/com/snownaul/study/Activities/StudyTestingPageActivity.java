package com.snownaul.study.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.StudyAnswersAdapter;
import com.snownaul.study.adapters.TestAnswersAdapter;
import com.snownaul.study.controls.StudyingManager;
import com.snownaul.study.study_classes.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;

public class StudyTestingPageActivity extends AppCompatActivity {

    AdView adView;

    Toolbar toolbar;

    View[] percentage;
    TextView[] qnum;

    int questionCnt;
    int timeLimit;

    //화면관련
    TextView tvTimeLimits;
    LinearLayout bar, qlist;

    LinearLayout type2;
    ToggleButton tbRorW;
    TextView tvQuestionNo;

    TextView tvQuestion;
    RecyclerView recyclerView;
    TestAnswersAdapter testAnswersAdapter;

    private ArrayList<Question> studyingQuestions;
    private ArrayList<Question> questionsParts;



    //시험진행 관련
    int studyingMode;
    Timer timer;
    int position;
    Question t;
    long startTime;
    long endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_testing_page);

        adView= findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        tvTimeLimits=findViewById(R.id.tv_timelimits);
        bar=findViewById(R.id.bar);
        qlist=findViewById(R.id.qlist);

        type2=findViewById(R.id.type2);
        tbRorW=findViewById(R.id.tb_rorw);
        tvQuestionNo=findViewById(R.id.tv_question_no);
        tvQuestion=findViewById(R.id.tv_question);
        recyclerView=findViewById(R.id.recycler);


        prepareTesting();
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
        setBar();
        switch (studyingMode){
            case StudyingManager.MODE_STUDYING:
                position++;
                setStudyPage();
                break;

        }

    }

    public void clickBar(View v){
        qlist.setVisibility(View.VISIBLE);
        setBar();
    }

    public void clickClear(View v){
        qlist.setVisibility(GONE);

    }

    public void prepareTesting(){

        switch (G.TEST_RGQ){
            case 0:
                questionCnt=G.currentStudySet.getSgSet().getQuestions().size();
                break;
            case 1:
                int num=G.TEST_RGQ_DEFAULT;

                if(num>G.currentStudySet.getSgSet().getQuestions().size())
                    questionCnt=G.currentStudySet.getSgSet().getQuestions().size();
                else
                    questionCnt=G.TEST_RGQ_DEFAULT;
                break;
        }

        switch (G.TEST_RGT){
            case 0:
                timeLimit=-99;
                tvTimeLimits.setVisibility(GONE);
                break;

            case 1:
                timeLimit=G.TEST_RGT_DEFAULT*60;

                int tmp=timeLimit;
                Log.i("MyTag","time Limit : "+tmp);
                StringBuffer time=new StringBuffer();
                if(tmp>3600){
                    time.append(tmp/3600+" : ");
                    tmp=tmp-((int)(tmp/3600))*3600;
                    Log.i("MyTag","3600이상이라 나뉜다던디.. "+tmp/3600);
                }

                if((timeLimit/60)<10){
                    time.append("0"+tmp/60+" : ");
                    Log.i("MyTag","숫자 집어넌다지 0+"+tmp/60);

                }else{
                    time.append(tmp/60+" : ");
                    Log.i("MyTag","그냥 넣눈다.... "+tmp/60);

                }

                time.append("00");

                tvTimeLimits.setText(time.toString());

                break;
        }


        studyingQuestions=G.currentStudySet.getSgSet().getQuestions();
        Collections.shuffle(studyingQuestions);

        questionsParts=new ArrayList<>();
        percentage=new View[questionCnt];
        qnum=new TextView[questionCnt];

        for(int i=0;i<questionCnt;i++){
            questionsParts.add(studyingQuestions.get(i));
            Collections.shuffle(questionsParts.get(i).getAnswers());

            for(int j=0;j<questionsParts.get(i).getAnswers().size();j++){
                if(questionsParts.get(i).getAnswers().get(j).isTestChecked()){
                    questionsParts.get(i).getAnswers().get(j).setTestChecked(false);
                }
            }
            questionsParts.get(i).setHasTestAnswer(false);

            percentage[i]=findViewById(R.id.percentage01+i);
            percentage[i].setVisibility(View.INVISIBLE);
            qnum[i]=findViewById(R.id.qnum01+i);
            qnum[i].setVisibility(View.VISIBLE);
            qnum[i].setText(i+1+"");

            qnum[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView t=(TextView)v;
                    position=Integer.parseInt(t.getText().toString())-1;
                    setStudyPage();
                    qlist.setVisibility(GONE);
                }
            });
        }

        timer=new Timer();
        timer.schedule(task,1000,1000);

        position=0;
        setStudyPage();

    }

    public void setStudyPage(){
        studyingMode= StudyingManager.MODE_STUDYING;

        if(position>=questionCnt){
            position=questionCnt-1;
            return;
        }

        tvQuestionNo.setText(position+1+"");

        t=questionsParts.get(position);
        tvQuestion.setText(t.getQuestion());

        if(t.getQuestionType()==Question.TYPE_RIGHTORWRONG){
            type2.setVisibility(View.VISIBLE);
            tbRorW.setChecked(t.isRightOrWrong());
        }else{
            type2.setVisibility(View.GONE);
        }

        testAnswersAdapter= new TestAnswersAdapter(this,t,t.getQuestionType(),studyingMode);
        recyclerView.setAdapter(testAnswersAdapter);

        startTime=System.currentTimeMillis()/1000;


    }

    public void setBar(){
        for(int i=0;i<questionCnt;i++){
            if(questionsParts.get(i).isHasTestAnswer()){
                qnum[i].setBackgroundColor(getResources().getColor(R.color.colorDeepSkyBlue));
                percentage[i].setVisibility(View.VISIBLE);
            }else{
                qnum[i].setBackgroundColor(getResources().getColor(R.color.colorWhite));
                percentage[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    TimerTask task=new TimerTask() {
        @Override
        public void run() {

            timeLimit--;

            int tmp=timeLimit;
            Log.i("MyTag","time Limit : "+tmp);
            final StringBuffer time=new StringBuffer();
            if(tmp>3600){
                time.append(tmp/3600+" : ");
                tmp=tmp-((int)(tmp/3600))*3600;
                Log.i("MyTag","3600이상이라 나뉜다던디.. "+tmp/3600);
            }

            if((timeLimit/60)<10){
                time.append("0"+tmp/60+" : ");
                Log.i("MyTag","숫자 집어넌다지 0+"+tmp/60);

            }else{
                time.append(tmp/60+" : ");
                Log.i("MyTag","그냥 넣눈다.... "+tmp/60);

            }

            if(tmp%60<10){
                time.append("0"+tmp%60);
            }else{
                time.append(tmp%60);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvTimeLimits.setText(time.toString());


                }
            });

        }
    };
}
