package com.snownaul.study.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.snownaul.study.fragments.F1Home;
import com.snownaul.study.fragments.F2Feed;
import com.snownaul.study.fragments.F3Study;
import com.snownaul.study.fragments.F4Report;
import com.snownaul.study.fragments.F5Settings;

/**
 * Created by alfo6-11 on 2018-05-01.
 */

public class FragAdapter extends FragmentPagerAdapter {

    Fragment[] frags=new Fragment[5];

    public FragAdapter(FragmentManager fm) {
        super(fm);

        frags[0]=new F1Home();
        frags[1]=new F2Feed();
        frags[2]=new F3Study();
        frags[3]=new F4Report();
        frags[4]=new F5Settings();

    }

    @Override
    public Fragment getItem(int position) {
        return frags[position];
    }

    @Override
    public int getCount() {
        return frags.length;
    }
}
