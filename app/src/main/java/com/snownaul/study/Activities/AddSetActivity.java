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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.fenjuly.mylibrary.ToggleExpandLayout;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.NewQuestionsAdapter;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;
import com.snownaul.study.study_classes.SgSet;

import java.util.ArrayList;

public class AddSetActivity extends AppCompatActivity {

    //View들 추가

    ScrollView scroll;

    EditText etTitle;
    EditText etInfo;

    LinearLayout addQuestion;

    RecyclerView recyclerView;

    //첫 셋팅 관련
    int defaultQuestionType= Question.TYPE_BASIC;
    TextView questionType;

    NewQuestionsAdapter newQuestionsAdapter;

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

        //sgSet에 새로운 세트 만듬
        G.newSgSet=new SgSet(G.getUserId(),G.getUserNickname());
        G.newQuestions=new ArrayList<>();
        G.newQuestions.add(new Question(defaultQuestionType));

        newQuestionsAdapter=new NewQuestionsAdapter(this);
        recyclerView.setAdapter(newQuestionsAdapter);

        addQuestion.setOnLongClickListener(new View.OnLongClickListener() {

            TextView t1,t2,t3,t4;


            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(AddSetActivity.this);

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
                finish();
                return true;

            case R.id.submit:
                submitSet();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public void clickFab(View v){
        scroll.scrollTo(5,10);

    }

    public void clickAddQuestion(View v){
        G.newQuestions.add(new Question(defaultQuestionType));

        newQuestionsAdapter.notifyItemInserted(G.newQuestions.size()-1);
        recyclerView.scrollToPosition(G.newQuestions.size()-1);
    }

    public void submitSet(){
        //타이틀 체크
        title=etTitle.getText().toString();
        if(title==null||title.length()==0){
            showAlertDialog(R.string.addset_submit_fail01);
            return;
        }

        info=etInfo.getText().toString();

        G.newSgSet.setTitle(title);
        G.newSgSet.setInfo(info);
        //Settings내용 체크

        boolean answerExists;
        //문제들중에 빈칸 있는지 확인
        for(int i=0;i<G.newQuestions.size();i++){
            Question t=G.newQuestions.get(i);
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

        uploadNewSgSet();




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

    public void uploadNewSgSet(){
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Set/uploadNewSgSet.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO: set추가 성공시 해야할 행동들..
                String[] str=response.split("&");
                Log.i("MyTag",response);
                if(str[0].equals("SUCCESSFUL")){
                    Toast.makeText(AddSetActivity.this, "successful : "+str[1], Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddSetActivity.this, "실패실패", Toast.LENGTH_SHORT).show();
                }

                SwipeBackActivityHelper.startSwipeActivity(AddSetActivity.this,new Intent(AddSetActivity.this,StudySetMainActivity.class),true,true,true);
                finish();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showAlertDialog(R.string.addset_submit_fail05);
                Log.i("MyTag", "upload error : " + error.getMessage());
            }
        });

        multiPartRequest.addStringParam("userID",G.getUserId()+"");
        multiPartRequest.addStringParam("title",title);
        multiPartRequest.addStringParam("info",info);

        StringBuffer buffer=new StringBuffer();
        String qDivider="&Q&";
        String aDivider="&A&";
        String detailDivider="[&]";

        for(int i=0;i<G.newQuestions.size();i++){
            Question t=G.newQuestions.get(i);
            buffer.append(t.getQuestionType()+detailDivider);
            buffer.append(t.getQuestion()+detailDivider);
            String expl=t.getExplanation();
//            if(expl==null||expl.length()==0)
//                buffer.append("NULL"+detailDivider);
            buffer.append(t.getExplanation()+detailDivider);
            buffer.append(t.isRightOrWrong());

            for(int j=0;j<t.getAnswers().size();j++){
                Answer ta=t.getAnswers().get(j);
                buffer.append(aDivider);
                buffer.append(ta.getAnswer()+detailDivider);
                buffer.append(ta.isCorrect()+detailDivider);
                buffer.append(ta.getSgOrder());
            }

            if(i==G.newQuestions.size()-1)
                break;

            buffer.append(qDivider);
        }

        Log.i("MyTag",buffer.toString());

        multiPartRequest.addStringParam("questions",buffer.toString());

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);



    }
}
