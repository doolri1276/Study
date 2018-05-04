package com.snownaul.study.controls;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

/**
 * Created by alfo6-11 on 2018-04-27.
 */

public class GlobalApplication extends Application{

    private static GlobalApplication instance=null;//인스턴스 객체 선언
    private static volatile Activity currentActivity=null;

    //static 객체를 반환하는 이유 : 매번 객체 생성하지 않고 다른 activity에서도 사용 가능
    public static GlobalApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        GlobalApplication.instance=this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public static Activity getCurrentActivity(){
        return currentActivity;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance=null;
    }

    public class KakaoSDKAdapter extends KakaoAdapter {

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Activity getTopActivity() {
                    return GlobalApplication.getCurrentActivity();
                }

                @Override
                public Context getApplicationContext() {
                    return GlobalApplication.getInstance();
                }
            };
        }

        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }


            };
        }
    }
}
