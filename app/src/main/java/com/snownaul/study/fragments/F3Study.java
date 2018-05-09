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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.snownaul.study.Activities.AddSetActivity;
import com.snownaul.study.Activities.MainActivity;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.StudyFragAdapter;
import com.snownaul.study.study_classes.SgSet;
import com.snownaul.study.study_classes.StudySet;

import java.util.Arrays;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

/**
 * Created by alfo6-11 on 2018-05-01.
 */

public class F3Study extends Fragment {

    Toolbar toolbar;

    JellyRefreshLayout jelly;

    CircleImageView profile;
    TextView nickname;
    TextView date;

    FloatingActionButton fab;

    RecyclerView recyclerView;
    StudyFragAdapter studyFragAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.f3_study,container,false);

        toolbar=view.findViewById(R.id.toolbar);

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Study");
        setHasOptionsMenu(true);

        jelly=view.findViewById(R.id.jelly);
        jelly.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MyTag","1 REFRESH!!!!!!!!");
                        loadStudySets();
                        jelly.setRefreshing(false);
                    }
                },2000);
            }
        });

        View loadingView=LayoutInflater.from(getContext()).inflate(R.layout.jelly_loading,null);
        jelly.setLoadingView(loadingView);

        profile=view.findViewById(R.id.profile);
        nickname=view.findViewById(R.id.nickname);
        date=view.findViewById(R.id.date);

        if(profile!=null)
            Glide.with(getContext()).load(G.getUserProfilepic()).into(profile);
        nickname.setText(G.getUserNickname());

        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddSetActivity.class);
                startActivity(intent);
            }
        });


        recyclerView=view.findViewById(R.id.recycler);
        studyFragAdapter=new StudyFragAdapter(getContext());
        recyclerView.setAdapter(studyFragAdapter);





        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadStudySets();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.i("My","f3study");
        inflater.inflate(R.menu.f3_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id=item.getItemId();

        if(id==R.id.action_refresh){
            jelly.post(new Runnable() {
                @Override
                public void run() {
                    jelly.setRefreshing(true);
                    Glide.with(getContext()).load(G.getUserProfilepic()).into(profile);
                    Log.i("MyTag","2 REFRESH!!!!!!!!");
                }
            });
            jelly.postDelayed(new Runnable() {
                @Override
                public void run() {
                    jelly.setRefreshing(false);
                    Log.i("MyTag","3 REFRESH!!!!!!!!");
                }
            },2000);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void loadStudySets(){
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Set/loadAllStudySets.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MyTag","RESPONSE : "+response);
                String[] studySets=response.split("&S&");

                G.studySets.clear();

                for(int i=1;i<studySets.length;i++){
                    StudySet st=new StudySet();
                    String[] studySetDetail=studySets[i].split("&");

                    st.setStudySetId(Integer.parseInt(studySetDetail[0]));
                    st.setSgSetID(Integer.parseInt(studySetDetail[1]));
                    st.setSgSet(new SgSet(Integer.parseInt(studySetDetail[1])));
                    st.setRecentDate(G.convertUTCToLocalTime(studySetDetail[2]));
                    st.getSgSet().setTitle(studySetDetail[3]);
                    st.getSgSet().setInfo(studySetDetail[4]);
                    st.getSgSet().setLikeCnt(Integer.parseInt(studySetDetail[5]));
                    st.setLiked(Boolean.parseBoolean(studySetDetail[6]));
                    st.setTriedCnt(Integer.parseInt(studySetDetail[7]));

                    G.studySets.add(st);


                }
                Collections.sort(G.studySets);
                studyFragAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyTag","ERROR : "+error);
            }
        });

        multiPartRequest.addStringParam("userID",G.getUserId()+"");

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());

        requestQueue.add(multiPartRequest);
    }
}
