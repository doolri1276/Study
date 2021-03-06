package com.snownaul.study.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
import com.snownaul.study.Activities.FeedCommentActivity;
import com.snownaul.study.Activities.FeedDetailActivity;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.feed_classes.Comment;
import com.snownaul.study.feed_classes.SubComment;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by macbookair on 2018. 5. 19..
 */

public class FeedCommentAdapter extends RecyclerView.Adapter {

    Context context;

    boolean isEditable;

    public FeedCommentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.feed_comment,parent,false);
        VH holder=new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        isEditable=false;
        VH vh=(VH) holder;
        SubComment t=G.currentComment.getSubComments().get(position);

        Glide.with(context).load(t.getImgPath()).thumbnail(0.1f).into(vh.civProf);

        vh.tvNickname.setText(t.getNickname());
        vh.tvDate.setText(t.getDate());
        vh.tvComment.setText(t.getContents());
        vh.tbFavor.setChecked(t.isLiked());
        vh.tvLikedCnt.setText(t.getLikeCnt()+"");

        isEditable=true;
    }

    @Override
    public int getItemCount() {
        return G.currentComment.getSubComments().size();
    }

    class VH extends RecyclerView.ViewHolder{

        CircleImageView civProf;
        TextView tvNickname, tvDate;
        ImageView ivMenu;
        TextView tvComment;
        ToggleButton tbFavor;
        TextView tvLikedCnt;
        ToggleButton tbSubComment;
        TextView tvSubCommentCnt;


        public VH(View itemView) {
            super(itemView);

            civProf=itemView.findViewById(R.id.civ_prof);
            tvNickname=itemView.findViewById(R.id.tv_nickname);
            tvDate=itemView.findViewById(R.id.tv_date);
            ivMenu=itemView.findViewById(R.id.iv_menu);
            tvComment=itemView.findViewById(R.id.tv_comment);
            tbFavor=itemView.findViewById(R.id.tb_favor);
            tvLikedCnt=itemView.findViewById(R.id.tv_liked_cnt);
            tbSubComment=itemView.findViewById(R.id.tb_sub_comment);
            tvSubCommentCnt=itemView.findViewById(R.id.tv_sub_comment_cnt);

            tbSubComment.setVisibility(View.GONE);
            tvSubCommentCnt.setVisibility(View.GONE);

            tbFavor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    if(!isEditable) return;

                    final SubComment t=G.currentComment.getSubComments().get(getLayoutPosition());

                    String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Feed/subCommentFavorChanged.php";

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
                    multiPartRequest.addStringParam("subCommentID",t.getSubCommentID()+"");

                    RequestQueue requestQueue= Volley.newRequestQueue(context);
                    requestQueue.add(multiPartRequest);

                }
            });
        }
    }
}
