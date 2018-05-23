package com.snownaul.study.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.adapters.FeedDetailAdapter;
import com.snownaul.study.controls.AmpersandInputFilter;
import com.snownaul.study.feed_classes.Comment;
import com.snownaul.study.feed_classes.Feed;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

public class FeedDetailActivity extends AppCompatActivity {

    AdView adView;

    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();

    JellyRefreshLayout jelly;

    //View들.

    Toolbar toolbar;
    CircleImageView civProf;
    TextView tvNickname, tvDate;
    ImageView ivMenu, ivImg;
    TextView tvContents;
    ToggleButton tbFavor;
    TextView tvLikedCnt;
    ToggleButton tbComment;
    TextView tvCommentCnt;
    ToggleButton tbBookMark;
    EditText etComment;
    TextView tvComment;

    RecyclerView recyclerView;
    FeedDetailAdapter feedDetailAdapter;

    boolean isEditable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        adView= findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                        loadFeedDetails();
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
        etComment=findViewById(R.id.et_comment);
        tvComment=findViewById(R.id.tv_comment);

        feedDetailAdapter=new FeedDetailAdapter(this);
        recyclerView.setAdapter(feedDetailAdapter);

        setView();
        loadFeedDetails();



        etComment.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300),new AmpersandInputFilter()});

        tbFavor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if(!isEditable) return;

                final Feed t=G.currentFeed;

                String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Feed/feedFavorChanged.php";

                SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyTag","feed likeeeee checkkkkk : "+response);
                        t.setLiked(isChecked);
                        if(isChecked){
                            t.setLikeCnt(t.getLikeCnt()+1);
                        }else {
                            t.setLikeCnt(t.getLikeCnt()-1);
                        }

                        tvLikedCnt.setText(t.getLikeCnt()+"");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tbFavor.setChecked(!isChecked);

                    }
                });

                multiPartRequest.addStringParam("userID",G.getUserId()+"");
                multiPartRequest.addStringParam("feedID",t.getFeedID()+"");

                RequestQueue requestQueue= Volley.newRequestQueue(FeedDetailActivity.this);
                requestQueue.add(multiPartRequest);
            }
        });

        tbBookMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if(!isEditable) return;

                final Feed t=G.currentFeed;

                String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Feed/feedMarkChanged.php";

                SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyTag","feedMark checkkkkk : "+response);
                        t.setFeedMarked(isChecked);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tbBookMark.setChecked(!isChecked);

                    }
                });

                multiPartRequest.addStringParam("userID",G.getUserId()+"");
                multiPartRequest.addStringParam("feedID",t.getFeedID()+"");

                RequestQueue requestQueue= Volley.newRequestQueue(FeedDetailActivity.this);
                requestQueue.add(multiPartRequest);
            }
        });




        G.hideKeyboard(this);


        //setView;

    }

    @Override
    protected void onResume() {
        super.onResume();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public void loadFeedDetails(){
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Feed/loadFeedDetails.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MyTag","FeedDetail : "+response);

                String[] com=response.split("&C&");
                Log.i("MyTag","길이 : "+com.length);

                G.currentFeed.getComments().clear();

                for(int i=0;i<com.length;i++){
                    Log.i("MyTag","각각 코멘트 : "+com[i]);
                    String[] c=com[i].split("&");
                    if(c.length<9)break;

                    Comment t=new Comment(Integer.parseInt(c[0]),Integer.parseInt(c[1]),c[2],G.convertUTCToLocalTime(c[3]),c[4],c[5],(c[6].equals("1")?true:false),Integer.parseInt(c[7]),(c[8].equals("1")?true:false),Integer.parseInt(c[9]));

                    G.currentFeed.getComments().add(t);
                }


                feedDetailAdapter.notifyDataSetChanged();


                setView();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyTag","FeedDetail Error : "+error);

            }
        });

        multiPartRequest.addStringParam("feedID",G.currentFeed.getFeedID()+"");
        multiPartRequest.addStringParam("userID",G.getUserId()+"");

        RequestQueue requestQueue=Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);
    }

    public void setView(){
        isEditable=false;

        if(civProf!=null&&G.currentFeed.getProfImgPath()!=null&&G.currentFeed.getProfImgPath().length()>0)
            Glide.with(this).load(G.currentFeed.getProfImgPath()).into(civProf);
        tvNickname.setText(G.currentFeed.getNickname());
        tvDate.setText(G.currentFeed.getDate());

        if(G.currentFeed.getImgPath()!=null&&G.currentFeed.getImgPath().toString().length()!=0)
            Glide.with(this).load(G.currentFeed.getImgPath()).into(ivImg);
        else
            ivImg.setVisibility(View.GONE);

        tvContents.setText(G.currentFeed.getContents()+"");
        tbFavor.setChecked(G.currentFeed.isLiked());
        tvLikedCnt.setText(G.currentFeed.getLikeCnt()+"");
        tbComment.setChecked(G.currentFeed.isCommented());
        tvCommentCnt.setText(G.currentFeed.getCommentCnt()+"");
        tbBookMark.setChecked(G.currentFeed.isFeedMarked());

        isEditable=true;
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

    public void clickSend(View v){

        final String comment=etComment.getText().toString();
        if(comment==null||comment.length()==0)
            return;

        etComment.setText("");
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Feed/uploadNewComment.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MyTag","FeedDetail : "+response);

                String[] c=response.split("&");

                Comment t=new Comment(Integer.parseInt(c[0]),G.getUserId(),comment,G.convertUTCToLocalTime(c[1]),G.getUserProfilepic(),G.getUserNickname(),false,0,false,0);
                G.currentFeed.getComments().add(t);

                feedDetailAdapter.notifyItemInserted(G.currentFeed.getComments().size()-1);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyTag","FeedDetail Error : "+error);

            }
        });

        multiPartRequest.addStringParam("feedID",G.currentFeed.getFeedID()+"");
        multiPartRequest.addStringParam("userID",G.getUserId()+"");
        multiPartRequest.addStringParam("contents",comment);

        RequestQueue requestQueue=Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);





    }

}
