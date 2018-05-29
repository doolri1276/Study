package com.snownaul.study.Activities;

import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.bumptech.glide.Glide;
import com.fenjuly.mylibrary.ToggleExpandLayout;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.NewQuestionsAdapter;
import com.snownaul.study.controls.AmpersandInputFilter;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;
import com.snownaul.study.study_classes.SgSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.snownaul.study.Activities.AddFeedActivity.CAMERA_PERMISSION;
import static com.snownaul.study.Activities.AddFeedActivity.EXTERNAL_STORAGE_PERMISSION;

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

    RadioGroup rgPrivacy;
    ImageView btnClear;
    int privacyNum=0;

//    int picPosition;
//
//    public static final int EXTERNAL_STORAGE_PERMISSION=100;
//    public final int CAMERA_PERMISSION=110;
//    public final int CAMERA_IMAGE=10;
//    public final int PHOTO_IMAGE=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_set);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.addset_actitle));

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

        etTitle.setFilters(new InputFilter[]{new AmpersandInputFilter()});
        etInfo.setFilters(new InputFilter[]{new AmpersandInputFilter()});

        //TODO:시험이라던지 이런게 설정이 덜됬기 때문에 일단 접어둔다..
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


//        if(G.getUserId()==2||G.getUserId()==3){
//            //외부 저장소 읽고쓰기 권한 퍼미션 체크 및 요청
//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//                if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
//                    //허용이 안되어 있는 상태이므로 퍼미션 다이얼로그 보이기
//                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},EXTERNAL_STORAGE_PERMISSION);
//
//                }
//                if(checkSelfPermission(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){
//                    requestPermissions(new String[]{android.Manifest.permission.CAMERA},CAMERA_PERMISSION);
//                }
//            }
//        }


        G.hideKeyboard(this);

    }

//    public void clickCamera(int position){
//        picPosition=position;
//        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent,CAMERA_IMAGE);
//
//
//    }
//
//    public void clickPhoto(int position){
//        picPosition=position;
//        Intent intent=new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, PHOTO_IMAGE);
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        switch (requestCode){
//            case CAMERA_IMAGE:
//                if(resultCode==RESULT_OK){
//                    Uri uri=data.getData();
//
//                    if(uri!=null){
//                        Log.i("MyTag","사진 업로드 uri로 나왔습니다. : "+uri.toString());
//                        Toast.makeText(this, uri.toString(),Toast.LENGTH_SHORT).show();
//
//                        G.newQuestions.get(picPosition).setImgPath(getRealPathFromUri(uri));
//
//                        newQuestionsAdapter.notifyItemChanged(picPosition);
//
//                    }else{
//                        Bitmap bm=(Bitmap)data.getExtras().get("data");
//                        Log.i("MyTag","비트맵으로 나왔습니다.");
//                        if(bm!=null){
//                            //Glide.with(this).load(bm).into(iv);
//                            Uri uriFromBm=saveBitmapAndGetUri(bm);
//
//                            if(uriFromBm!=null) {
//                                Log.i("MyTag","bitmap 저장하고 uri로 바꿨습니다 : "+uriFromBm.toString());
//
//                                G.newQuestions.get(picPosition).setImgPath(uriFromBm.getPath());
//                                newQuestionsAdapter.notifyItemChanged(picPosition);
//
//                            }else{
//                                Log.i("MyTag","널입니다 ㅠㅠㅠ 왜죠?");
//
//                            }
//                            //Toast.makeText(this, "uri는 널이다..", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    }
//                }
//
//                break;
//            case PHOTO_IMAGE:
//                if(resultCode==RESULT_OK){
//                    Uri uri=data.getData();
//
//                    if(uri!=null){
//                        Toast.makeText(this, uri.toString(),Toast.LENGTH_SHORT).show();
//                        G.newQuestions.get(picPosition).setImgPath(getRealPathFromUri(uri));
//                        newQuestionsAdapter.notifyItemChanged(picPosition);
//                    }
//                }
//                break;
//            case EXTERNAL_STORAGE_PERMISSION:
//
//                break;
//            case CAMERA_PERMISSION:
//
//                break;
//
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    String getRealPathFromUri(Uri uri){
//        String[] proj={MediaStore.Images.Media.DATA};
//        CursorLoader loader=new CursorLoader(this,uri,proj,null,null,null);
//        Cursor cursor=loader.loadInBackground();
//        int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String result = cursor.getString(column_index);
//        cursor.close();
//
//        Log.i("MyTag","REAL PATH : "+result);
//        return result;
//
//    }
//
//    Uri saveBitmapAndGetUri(Bitmap bitmap){
//        File path= Environment.getExternalStorageDirectory();
//
//        String time=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File file=new File(path,time+".jpg");
//
//        Uri uri=null;
//
//        try{
//            FileOutputStream fos=new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
//
//            fos.flush();
//            fos.close();
//
//            Log.i("MyTag","빝맵파일 저장 완료!!");
//            Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            uri=Uri.parse(file.getPath());
//            intent.setData(uri);
//
//            sendBroadcast(intent);
//
//            return uri;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//
//    }

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
        multiPartRequest.addStringParam("privacy",privacyNum+"");

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
            buffer.append((t.isRightOrWrong()==true?1:0));

            for(int j=0;j<t.getAnswers().size();j++){
                Answer ta=t.getAnswers().get(j);
                buffer.append(aDivider);
                buffer.append(ta.getAnswer()+detailDivider);
                buffer.append((ta.isCorrect()?1:0)+detailDivider);
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



    public void clickSetting(View v){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.dialog_add_set,null);
        builder.setView(view);


        final AlertDialog dialog=builder.create();
        rgPrivacy=view.findViewById(R.id.rg_privacy);

        rgPrivacy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rg_privacy_public:
                        privacyNum=0;
                        break;
                    case R.id.rg_privacy_private:
                        privacyNum=1;
                        break;
                }
            }
        });
        btnClear=view.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();










    }


}
