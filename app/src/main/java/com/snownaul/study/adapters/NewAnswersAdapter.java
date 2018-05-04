package com.snownaul.study.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.snownaul.study.R;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;

import java.util.ArrayList;

/**
 * Created by alfo6-11 on 2018-05-03.
 */

public class NewAnswersAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Answer> answers;
    int questionType;

    public NewAnswersAdapter(Context context, ArrayList<Answer> answers,int questionType) {
        this.context = context;
        this.answers = answers;
        this.questionType=questionType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.a1_answer,parent,false);
        VH vh=new VH(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH)holder;
        Answer t=answers.get(position);

        vh.cb.setChecked(t.isCorrect());

        if(position==0){
            vh.btnClear.setVisibility(View.GONE);
        }else {
            vh.btnClear.setVisibility(View.VISIBLE);
        }

        if(questionType==Question.TYPE_ORDER)
            vh.tvNum.setText(position+1+"");

        t.setSgOrder(position+1);

        vh.answer.setText(t.getAnswer());




    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    class VH extends RecyclerView.ViewHolder{

        CheckBox cb;
        TextView tvNum;
        EditText answer;
        ImageView btnClear;

        public VH(View itemView) {
            super(itemView);

            cb=itemView.findViewById(R.id.cb);
            tvNum=itemView.findViewById(R.id.tv_num);
            answer=itemView.findViewById(R.id.et_answer);
            btnClear=itemView.findViewById(R.id.btn_clear);


            if(answers.size()>1){

                answer.requestFocus();
            }


            switch (questionType){
                case Question.TYPE_ORDER:
                    tvNum.setVisibility(View.VISIBLE);

                case Question.TYPE_ONEANSWER:
                    cb.setVisibility(View.GONE);
                    break;

            }

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    answers.get(getLayoutPosition()).setCorrect(isChecked);
                }
            });

            answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    answers.get(getLayoutPosition()).setAnswer(s.toString());
                }
            });

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(answers.size()<=1) return;
                    answers.remove(getLayoutPosition());
                    NewAnswersAdapter.this.notifyItemRemoved(getLayoutPosition());
                    NewAnswersAdapter.this.notifyItemRangeChanged(getLayoutPosition(),answers.size()-getLayoutPosition());
                }
            });
        }
    }
}
