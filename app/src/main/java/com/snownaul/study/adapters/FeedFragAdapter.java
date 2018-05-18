package com.snownaul.study.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
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
        Feed t= G.currentFeeds.get(position);
        Log.i("MyTag","지금 "+position+"번째꺼 만들었당 : "+t.getContents());

        isEditable=false;

        int i=vh.itemView.getVisibility();
        Log.i("MyTag",("보여지고 있나?"+i));
        Glide.with(context).load(t.getProfImgPath()).into(vh.civProf);

        vh.tvNickname.setText(t.getNickname());
        vh.tvDate.setText(t.getDate());

        if(t.getImgPath()!=null||t.getImgPath().length()==0){
            vh.ivImg.setVisibility(View.VISIBLE);
            Glide.with(context).load(t.getImgPath()).into(vh.ivImg);
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
        return G.currentFeeds.size();
    }

    public class VH extends RecyclerView.ViewHolder{

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






        }
    }
}
