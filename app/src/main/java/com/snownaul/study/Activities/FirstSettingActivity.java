package com.snownaul.study.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.TextView;

import com.snownaul.study.R;

public class FirstSettingActivity extends AppCompatActivity {

    TextView tv;
    EditText et01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting);

        tv=findViewById(R.id.tv_info);

        et01=findViewById(R.id.et_info01);

        et01.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for(int i=start;i<end;i++){

                    if(source.charAt(i)=='_'){

                    }else if(!Character.isLetterOrDigit(source.charAt(i))){
                        return "";
                    }
                }
                return null;
            }
        },new InputFilter.LengthFilter(12)});



    }
}
