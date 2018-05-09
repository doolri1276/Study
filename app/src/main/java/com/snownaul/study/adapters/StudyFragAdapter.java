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
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.snownaul.study.Activities.AddSetActivity;
import com.snownaul.study.Activities.MainActivity;
import com.snownaul.study.Activities.StudySetMainActivity;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.study_classes.StudySet;

/**
 * Created by alfo6-11 on 2018-05-08.
 */

public class StudyFragAdapter extends RecyclerView.Adapter {

    Context context;

    public StudyFragAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.study_setlayout,parent,false);
        VH holder=new VH(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH) holder;
        StudySet t=G.studySets.get(position);

        t.setSetting(true);

        vh.tvTitle.setText(t.getSgSet().getTitle());
        vh.tvRecentDate.setText(t.getRecentDate());
        Log.i("MyTag","Like check : "+t.isLiked());
        vh.tbFavor.setChecked(t.isLiked());
        vh.tvLiked.setText(t.getSgSet().getLikeCnt()+"");
        vh.tvTriedCnt.setText(t.getTriedCnt()+"");

        t.setSetting(false);
    }

    @Override
    public int getItemCount() {
        return G.studySets.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView tvTitle,tvRecentDate;
        ToggleButton tbFavor;
        TextView tvLiked,tvTriedCnt;
        ImageView btnDetail;


        public VH(View itemView) {
            super(itemView);

            tvTitle=itemView.findViewById(R.id.tv_title);
            tvRecentDate=itemView.findViewById(R.id.tv_recent_date);
            tbFavor=itemView.findViewById(R.id.tb_favor);
            tvLiked=itemView.findViewById(R.id.tv_liked);
            tvTriedCnt=itemView.findViewById(R.id.tv_tried_cnt);
            btnDetail=itemView.findViewById(R.id.btn_detail);

            tbFavor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    final StudySet t=G.studySets.get(getLayoutPosition());

                    if(t.isSetting()) return;
                    String serverUrl="http://snownaul2.dothome.co.kr/StudyGuide/Set/studySetFavorChanged.php";

                    SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("MyTag","likeeeee checkkkkk : "+response);
                            t.setLiked(isChecked);
                            if(isChecked){
                                t.getSgSet().setLikeCnt(t.getSgSet().getLikeCnt()+1);
                            }else {
                                t.getSgSet().setLikeCnt(t.getSgSet().getLikeCnt()-1);
                            }

                            tvLiked.setText(t.getSgSet().getLikeCnt()+"");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            tbFavor.setChecked(!isChecked);

                        }
                    });

                    if(isChecked){
                        multiPartRequest.addStringParam("sgSetLike",isChecked+"");
                    }else{
                        multiPartRequest.addStringParam("sgSetLike","");
                    }

                    //multiPartRequest.addStringParam("sgSetLike",isChecked+"");
                    multiPartRequest.addStringParam("userID",G.getUserId()+"");
                    multiPartRequest.addStringParam("sgSetID",t.getSgSet().getSgSetID()+"");

                    RequestQueue requestQueue= Volley.newRequestQueue(context);
                    requestQueue.add(multiPartRequest);


                }
            });

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    G.currentStudySet=G.studySets.get(getLayoutPosition());
                    SwipeBackActivityHelper.startSwipeActivity((MainActivity)context,new Intent(context,StudySetMainActivity.class),true,true,true);

                }
            });
        }
    }
}
