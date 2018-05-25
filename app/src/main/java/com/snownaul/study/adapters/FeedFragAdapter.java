package com.snownaul.study.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.BlurEffect;
import com.mingle.sweetpick.RecyclerViewDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.snownaul.study.Activities.FeedDetailActivity;
import com.snownaul.study.Activities.MainActivity;
import com.snownaul.study.Activities.StudySetMainActivity;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.feed_classes.Feed;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alfo6-11 on 2018-05-18.
 */

public class FeedFragAdapter extends RecyclerView.Adapter {

    Context context;

    boolean isEditable;

    public FeedFragAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.feed_main,parent,false);
        VH holder=new VH(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH) holder;
        Feed t= G.feeds.get(position);
        //Log.i("MyTag","지금 "+position+"번째꺼 만들었당 : "+t.getContents());

        isEditable=false;

        int i=vh.itemView.getVisibility();
        //Log.i("MyTag",("보여지고 있나?"+i));
        Glide.with(context).load(t.getProfImgPath()).thumbnail(0.1f).into(vh.civProf);

        vh.tvNickname.setText(t.getNickname());
        vh.tvDate.setText(t.getDate());

        if(t.getImgPath()!=null&&t.getImgPath().length()!=0){
            vh.ivImg.setVisibility(View.VISIBLE);
            Glide.with(context).load(t.getImgPath()).thumbnail(0.1f).into(vh.ivImg);
        }else
            vh.ivImg.setVisibility(View.GONE);


        vh.tvContents.setText(t.getContents());
        vh.tbFavor.setChecked(t.isLiked());
        vh.tvLikedCnt.setText(t.getLikeCnt()+"");
        vh.tbComment.setChecked(t.isCommented());
        vh.tvCommentCnt.setText(t.getCommentCnt()+"");
        vh.tbBookMark.setChecked(t.isFeedMarked());

        isEditable=true;
    }

    @Override
    public int getItemCount() {
        if(G.feeds==null) return 0;
        else
        return G.feeds.size();
    }

    public class VH extends RecyclerView.ViewHolder{

        RelativeLayout rl;
        CircleImageView civProf;
        TextView tvNickname, tvDate;
        ImageView ivMenu, ivImg;
        TextView tvContents;
        ToggleButton tbFavor;
        TextView tvLikedCnt;
        ToggleButton tbComment;
        TextView tvCommentCnt;
        ToggleButton tbBookMark;

        public VH(View itemView) {
            super(itemView);

            rl=itemView.findViewById(R.id.rl);
            civProf=itemView.findViewById(R.id.civ_prof);
            tvNickname=itemView.findViewById(R.id.tv_nickname);
            tvDate=itemView.findViewById(R.id.tv_date);
            ivMenu=itemView.findViewById(R.id.iv_menu);
            ivImg=itemView.findViewById(R.id.iv_img);
            tvContents=itemView.findViewById(R.id.tv_contents);
            tbFavor=itemView.findViewById(R.id.tb_favor);
            tvLikedCnt=itemView.findViewById(R.id.tv_liked_cnt);
            tbComment=itemView.findViewById(R.id.tb_comment);
            tvCommentCnt=itemView.findViewById(R.id.tv_comment_cnt);
            tbBookMark=itemView.findViewById(R.id.tb_bookmark);

            tvContents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    G.currentFeed=G.feeds.get(getLayoutPosition());
                    SwipeBackActivityHelper.startSwipeActivity((MainActivity)context,new Intent(context,FeedDetailActivity.class),true,true,true);

                }
            });

            ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    G.currentFeed=G.feeds.get(getLayoutPosition());
                    SwipeBackActivityHelper.startSwipeActivity((MainActivity)context,new Intent(context,FeedDetailActivity.class),true,true,true);

                }
            });

            tbComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    G.currentFeed=G.feeds.get(getLayoutPosition());
                    SwipeBackActivityHelper.startSwipeActivity((MainActivity)context,new Intent(context,FeedDetailActivity.class),true,true,true);

                }
            });

            ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "눌렸다.", Toast.LENGTH_SHORT).show();

                    final SweetSheet sweetSheet=new SweetSheet(rl);
                    sweetSheet.setMenuList(R.menu.feed_menu);
                    sweetSheet.setDelegate(new RecyclerViewDelegate(true));
                    sweetSheet.setBackgroundEffect(new BlurEffect(8));
                    sweetSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
                        @Override
                        public boolean onItemClick(int position, MenuEntity menuEntity) {

                            switch (position){
                                case 0:
                                    Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                                    break;
                            }


                            return false;
                        }
                    });

//                    final PopupMenu popup=new PopupMenu(context, v);
//
//                    popup.getMenuInflater().inflate(R.menu.feed_menu, popup.getMenu());
//
//
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//
//                            switch (item.getItemId()){
//                                case R.id.menu_edit:
//                                    Toast.makeText(context, "Edit 눌렀다.", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case R.id.menu_delete:
//                                    Toast.makeText(context, "Delete 눌렀다.", Toast.LENGTH_SHORT).show();
//                                    break;
//                            }
//
//
//
//
//                            return false;
//                        }
//                    });
//
//                    popup.show();
                }
            });

            tbFavor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    if(!isEditable) return;

                    final Feed t=G.feeds.get(getLayoutPosition());

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

                    RequestQueue requestQueue= Volley.newRequestQueue(context);
                    requestQueue.add(multiPartRequest);
                }
            });

            tbBookMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    if(!isEditable) return;

                    final Feed t=G.feeds.get(getLayoutPosition());

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

                    RequestQueue requestQueue= Volley.newRequestQueue(context);
                    requestQueue.add(multiPartRequest);
                }
            });




        }
    }
}
