package com.snownaul.study.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.snownaul.study.Activities.MainActivity;
import com.snownaul.study.G;
import com.snownaul.study.R;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

/**
 * Created by alfo6-11 on 2018-05-01.
 */

public class F1Home extends Fragment {

    Toolbar toolbar;
    JellyRefreshLayout jelly;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.f1_home,container,false);

        toolbar=view.findViewById(R.id.toolbar);

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        //toolbar.setTitle("Home");
        setHasOptionsMenu(true);

        jelly=view.findViewById(R.id.jelly);
        jelly.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jelly.setRefreshing(false);
                    }
                },3000);
            }
        });

        View loadingView=LayoutInflater.from(getContext()).inflate(R.layout.jelly_loading,null);
        jelly.setLoadingView(loadingView);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.i("My","f1home");
        inflater.inflate(R.menu.f1_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if(id==R.id.action_refresh){
            jelly.post(new Runnable() {
                @Override
                public void run() {
                    jelly.setRefreshing(true);

                }
            });
            jelly.postDelayed(new Runnable() {
                @Override
                public void run() {
                    jelly.setRefreshing(false);
                }
            },3000);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
