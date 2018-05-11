package com.snownaul.study.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;

/**
 * Created by alfo6-11 on 2018-05-03.
 */

public class UpdateQuestionsAdapter extends RecyclerView.Adapter {

    Context context;

    UpdateAnswersAdapter updateAnswersAdapter;

    boolean updatable;

    public UpdateQuestionsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.q1_question,parent,false);
        VH holder=new VH(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        updatable=false;
        VH vh=(VH) holder;
        Question t= G.updateSgSet.getQuestions().get(position);

        if(t.getQuestionType()==Question.TYPE_ONEANSWER){
            vh.addAnswer.setVisibility(View.GONE);
        }else {
            vh.addAnswer.setVisibility(View.VISIBLE);
        }

        if(t.getQuestionType()==Question.TYPE_RIGHTORWRONG){
            vh.type2.setVisibility(View.VISIBLE);
            vh.tbRorW.setChecked(t.isRightOrWrong());
        }else {
            vh.type2.setVisibility(View.GONE);
        }


        vh.tvQuestionNo.setText(position+1+"");
        vh.tvQuestionType.setText(R.string.q_type_1+t.getQuestionType());
        vh.etQuestion.setText(t.getQuestion());

        updateAnswersAdapter =new UpdateAnswersAdapter(context,t.getAnswers(),t.getQuestionType());
        vh.recyclerView.setAdapter(updateAnswersAdapter);

        vh.etExplanation.setText(t.getExplanation());


        updatable=true;
    }

    @Override
    public int getItemCount() {
        return G.updateSgSet.getQuestions().size();
    }

    class VH extends RecyclerView.ViewHolder{

        RelativeLayout layoutQuestion;
        TextView tvQuestionNo;
        TextView tvQuestionType;
        ImageView btnClear;
        LinearLayout type2;
        ToggleButton tbRorW;
        EditText etQuestion;
        RecyclerView recyclerView;
        RelativeLayout addAnswer;
        EditText etExplanation;


        public VH(View itemView) {
            super(itemView);

            layoutQuestion=itemView.findViewById(R.id.layout_question);
            tvQuestionNo=itemView.findViewById(R.id.tv_question_no);
            tvQuestionType=itemView.findViewById(R.id.tv_question_type);
            btnClear=itemView.findViewById(R.id.btn_clear);
            type2=itemView.findViewById(R.id.type2);
            tbRorW=itemView.findViewById(R.id.btn_rorw);
            etQuestion=itemView.findViewById(R.id.et_question);
            recyclerView=itemView.findViewById(R.id.recycler);
            addAnswer=itemView.findViewById(R.id.add_answer);
            etExplanation=itemView.findViewById(R.id.et_explanation);

            if(G.updateSgSet.getQuestions().size()>1)
                etQuestion.requestFocus();

            //해당 문제를 삭제할때..
            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!updatable)return;
                    Question t=G.updateSgSet.getQuestions().get(getLayoutPosition());
                    t.setUpdateState(Question.MODE_DELETED);
                    if(t.getQuestionID()!=0)G.deletedQuestions.add(t);
                    Log.i("MyTag",t.getQuestionID()+"번 Question DELETED");

                    G.updateSgSet.getQuestions().remove(getLayoutPosition());
                    UpdateQuestionsAdapter.this.notifyItemRemoved(getLayoutPosition());
                    UpdateQuestionsAdapter.this.notifyItemRangeChanged(getLayoutPosition(),G.updateSgSet.getQuestions().size()-getLayoutPosition());

                }
            });

            tbRorW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!updatable)return;

                    Question t=G.updateSgSet.getQuestions().get(getLayoutPosition());

                    if(t.getUpdateState()!=Question.MODE_ADDED)t.setUpdateState(Question.MODE_UPDATED);
                    t.setRightOrWrong(isChecked);
                }
            });

            etQuestion.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!updatable)return;
                    Question t=G.updateSgSet.getQuestions().get(getLayoutPosition());
                    if(t.getUpdateState()!=Question.MODE_ADDED){
                        t.setUpdateState(Question.MODE_UPDATED);
                        Log.i("MyTag",t.getQuestionID()+"번 Question question UPDATED============");
                    }
                    t.setQuestion(s.toString());
                }
            });

            addAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!updatable)return;
                    Question t=G.updateSgSet.getQuestions().get(getLayoutPosition());

                    Answer a=new Answer();
                    a.setQuestionID(t.getQuestionID());




                    if(t.getQuestionType()==Question.TYPE_ORDER)
                        a.setCorrect(true);
                    Log.i("MyTag",t.getQuestionID()+"번 Question ADDED NEW ANSWER");

                    if(t.getUpdateState()==Question.MODE_ADDED)
                        a.setUpdateState(Answer.MODE_ADDED_BYQUESTION);
                    else {
                        a.setUpdateState(Answer.MODE_ADDED);

                    }

                    t.getAnswers().add(a);

                    updateAnswersAdapter.notifyItemInserted(t.getAnswers().size()-1);
                    recyclerView.scrollToPosition(t.getAnswers().size()-1);
                }
            });

            etExplanation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!updatable)return;
                    Question t=G.updateSgSet.getQuestions().get(getLayoutPosition());

                    if(t.getUpdateState()!=Question.MODE_ADDED){
                        t.setUpdateState(Question.MODE_UPDATED);
                        Log.i("MyTag",t.getQuestionID()+"번 Question explanationUPDATED============");
                    }

                    t.setExplanation(s.toString());
                }
            });
        }
    }
}
