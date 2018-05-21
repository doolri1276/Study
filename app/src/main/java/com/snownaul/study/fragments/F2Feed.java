package com.snownaul.study.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.snownaul.study.Activities.AddFeedActivity;
import com.snownaul.study.Activities.MainActivity;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.FeedFragAdapter;
import com.snownaul.study.feed_classes.Feed;

import java.util.Arrays;

import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

/**
 * Created by alfo6-11 on 2018-05-01.
 */

public class F2Feed extends Fragment {

    Toolbar toolbar;

    JellyRefreshLayout jelly;

    FloatingActionButton fab;

    RecyclerView recyclerView;
    FeedFragAdapter feedFragAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.f2_feed,container,false);

        toolbar=view.findViewById(R.id.toolbar);

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Feed");
        setHasOptionsMenu(true);

        jelly=view.findViewById(R.id.jelly);
        jelly.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadAllFeeds();
                        jelly.setRefreshing(false);
                    }
                },3000);
            }
        });

        View loadingView=LayoutInflater.from(getContext()).inflate(R.layout.jelly_loading,null);
        jelly.setLoadingView(loadingView);

        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getContext(), AddFeedActivity.class);
                startActivity(intent);

            }
        });

        loadAllFeeds();

        recyclerView=view.findViewById(R.id.recycler);
        feedFragAdapter=new FeedFragAdapter(getContext());
        recyclerView.setAdapter(feedFragAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAllFeeds();

    }

    public void loadAllFeeds(){
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Feed/loadAllFeeds.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("MyTag","Feed 받아온다아아아아아아아아\n\n"+response);

                String[] feeds=response.split("&F&");
                Log.i("MyTag","길이 : "+feeds.length);


                G.feeds.clear();

                for(int i=0;i<feeds.length;i++){
                    //Log.i("MyTag","Feed 1문장씩 : "+ feeds[i]);
                    String[] f=feeds[i].split("&");
                    if(f.length<11)break;
                    //Log.i("MyTag", "array화 : "+Arrays.toString(f));

                    Feed t=new Feed(Integer.parseInt(f[0]),Integer.parseInt(f[1]),f[2],f[3],G.convertUTCToLocalTime(f[4]),
                            f[5],f[6],(f[7].equals("1"))?true:false, Integer.parseInt(f[8]), (f[9].equals("1"))?true:false, Integer.parseInt(f[10]),(f[11].equals("1"))?true:false);

                    G.feeds.add(t);

                }

                feedFragAdapter.notifyDataSetChanged();
                Log.i("MyTag","몇개더냐아 : "+feedFragAdapter.getItemCount());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("MyTag","Feed받아오는데 문제생김.. : "+error.getMessage());

            }
        });

        multiPartRequest.addStringParam("userID", G.getUserId()+"");

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());

        requestQueue.add(multiPartRequest);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.i("My","f2feed");
        inflater.inflate(R.menu.f2_menu,menu);
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
