package com.snownaul.study.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.snownaul.study.G;
import com.snownaul.study.R;

import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {

    LinearLayout logo;

    //카카오 로그인을 위한 것들
    LoginButton kakaoLoginBtn;
    private SessionCallback callback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callback=new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        requestMe();


        setContentView(R.layout.activity_login);

        //애니메이션 설정
//        logo=findViewById(R.id.logo);
//
//        Animation ani= AnimationUtils.loadAnimation(this,R.anim.appear_logo);
//        logo.startAnimation(ani);

        //카카오 로그인
        kakaoLoginBtn=findViewById(R.id.kakao_loginbtn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //카카오 로그인 확인
        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)){
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void clickFB(View v){

    }

    public void clickKakao(View v){
        kakaoLoginBtn.performClick();
    }

    public void clickGoogle(View v){

    }

    //카카오 로그인 (유저의 정보를 받아오는 함수)////////////////////////////
    protected void requestMe(){
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("Error", "오류로 카카오로그인 실패 ");
                Log.d("MyTag","95 : "+errorResult.getErrorMessage());
                //Toast.makeText(LoginActivity.this, R.string.login_fail01+" : 96", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNotSignedUp() {//카카오톡 회원이 아닐 시 호출됨.
                //Toast.makeText(LoginActivity.this, R.string.login_fail02+" : 102", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(UserProfile result) {
                Log.i("Kakao","UserProfile : "+result);
                Log.i("Kakao",result.getId()+",,,,,"+result.toString());

                String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/User/loginUser.php";
                String sns="Kakao";
                int snsID= (int) result.getId();

                G.setUserSns(sns);
                G.setUserSnsId(result.getId());
                G.setUserProfilepic(result.getThumbnailImagePath());


                SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyTag","R : "+response);
                        if(response.equals("FIRST")){
                            Intent intent=new Intent(LoginActivity.this,FirstSettingActivity.class);
                            startActivity(intent);
                            finish();
                        }else{

                            String[] userInfo=response.split("&");


                            G.setUserId(Integer.parseInt(userInfo[0]));
                            G.setUserNickname(userInfo[1]);
                            G.setUserAge(Integer.parseInt(userInfo[2]));
                            G.setUserProfilepic(userInfo[3]);

                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("MyTag","132"+error.getMessage());
                        //Toast.makeText(LoginActivity.this, R.string.login_fail01+" : 133", Toast.LENGTH_SHORT).show();
                    }
                });

                multiPartRequest.addStringParam("sns",sns);
                multiPartRequest.addStringParam("snsID",snsID+"");

                RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);

                requestQueue.add(multiPartRequest);

                //Toast.makeText(LoginActivity.this, "카카오 로그인 성공!", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message="failed to get user info. msg="+errorResult;
                Log.i("Kakao",message);
                Log.v("Kakao","fail");

                //Toast.makeText(LoginActivity.this, "160: "+R.string.login_fail01, Toast.LENGTH_SHORT).show();

                ErrorCode result=ErrorCode.valueOf(errorResult.getErrorCode());
                if(result==ErrorCode.CLIENT_ERROR_CODE){

                }else{

                }
            }
        });


    }




    private class SessionCallback implements ISessionCallback{

        //로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            //가입한적 있는지 확인

            //Toast.makeText(LoginActivity.this, "카카오 세션 오픈!!", Toast.LENGTH_SHORT).show();
            //첫 로그인이면 FirstSettingAc로 가고

            //로그인 한 적 있으면 MainAc로....

            requestMe();

        }

        //로그인 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {

            //Toast.makeText(LoginActivity.this, "197: "+R.string.login_fail03, Toast.LENGTH_SHORT).show();
            if(exception!=null){
                com.kakao.util.helper.log.Logger.e(exception);
                
                
            }
        }



    }


}
