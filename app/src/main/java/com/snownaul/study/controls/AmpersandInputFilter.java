package com.snownaul.study.controls;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by alfo6-11 on 2018-05-21.
 */

public class AmpersandInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        for(int i=start;i<end;i++){
            if(source.charAt(i)=='&')
                return "";
        }

        return null;
    }
}
