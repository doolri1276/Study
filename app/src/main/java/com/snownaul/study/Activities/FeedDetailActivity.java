package com.snownaul.study.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snownaul.study.G;
import com.snownaul.study.R;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

public class FeedDetailActivity extends AppCompatActivity {

    AdView adView;

    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();

    JellyRefreshLayout jelly;

    //View들.

    CircleImageView civProf;
    TextView tvNickname, tvDate;
    ImageView ivMenu, ivImg;
    TextView tvContents;
    ToggleButton tbFavor;
    TextView tvLikedCnt;
    ToggleButton tbComment;
    TextView tvCommentCnt;
    ToggleButton tbBookMark;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        adView= findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        jelly=findViewById(R.id.jelly);
        jelly.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MyTag","1 REFRESH!!!!!!!!");
                        //G.loadCurrentSet(StudySetMainActivity.this);
                        //setView();
                        jelly.setRefreshing(false);
                    }
                },1500);
            }
        });

        View loadingView= LayoutInflater.from(this).inflate(R.layout.jelly_loading,null);
        jelly.setLoadingView(loadingView);


        helper.setEdgeMode(true).setParallaxMode(true).setParallaxRatio(3).setNeedBackgroundShadow(true).init(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //View들..
        civProf=findViewById(R.id.civ_prof);
        tvNickname=findViewById(R.id.tv_nickname);
        tvDate=findViewById(R.id.tv_date);
        ivMenu=findViewById(R.id.iv_menu);
        ivImg=findViewById(R.id.iv_img);
        tvContents=findViewById(R.id.tv_contents);
        tbFavor=findViewById(R.id.tb_favor);
        tvLikedCnt=findViewById(R.id.tv_liked_cnt);
        tbComment=findViewById(R.id.tb_comment);
        tvCommentCnt=findViewById(R.id.tv_comment_cnt);
        tbBookMark=findViewById(R.id.tb_bookmark);
        recyclerView=findViewById(R.id.recycler);

        //TODO:adapter를 만들어야 한다



        //setView;

    }

    public void setView(){
        Glide.with(this).load(G.currentFeed.getProfImgPath()).into(civProf);
        tvNickname.setText(G.currentFeed.getNickname());
        tvDate.setText(G.currentFeed.getDate());

        if(G.currentFeed.getImgPath()!=null&&G.currentFeed.getImgPath().toString().length()!=0)
            Glide.with(this).load(G.currentFeed.getImgPath()).into(ivImg);
        else
            ivImg.setVisibility(View.GONE);

        tvContents.setText(G.currentFeed.getContents());
        tbFavor.setChecked(G.currentFeed.isLiked());
        tvLikedCnt.setText(G.currentFeed.getLikeCnt());
        tbComment.setChecked(G.currentFeed.isCommented());
        tvCommentCnt.setText(G.currentFeed.getCommentCnt());
        tbBookMark.setChecked(G.currentFeed.isFeedMarked());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                helper.finish();
                return true;
            case R.id.action_refresh:
                jelly.post(new Runnable() {
                    @Override
                    public void run() {
                        jelly.setRefreshing(true);
                        Log.i("MyTag","2 REFRESH!!!!!!!!");
                    }
                });
                jelly.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jelly.setRefreshing(false);
                        Log.i("MyTag","3 REFRESH!!!!!!!!");
                    }
                },1500);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        helper.finish();
    }
}
