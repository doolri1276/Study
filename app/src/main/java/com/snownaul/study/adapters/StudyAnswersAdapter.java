package com.snownaul.study.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.snownaul.study.R;
import com.snownaul.study.controls.StudyingManager;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * Created by alfo6-11 on 2018-05-09.
 */

public class StudyAnswersAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Answer> answers;
    int questionType;
    int studyingMode;

    public StudyAnswersAdapter(Context context, ArrayList<Answer> answers, int questionType, int studyingMode) {
        this.context = context;
        this.answers = answers;
        this.questionType = questionType;
        this.studyingMode = studyingMode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.a3_answer,parent,false);
        VH vh=new VH(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH)holder;
        Answer t=answers.get(position);

        vh.tvAnswer.setText(t.getAnswer());

        if(studyingMode==StudyingManager.MODE_STUDYING){


        }else if(studyingMode==StudyingManager.MODE_ANSWER){
            vh.cb.setEnabled(false);
            if(t.isCorrect()){
                vh.tvAnswer.setTypeface(null, Typeface.BOLD);
                vh.tvAnswer.setTextColor(context.getResources().getColor(R.color.colorGreen));

                vh.cb.setVisibility(GONE);
                vh.cbRorW.setVisibility(View.VISIBLE);
                vh.cbRorW.setChecked(true);
            }else if(t.isChecked()){
                vh.tvAnswer.setTextColor(context.getResources().getColor(R.color.colorRed));

                vh.cb.setVisibility(GONE);
                vh.cbRorW.setVisibility(View.VISIBLE);

            }
            if(questionType==Question.TYPE_ONEANSWER) vh.cbRorW.setVisibility(GONE);

        }


        //else vh.cbRorW.setVisibility(View.VISIBLE);

//        if(questionType==Question.TYPE_ORDER){
//            vh.tvNum.setText(t.getSgOrder()+"");
//        }

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    class VH extends RecyclerView.ViewHolder{

        CheckBox cb,cbRorW;
        TextView tvNum;
        TextView tvAnswer;

        public VH(View itemView) {
            super(itemView);

            cb=itemView.findViewById(R.id.cb);
            cbRorW=itemView.findViewById(R.id.cb_rorw);
            tvNum=itemView.findViewById(R.id.tv_num);
            tvAnswer=itemView.findViewById(R.id.tv_answer);

            switch (questionType){
                case Question.TYPE_ORDER:
                    tvNum.setVisibility(View.VISIBLE);

                case Question.TYPE_ONEANSWER:
                    cb.setVisibility(GONE);
                    break;
            }

            cb.setEnabled(true);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    answers.get(getLayoutPosition()).setChecked(isChecked);
                }
            });

            tvAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(studyingMode==StudyingManager.MODE_STUDYING){
                        cb.setChecked(!cb.isChecked());
                    }
                }
            });



        }
    }

    public int getStudyingMode() {
        return studyingMode;
    }

    public void setStudyingMode(int studyingMode) {
        this.studyingMode = studyingMode;
    }
}
