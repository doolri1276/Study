package com.snownaul.study.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.snownaul.study.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by macbookair on 2018. 5. 19..
 */

public class FeedCommentAdapter extends RecyclerView.Adapter {

    Context context;



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class VH extends RecyclerView.ViewHolder{

        CircleImageView civProf;
        TextView tvNickname, tvDate;
        ImageView ivMenu;
        TextView tvComment;
        ToggleButton tbFavor;
        TextView tvLikedCnt;
        ToggleButton tbComment;
        TextView tvCommentCnt;


        public VH(View itemView) {
            super(itemView);

            civProf=itemView.findViewById(R.id.civ_prof);
            tvNickname=itemView.findViewById(R.id.tv_nickname);
            tvDate=itemView.findViewById(R.id.tv_date);
            ivMenu=itemView.findViewById(R.id.iv_menu);
            tvComment=itemView.findViewById(R.id.tv_comment);
            
        }
    }
}
