package com.snownaul.study.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.snownaul.study.R;

public class FirstSettingActivity extends AppCompatActivity {

    TextView tv;
    EditText et01,et02;
    TextView tvWar;

    int page=0;

    String nickname;
    int age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting);

        tv=findViewById(R.id.tv_info);
        tvWar=findViewById(R.id.tv_warning);

        et01=findViewById(R.id.et_info01);
        et02=findViewById(R.id.et_info02);

        et01.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if(source.length()>0){
                    tvWar.setVisibility(View.GONE);
                }

                for(int i=start;i<end;i++){

                    if(source.charAt(i)=='_'){

                    }else if(!Character.isLetterOrDigit(source.charAt(i))){
                        return "";
                    }
                }
                return null;
            }
        },new InputFilter.LengthFilter(12)});

        et02.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.length()>0){
                    tvWar.setVisibility(View.GONE);
                }
                return null;
            }
        },new InputFilter.LengthFilter(2)});

    }

    public void clickNext(View v){

        switch (page){
            case 0:
                checkNickname();
                break;
            case 1:
                checkAge();
                break;

            case 2:
                checkInterest();
                break;
            case 3:
                checkSetting();
                break;
            case 4:
                registerUser();
                break;
        }

    }

    public void checkNickname(){

        final String nick=et01.getText().toString();

        if(nick==null||nick.length()==0){
            tvWar.setText(R.string.first_warning01);
            tvWar.setVisibility(View.VISIBLE);
            return;
        }

        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/User/checkNickname.php";

        //파일전송요청객체 생성
        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("NicknameCheck",response+"");

                if(response.equals("Available")){
                    Toast.makeText(FirstSettingActivity.this, response+"닉네임 사용가능", Toast.LENGTH_SHORT).show();

                    page=1;

                    nickname=nick;

                    tv.setText(R.string.first_info01+page);
                    et01.setVisibility(View.GONE);
                    et02.setVisibility(View.VISIBLE);


                }else{
                    Toast.makeText(FirstSettingActivity.this, response+"닉네임 중복!!!!!", Toast.LENGTH_SHORT).show();

                    tvWar.setText(R.string.first_warning02);
                    tvWar.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirstSettingActivity.this, getString(R.string.error_basic), Toast.LENGTH_SHORT).show();
            }
        });

        //요청객체에 데이터 추가하기
        multiPartRequest.addStringParam("nickname",nick);

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);



    }

    public void checkAge(){
        String str=et02.getText().toString();

        if(str==null||str.length()==0){
            tvWar.setText(R.string.first_warning01);
            tvWar.setVisibility(View.VISIBLE);
            return;
        }

        age=Integer.parseInt(str);

        page=2;

        tv.setText(R.string.first_info01+page);






    }

    public void checkInterest(){
        page=3;

        tv.setText(R.string.first_info01+page);
    }

    public void checkSetting(){
        page=4;

        tv.setText(R.string.first_info01+page);

    }

    public void registerUser(){

        //지금까지 모인것들을 가지고 서버에 등록을 한다
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/User/registerUser.php";

        //파일전송요청객체 생성
        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirstSettingActivity.this, getString(R.string.error_basic), Toast.LENGTH_SHORT).show();
            }
        });

        //요청객체에 데이터 추가하기
        multiPartRequest.addStringParam("nickname",nickname);
        multiPartRequest.addStringParam("age",age+"");

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);




    }







}
