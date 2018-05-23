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
import com.snownaul.study.adapters.FeedCommentAdapter;
import com.snownaul.study.controls.AmpersandInputFilter;
import com.snownaul.study.feed_classes.Comment;
import com.snownaul.study.feed_classes.SubComment;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

public class FeedCommentActivity extends AppCompatActivity {

    AdView adView;

    SwipeBackActivityHelper helper=new SwipeBackActivityHelper();

    JellyRefreshLayout jelly;

    Toolbar toolbar;

    //View들..
    CircleImageView civProf;
    TextView tvNickname, tvDate;
    ImageView ivMenu;
    TextView tvComment;
    ToggleButton tbFavor;
    TextView tvLikedCnt;
    ToggleButton tbSubComment;
    TextView tvSubCommentCnt;
    EditText etSubComment;
    RecyclerView recyclerView;

    FeedCommentAdapter feedCommentAdapter;



    boolean isEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_comment);

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
                        loadSubComments();
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
        tvComment=findViewById(R.id.tv_comment);
        tbFavor=findViewById(R.id.tb_favor);
        tvLikedCnt=findViewById(R.id.tv_liked_cnt);
        tbSubComment=findViewById(R.id.tb_sub_comment);
        tvSubCommentCnt=findViewById(R.id.tv_sub_comment_cnt);
        etSubComment=findViewById(R.id.et_sub_comment);
        recyclerView=findViewById(R.id.recycler);

        tbFavor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if(!isEditable) return;

                final Comment t= G.currentComment;

                String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Feed/commentFavorChanged.php";

                SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyTag","comment likeeeee checkkkkk : "+response);
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
                multiPartRequest.addStringParam("commentID",t.getCommentID()+"");

                RequestQueue requestQueue= Volley.newRequestQueue(FeedCommentActivity.this);
                requestQueue.add(multiPartRequest);

            }
        });
        setView();
        loadSubComments();

        etSubComment.setFilters(new InputFilter[]{new AmpersandInputFilter(), new InputFilter.LengthFilter(300)});

        feedCommentAdapter=new FeedCommentAdapter(this);
        recyclerView.setAdapter(feedCommentAdapter);

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

    public void loadSubComments(){
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Feed/loadAllSubComments.php";
        Log.i("MyTag","LoadSubComments 불러졌다!!!!!");

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MyTag","load SubComments : "+response);

                String[] sub=response.split("&S&");
                Log.i("MyTag","길이 : "+sub.length);

                G.currentComment.getSubComments().clear();

                for(int i=0;i<sub.length;i++){
                    String[] s=sub[i].split("&");

                    if(s.length<7)break;

                    SubComment t=new SubComment(Integer.parseInt(s[0]),G.currentComment.getCommentID(),Integer.parseInt(s[1]),s[2],G.convertUTCToLocalTime(s[3]),s[4],s[5],(s[6].equals("1")),Integer.parseInt(s[7]));
                    G.currentComment.getSubComments().add(t);
                }

                feedCommentAdapter.notifyDataSetChanged();
                setView();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyTag","썹댓 볼라구우 근데 에러남 : "+error.getMessage());

            }
        });

        multiPartRequest.addStringParam("commentID",G.currentComment.getCommentID()+"");
        multiPartRequest.addStringParam("userID",G.getUserId()+"");

        RequestQueue requestQueue=Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);
        Log.i("MyTag","썹댓 달라고 메세지 보냈다!!");
    }

    public void setView(){
        isEditable=false;

        Comment t=G.currentComment;

        Glide.with(this).load(t.getImgPath()).thumbnail(0.1f).into(civProf);

        tvNickname.setText(t.getNickname());
        tvDate.setText(t.getDate());
        tvComment.setText(t.getContents());
        tbFavor.setChecked(t.isLiked());
        tvLikedCnt.setText(t.getLikeCnt()+"");
        tbSubComment.setChecked(t.isSubCommented());
        tvSubCommentCnt.setText(t.getSubCommentCnt()+"");

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
        final String subComment=etSubComment.getText().toString();
        if(subComment==null||subComment.length()==0)
            return;

        etSubComment.setText("");
        String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Feed/uploadNewSubComment.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MyTag","CommentDetail : "+response);

                String[] c=response.split("&");

                SubComment t=new SubComment(Integer.parseInt(c[0]),G.currentComment.getCommentID(),G.getUserId(),subComment,G.convertUTCToLocalTime(c[1]),G.getUserProfilepic(),G.getUserNickname(),false,0);
                G.currentComment.getSubComments().add(t);
                feedCommentAdapter.notifyItemInserted(G.currentComment.getSubComments().size()-1);

                G.currentComment.setSubCommented(true);
                G.currentComment.setSubCommentCnt(G.currentComment.getSubCommentCnt()+1);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MyTag","CommentDetail Error : "+error);

            }
        });

        multiPartRequest.addStringParam("commentID",G.currentComment.getCommentID()+"");
        multiPartRequest.addStringParam("userID",G.getUserId()+"");
        multiPartRequest.addStringParam("contents",subComment);

        RequestQueue requestQueue=Volley.newRequestQueue(this);

        requestQueue.add(multiPartRequest);
    }
}
