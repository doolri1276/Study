package com.snownaul.study.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.snownaul.study.R;
import com.snownaul.study.report_classes.Test;

import java.util.ArrayList;

/**
 * Created by alfo6-11 on 2018-05-29.
 */

public class StudyReportTestsAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Test> tests;

    public StudyReportTestsAdapter(Context context, ArrayList<Test> tests) {
        this.context = context;
        this.tests = tests;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=LayoutInflater.from(context).inflate(R.layout.study_report_tmp,parent,false);
        VH vh=new VH(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VH vh=(VH) holder;
        Test t=tests.get(position);

        vh.tvNo.setText(position+1+"");
        vh.tvDate.setText(t.getDate());
        vh.tvGrade.setText(t.getGrade());
        vh.tvScore.setText(t.getScore()+"");

        Log.i("MyTag","그렸다 : "+position);


    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView tvNo,tvDate,tvGrade,tvScore;

        public VH(View itemView) {
            super(itemView);

            tvNo=itemView.findViewById(R.id.tv_no);
            tvDate=itemView.findViewById(R.id.tv_date);
            tvGrade=itemView.findViewById(R.id.tv_grade);
            tvScore=itemView.findViewById(R.id.tv_score);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, getLayoutPosition()+"번째 눌림", Toast.LENGTH_SHORT).show();


                }
            });
        }
    }
}
