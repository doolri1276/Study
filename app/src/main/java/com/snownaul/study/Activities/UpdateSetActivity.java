package com.snownaul.study.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.NewQuestionsAdapter;
import com.snownaul.study.adapters.UpdateQuestionsAdapter;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;
import com.snownaul.study.study_classes.SgSet;

import java.util.ArrayList;

public class UpdateSetActivity extends AppCompatActivity {
    ScrollView scroll;

    EditText etTitle;
    EditText etInfo;

    LinearLayout addQuestion;

    RecyclerView recyclerView;

    //첫 셋팅 관련
    int defaultQuestionType= Question.TYPE_BASIC;
    TextView questionType;

    UpdateQuestionsAdapter updateQuestionsAdapter;

    String title,info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_set);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scroll=findViewById(R.id.scroll);
        etTitle=findViewById(R.id.et_title);
        etInfo=findViewById(R.id.et_info);

        addQuestion=findViewById(R.id.add_question);
        recyclerView=findViewById(R.id.recycler);

        questionType=findViewById(R.id.tv_question_type);

        G.updateSgSet=G.currentStudySet.getSgSet();
        G.deletedQuestions=new ArrayList<>();
        G.deletedAnswers=new ArrayList<>();

        etTitle.setText(G.updateSgSet.getTitle());
        Log.i("MyTag","aaaaa Titie : "+G.updateSgSet.getTitle());
        etInfo.setText(G.updateSgSet.getInfo());
        Log.i("MyTag","aaaaa Info : "+G.updateSgSet.getInfo());

        updateQuestionsAdapter=new UpdateQuestionsAdapter(this);
        recyclerView.setAdapter(updateQuestionsAdapter);

        addQuestion.setOnLongClickListener(new View.OnLongClickListener() {

            TextView t1,t2,t3,t4;


            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(UpdateSetActivity.this);

                LayoutInflater inf=getLayoutInflater();
                View view=inf.inflate(R.layout.dialog_selecttype,null);
                builder.setView(view);
                final AlertDialog dialog=builder.create();
                dialog.setCanceledOnTouchOutside(false);

                t1=view.findViewById(R.id.type1);
                t2=view.findViewById(R.id.type2);
                t3=view.findViewById(R.id.type3);
                t4=view.findViewById(R.id.type4);

                t1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDefaultQuestionType(Question.TYPE_BASIC);
                        dialog.dismiss();

                    }
                });

                t2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDefaultQuestionType(Question.TYPE_RIGHTORWRONG);
                        dialog.dismiss();
                    }
                });

                t3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDefaultQuestionType(Question.TYPE_ONEANSWER);
                        dialog.dismiss();
                    }
                });

                t4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDefaultQuestionType(Question.TYPE_ORDER);
                        dialog.dismiss();
                    }
                });

                dialog.show();





                return false;
            }
        });

        G.hideKeyboard(this);



    }



    public void setDefaultQuestionType(int defaultQuestionType) {
        this.defaultQuestionType = defaultQuestionType;
        questionType.setText(R.string.q_type_1+defaultQuestionType);
        clickAddQuestion(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf=getMenuInflater();
        inf.inflate(R.menu.addset_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case android.R.id.home:
                G.loadCurrentSet(this);
                finish();
                return true;

            case R.id.submit:

                submitSet();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        G.loadCurrentSet(this);
    }

    public void clickFab(View v){
        scroll.scrollTo(5,10);

    }

    public void clickAddQuestion(View v){
        G.updateSgSet.getQuestions().add(new Question(defaultQuestionType));

        Question t=G.updateSgSet.getQuestions().get(G.updateSgSet.getQuestions().size()-1);

        Log.i("MyTag",t.getQuestionID()+"번 Question ADDED");
        t.setUpdateState(Question.MODE_ADDED);
        t.getAnswers().get(0).setUpdateState(Answer.MODE_ADDED_BYQUESTION);
        updateQuestionsAdapter.notifyItemInserted(G.updateSgSet.getQuestions().size()-1);
        recyclerView.scrollToPosition(G.updateSgSet.getQuestions().size()-1);
    }

    public void submitSet(){
        //타이틀 체크
        title=etTitle.getText().toString();
        if(title==null||title.length()==0){
            showAlertDialog(R.string.addset_submit_fail01);
            return;
        }

        info=etInfo.getText().toString();

        G.updateSgSet.setTitle(title);
        G.updateSgSet.setInfo(info);
        //Settings내용 체크

        boolean answerExists;
        //문제들중에 빈칸 있는지 확인
        for(int i=0;i<G.updateSgSet.getQuestions().size();i++){
            Question t=G.updateSgSet.getQuestions().get(i);
            String q=t.getQuestion();
            if(q==null||q.length()==0){
                showAlertDialog(R.string.addset_submit_fail02,i);
                return;
            }
            answerExists=false;
            for(int j=0;j<t.getAnswers().size();j++){
                Answer a=t.getAnswers().get(j);

                if(a.getAnswer()==null||a.getAnswer().length()==0){
                    showAlertDialog(R.string.addset_submit_fail03,i,j);
                    return;
                }
                if(answerExists==false&&a.isCorrect())
                    answerExists=true;

            }
            //답이 없는 문제가 있는지 확인
            if(answerExists==false){
                showAlertDialog(R.string.addset_submit_fail04,i);
                return;
            }
        }

        updateSgSet();




    }

    public void showAlertDialog(int stringResId){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(stringResId);
        builder.setPositiveButton("OKAY",null);
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void showAlertDialog(int stringResId,int qPosition){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        String message="Number "+(qPosition+1)+" "+getString(stringResId);
        builder.setMessage(message);
        builder.setPositiveButton("OKAY",null);
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void showAlertDialog(int stringResId,int qPosition,int aPosition){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        String message="Number "+(qPosition+1)+" Question, Number "+(aPosition+1)+" "+ getString(stringResId);
        builder.setMessage(message);
        builder.setPositiveButton("OKAY",null);
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void updateSgSet(){

        StringBuffer buffer=new StringBuffer();
        StringBuffer updateBuffer=new StringBuffer();
        StringBuffer questionBuffer1=new StringBuffer();
        StringBuffer questionBuffer2=new StringBuffer();
        StringBuffer answerBuffer1=new StringBuffer();
        StringBuffer answerBuffer2=new StringBuffer();
        StringBuffer deleteBufferA=new StringBuffer();
        StringBuffer deleteBufferQ=new StringBuffer();


        //세트에 관
        for(int i=0;i<G.currentStudySet.getSgSet().getQuestions().size();i++){
            Question t=G.currentStudySet.getSgSet().getQuestions().get(i);

            buffer.append("Q\tID ["+t.getQuestionID()+"] state ["+t.getUpdateState()+"]\n");
            if(t.getUpdateState()==Question.MODE_UPDATED){
                if(questionBuffer1.toString().length()!=0) {
                    questionBuffer1.append("&Q&");
                    Log.i("MyTag","questionBuffer1\t"+"&Q&");
                }
                questionBuffer1.append(t.getQuestionID()+"[&]"+t.getQuestion()+"[&]"+t.getExplanation()+"[&]"+(t.isRightOrWrong()?1:0));
                Log.i("MyTag","questionBuffer1\t"+t.getQuestionID()+"[&]"+t.getQuestion()+"[&]"+t.getExplanation()+"[&]"+(t.isRightOrWrong()?1:0));
            }else if(t.getUpdateState()==Question.MODE_ADDED){
                if(questionBuffer2.toString().length()!=0) {
                    questionBuffer2.append("&Q&");
                    Log.i("MyTag","questionBuffer2\t"+"&Q&");
                }
                questionBuffer2.append(t.getQuestionType()+"[&]"+t.getQuestion()+"[&]"+t.getExplanation()+"[&]"+(t.isRightOrWrong()?1:0));
                Log.i("MyTag","questionBuffer2\t"+t.getQuestionType()+"[&]"+t.getQuestion()+"[&]"+t.getExplanation()+"[&]"+(t.isRightOrWrong()?1:0));
                Log.i("MyTag","t.getAnswers().size()결과 !! - "+t.getAnswers().size());
                for(int j=0;j<t.getAnswers().size();j++){
                    Answer ta=t.getAnswers().get(j);
                    Log.i("MyTag","questionBuffer2A\t"+"&A&"+ta.getAnswer()+"[&]"+(ta.isCorrect()?1:0)+"[&]"+ta.getSgOrder());
                    questionBuffer2.append("&A&"+ta.getAnswer()+"[&]"+(ta.isCorrect()?1:0)+"[&]"+ta.getSgOrder());
                }

            }

            for(int j=0;j<t.getAnswers().size();j++){
                Answer ta=t.getAnswers().get(j);

                buffer.append("\tA\tID ["+ta.getAnswerID()+"] state ["+ta.getUpdateState()+"]\n");

                if(ta.getUpdateState()==Answer.MODE_UPDATED){
                    if(answerBuffer1.toString().length()!=0) {
                        answerBuffer1.append("&A&");
                        //Log.i("MyTag","answerBuffer1\t"+"&A&");
                    }
                    answerBuffer1.append(ta.getAnswerID()+"[&]"+ta.getAnswer()+"[&]"+(ta.isCorrect()?1:0)+"[&]"+ta.getSgOrder());
                }else if(ta.getUpdateState()==Answer.MODE_ADDED){
                    if(answerBuffer2.toString().length()!=0) answerBuffer2.append("&A&");
                    answerBuffer2.append(ta.getQuestionID()+"[&]"+ta.getAnswer()+"[&]"+(ta.isCorrect()?1:0)+"[&]"+ta.getSgOrder());
                }

            }

        }


        buffer.append("\n\nDELETED QUESTIONS\n");

        for(int i=0;i<G.deletedQuestions.size();i++){
            Question t=G.deletedQuestions.get(i);
            buffer.append("Q\tID ["+t.getQuestionID()+"] state ["+t.getUpdateState()+"]\n");
            if(deleteBufferQ.toString().length()!=0) deleteBufferQ.append("&Q&");
            deleteBufferQ.append(t.getQuestionID());

        }


        buffer.append("\n\nDELETED ANSWERS\n");
        for(int i=0;i<G.deletedAnswers.size();i++){
            Answer ta=G.deletedAnswers.get(i);
            buffer.append("\tA\tID ["+ta.getAnswerID()+"] state ["+ta.getUpdateState()+"]\n");
            if(deleteBufferA.toString().length()!=0) deleteBufferA.append("&A&");
            deleteBufferA.append(ta.getAnswerID());

        }


        Log.i("MyTag",buffer.toString());

        updateBuffer.append(questionBuffer1.toString()+"&T&"+questionBuffer2.toString()+"&T&"+answerBuffer1.toString()+"&T&"+answerBuffer2.toString()+"&T&"+deleteBufferQ+"&T&"+deleteBufferA);

        Log.i("MyTag",updateBuffer.toString());




        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Set/updateSgSet.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO: set업데이트 성공시 해야할 행동들..
                Log.i("MyTag",response);

                G.loadCurrentSet(UpdateSetActivity.this);
                finish();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.getMessage()==null||error.getMessage().length()==0){
                    G.loadCurrentSet(UpdateSetActivity.this);
                    finish();
                }
                showAlertDialog(R.string.addset_submit_fail05);
                Log.i("MyTag", "upload error : " + error.getMessage());
            }
        });

        multiPartRequest.addStringParam("userID",G.currentStudySet.getUserID()+"");
        multiPartRequest.addStringParam("sgSetID",G.currentStudySet.getSgSetID()+"");
        multiPartRequest.addStringParam("studySetID",G.currentStudySet.getStudySetId()+"");
        multiPartRequest.addStringParam("title",title);
        multiPartRequest.addStringParam("info",info);
        multiPartRequest.addStringParam("update",updateBuffer.toString());

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);

    }


}
