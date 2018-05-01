package com.snownaul.study.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snownaul.study.Activities.MainActivity;
import com.snownaul.study.R;

/**
 * Created by alfo6-11 on 2018-05-01.
 */

public class F4Report extends Fragment {

    Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.f4_report,container,false);

        toolbar=view.findViewById(R.id.toolbar);

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Report");
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }
}
