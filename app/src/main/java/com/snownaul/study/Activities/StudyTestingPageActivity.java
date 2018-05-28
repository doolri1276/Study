package com.snownaul.study.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.StudyAnswersAdapter;
import com.snownaul.study.adapters.TestAnswersAdapter;
import com.snownaul.study.controls.StudyingManager;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Random;
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

    LinearLayout type2,type3;
    ToggleButton tbRorW;
    TextView tvQuestionNo;
    EditText etTestOneAnswer;
    Button btnTest;
    TextView tvRorW;

    TextView tvQuestion, tvScore, tvExplanation;
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
    int correctionCnt;
    int totalTimeLength;

    boolean isSetting;


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
        type3=findViewById(R.id.type3);
        tbRorW=findViewById(R.id.tb_rorw);
        tvQuestionNo=findViewById(R.id.tv_question_no);
        tvQuestion=findViewById(R.id.tv_question);
        recyclerView=findViewById(R.id.recycler);
        btnTest=findViewById(R.id.btn_test);
        etTestOneAnswer=findViewById(R.id.et_testoneanswer);
        tvRorW=findViewById(R.id.tv_rorw);
        tvScore=findViewById(R.id.tv_score);
        tvExplanation=findViewById(R.id.tv_explanation);


        prepareTesting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(isSetting)return true;

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.submit:
                if(studyingMode==StudyingManager.MODE_STUDYING)
                    showAlertDialog(R.string.test_submit);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showAlertDialog(int stringResId){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(stringResId);
        builder.setPositiveButton(getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitTest();
            }
        });

        builder.setNegativeButton(getString(R.string.dialog_negative),null);
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void submitTest(){
        //TODO: 테스트 제출하고 체점하고 그런기능 있어야..
        studyingMode=StudyingManager.MODE_ANSWER;
        testAnswersAdapter.setStudyingMode(studyingMode);

        if(timer!=null)
            timer.cancel();

        //각각의 정답 체크..
        checkAnswers();

        submitTestResult();

        position=0;
        setAnswerPage();

    }

    public void checkAnswers(){

        for(int i=0;i<questionsParts.size();i++){
            Question q=questionsParts.get(i);
            ArrayList<Answer> ta=q.getAnswers();

            boolean correct=true;
            for(int j=0;j<ta.size();j++){
                Answer t=ta.get(j);
                if(t.isCorrect()!=t.isTestChecked()){
                    correct=false;
                    break;
                }
            }
            q.setTestCorrection(correct);
            q.setTriedCnt(q.getTriedCnt()+1);
            if(correct){
                q.setSolvedCnt(q.getSolvedCnt()+1);
                qnum[i].setBackgroundColor(getResources().getColor(R.color.colorGreen));
                percentage[i].setBackgroundColor(getResources().getColor(R.color.colorGreen));
                correctionCnt++;
            }else{
                percentage[i].setVisibility(View.VISIBLE);
                qnum[i].setBackgroundColor(getResources().getColor(R.color.colorRed));
                percentage[i].setBackgroundColor(getResources().getColor(R.color.colorRed));
            }
            totalTimeLength+=q.getTestTimeLength();

            sendQuestionResultToServer(q);



        }

        return;
    }

    public void submitTestResult(){

        tvTimeLimits.setVisibility(GONE);
        tvScore.setVisibility(View.VISIBLE);

        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Study/submitTestResult.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MyTag","submit TEST Result MSG : "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyTag","submitTest Result ERROR : "+error.getMessage());

            }
        });

        multiPartRequest.addStringParam("studySetID",G.currentStudySet.getStudySetId()+"");
        multiPartRequest.addStringParam("questionCnt",questionCnt+"");
        multiPartRequest.addStringParam("correctionCnt",correctionCnt+"");
        multiPartRequest.addStringParam("timeLength",totalTimeLength+"");

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);

        int score=(correctionCnt*100/questionCnt);
        tvScore.setText(score+"");
        tvExplanation.setVisibility(View.VISIBLE);

    }

    public void sendQuestionResultToServer(Question q){
        int time=(int)(endTime-startTime);

        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Study/submitQuestionResult.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MyTag","submit Question Result MSG : "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("MyTag","submit Question Result ERROR : "+error.getMessage());

            }
        });

        multiPartRequest.addStringParam("studySetID",G.currentStudySet.getStudySetId()+"");
        multiPartRequest.addStringParam("questionID",q.getQuestionID()+"");
        multiPartRequest.addStringParam("isSolved",(q.isTestCorrection()?1:0)+"");
        multiPartRequest.addStringParam("timeLength",q.getTestTimeLength()+"");

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);

    }


    public void clickBtn(View v){

        Log.i("MyTag","clickBtnㅎㅐㅆ는데 isSetting 상태 : "+isSetting);

        if(isSetting)return;

        G.hideKeyboard(StudyTestingPageActivity.this);

        switch (studyingMode){
            case StudyingManager.MODE_STUDYING:
                setBar();
                position++;
                setStudyPage();
                break;
            case StudyingManager.MODE_ANSWER:
                position++;
                setAnswerPage();
                break;

        }

    }

    public void clickBar(View v){
        isSetting=true;
        qlist.setVisibility(View.VISIBLE);
        setBar();
    }

    public void clickClear(View v){
        isSetting=false;
        qlist.setVisibility(GONE);

    }

    public void prepareTesting(){

        etTestOneAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isSetting)return;

                if(t.getQuestionType()!=Question.TYPE_ONEANSWER) return;

                t.setHasTestAnswer(false);
                if(s.toString()==null) {
                    return;
                }else if(s.toString().length()>0) t.setHasTestAnswer(true);


                t.setTestOneAnswer(s.toString());

            }
        });

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
            questionsParts.get(i).setTestCorrection(false);
            questionsParts.get(i).setTestTimeLength(0);

            switch (questionsParts.get(i).getQuestionType()){
                case Question.TYPE_RIGHTORWRONG:
                    if(new Random().nextInt(10)<5){
                        questionsParts.get(i).setRightOrWrong(!questionsParts.get(i).isRightOrWrong());

                        ArrayList<Answer> ta=questionsParts.get(i).getAnswers();
                        for(int j=0;j<ta.size();j++){
                            ta.get(j).setCorrect(!ta.get(j).isCorrect());
                        }
                    }
                    break;
                case Question.TYPE_ONEANSWER:
                    questionsParts.get(i).setTestOneAnswer("");
                    break;
            }

            percentage[i]=findViewById(R.id.percentage01+i);
            percentage[i].setVisibility(View.INVISIBLE);
            qnum[i]=findViewById(R.id.qnum01+i);
            qnum[i].setVisibility(View.VISIBLE);
            qnum[i].setText(i+1+"");

            qnum[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSetting=false;
                    TextView t=(TextView)v;
                    position=Integer.parseInt(t.getText().toString())-1;
                    if(studyingMode==StudyingManager.MODE_STUDYING)
                        setStudyPage();
                    else if(studyingMode==StudyingManager.MODE_ANSWER)
                        setAnswerPage();
                    qlist.setVisibility(GONE);
                }
            });


        }

        if(timeLimit!=-99){
            timer=new Timer();
            timer.schedule(task,1000,1000);
        }


        position=0;
        setStudyPage();

    }

    public void setStudyPage(){
        studyingMode= StudyingManager.MODE_STUDYING;

        isSetting=true;

        if(position>=questionCnt){
            position=questionCnt-1;
            showAlertDialog(R.string.test_submit);
            return;
        }

        endTime=System.currentTimeMillis()/1000;
        Log.i("MyTag","시간 적립 : "+(endTime-startTime));
        if(t!=null)
            t.setTestTimeLength((int) (t.getTestTimeLength()+endTime-startTime));

        tvQuestionNo.setText(position+1+"");

        t=questionsParts.get(position);
        tvQuestion.setText(t.getQuestion());

        switch (t.getQuestionType()){
            case Question.TYPE_RIGHTORWRONG:
                type3.setVisibility(GONE);
                type2.setVisibility(View.VISIBLE);
                tbRorW.setChecked(t.isRightOrWrong());
                recyclerView.setVisibility(View.VISIBLE);
                break;
            case Question.TYPE_ONEANSWER:
                type2.setVisibility(GONE);
                type3.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                etTestOneAnswer.setText(t.getTestOneAnswer());
                break;
            default:
                type3.setVisibility(GONE);
                type2.setVisibility(GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;

        }

        if(t.getQuestionType()==Question.TYPE_RIGHTORWRONG){
            type2.setVisibility(View.VISIBLE);
            tbRorW.setChecked(t.isRightOrWrong());
        }else{
            type2.setVisibility(View.GONE);
        }

        testAnswersAdapter= new TestAnswersAdapter(this,t,t.getQuestionType(),studyingMode);
        recyclerView.setAdapter(testAnswersAdapter);

        startTime=System.currentTimeMillis()/1000;

        isSetting=false;

    }

    public void setAnswerPage(){
        studyingMode=StudyingManager.MODE_ANSWER;



        if(position>=questionCnt){
            position=questionCnt-1;
        }

        tvQuestionNo.setText(position+1+"");
        tvExplanation.setText(t.getExplanation());

        t=questionsParts.get(position);

        //맞았을때 틀렸을때의 설정.... 들어간다!
        if(t.isTestCorrection()){
            btnTest.setBackgroundResource(R.drawable.testbtn_r);
            tvRorW.setText(getString(R.string.study_tvrorw_right));
            tvRorW.setTextColor(getResources().getColor(R.color.colorGreen));
        }else{
            btnTest.setBackgroundResource(R.drawable.testbtn_w);
            tvRorW.setText(getString(R.string.study_tvrorw_wrong));
            tvRorW.setTextColor(getResources().getColor(R.color.colorRed));
        }


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

            //TODO:0초가 되었으면 타이머 종료하구 바로 제출기능
            if(timeLimit<0){

                if(task != null){
                    task.cancel(); //타이머task를 timer 큐에서 지워버린다
                    task=null;
                }
                timer.cancel(); //스케쥴task과 타이머를 취소한다.
                timer.purge(); //task큐의 모든 task를 제거한다.
                timer=null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        submitTest();
                    }
                });

            }

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

    @Override
    protected void onStop() {
        super.onStop();
        if(task != null){
            task.cancel(); //타이머task를 timer 큐에서 지워버린다
            task=null;
        }

        if(timer!=null){
            timer.cancel(); //스케쥴task과 타이머를 취소한다.
            timer.purge(); //task큐의 모든 task를 제거한다.
            timer=null;
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //timer.cancel();

    }
}
