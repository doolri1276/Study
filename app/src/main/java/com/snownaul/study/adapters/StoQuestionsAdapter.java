package com.snownaul.study.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.bumptech.glide.Glide;
import com.snownaul.study.Activities.StudyStorageActivity;
import com.snownaul.study.G;
import com.snownaul.study.R;
import com.snownaul.study.study_classes.Answer;
import com.snownaul.study.study_classes.Question;

import java.util.ArrayList;

/**
 * Created by alfo6-11 on 2018-05-09.
 */

public class StoQuestionsAdapter extends RecyclerView.Adapter {

    Context context;
    StudyStorageActivity studyStorageActivity;

    ArrayList<Question> currentQuestions=G.currentStudySet.getSgSet().getQuestions();

    StoAnswersAdapter stoAnswersAdapter;

    public StoQuestionsAdapter(Context context) {
        this.context = context;
        this.studyStorageActivity= (StudyStorageActivity) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.q2_question,parent,false);
        VH holder=new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH) holder;
        Question t=currentQuestions.get(position);

        vh.tbFavor.setChecked(t.isLiked());
        vh.tvQuestionNo.setText(position+1+"");
        vh.tvQuestionType.setText(R.string.q_type_1+t.getQuestionType());

        if(t.getQuestionType()==Question.TYPE_RIGHTORWRONG){
            vh.type2.setVisibility(View.VISIBLE);
            vh.tbRorW.setChecked(t.isRightOrWrong());
        }else {
            vh.type2.setVisibility(View.GONE);
        }

        if(t.getQuestionPic()==null||t.getQuestionPic().length()==0){
            vh.questionPic.setVisibility(View.GONE);
        }else{
            vh.questionPic.setVisibility(View.VISIBLE);
            Glide.with(context).load(t.getQuestionPic()).thumbnail(0.1f).into(vh.questionPic);
        }

        vh.etQuestion.setText(t.getQuestion());

        stoAnswersAdapter= new StoAnswersAdapter(context,t.getAnswers(),t.getQuestionType(),t.isEditMode());
        vh.recyclerView.setAdapter(stoAnswersAdapter);

        if(t.getExplanation()==null||t.getExplanation().length()==0||t.getExplanation().equals("null")){
            //vh.etExplanation.setVisibility(View.GONE);
            vh.etExplanation.setText("");
        }else{
            //vh.etExplanation.setVisibility(View.VISIBLE);
            vh.etExplanation.setText(t.getExplanation());
        }


        if(t.isEditMode()){
            vh.etQuestion.setEnabled(true);
            vh.tbRorW.setEnabled(true);
            vh.etExplanation.setEnabled(true);
            vh.btnDone.setVisibility(View.VISIBLE);
            vh.btnEdit.setVisibility(View.GONE);
            vh.addAnswer.setVisibility(View.VISIBLE);

        }else{
            vh.etQuestion.setEnabled(false);
            vh.tbRorW.setEnabled(false);
            vh.etExplanation.setEnabled(false);
            vh.btnDone.setVisibility(View.GONE);
            vh.btnEdit.setVisibility(View.VISIBLE);
            vh.addAnswer.setVisibility(View.GONE);


        }




    }

    @Override
    public int getItemCount() {
        return currentQuestions.size();
    }

    class VH extends RecyclerView.ViewHolder{

        ToggleButton tbFavor;
        TextView tvQuestionNo;
        TextView tvQuestionType;
        ImageView btnEdit;
        ImageView btnDone;
        ImageView btnClear;
        LinearLayout type2;
        ToggleButton tbRorW;
        TextView etQuestion;
        RecyclerView recyclerView;
        RelativeLayout addAnswer;
        TextView etExplanation;
        ImageView questionPic, ivCamera,ivPhoto;


        public VH(View itemView) {
            super(itemView);

            tbFavor=itemView.findViewById(R.id.tb_favor);
            tvQuestionNo=itemView.findViewById(R.id.tv_question_no);
            tvQuestionType=itemView.findViewById(R.id.tv_question_type);
            btnEdit=itemView.findViewById(R.id.btn_edit);
            btnDone=itemView.findViewById(R.id.btn_done);
            btnClear=itemView.findViewById(R.id.btn_clear);
            type2=itemView.findViewById(R.id.type2);
            tbRorW=itemView.findViewById(R.id.tb_rorw);
            etQuestion=itemView.findViewById(R.id.et_question);
            recyclerView=itemView.findViewById(R.id.recycler);
            addAnswer=itemView.findViewById(R.id.add_answer);
            etExplanation=itemView.findViewById(R.id.et_explanation);
            questionPic=itemView.findViewById(R.id.iv_questionpic);
            ivCamera=itemView.findViewById(R.id.iv_camera);
            ivPhoto=itemView.findViewById(R.id.iv_photo);

//            if(G.currentStudySet.studyingMode()&&G.currentStudySet.getSgSet().getQuestions().size()>1)
//                etQuestion.requestFocus();

            ivCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    studyStorageActivity.clickCamera(getLayoutPosition());
                }
            });

            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    studyStorageActivity.clickPhoto(getLayoutPosition());
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {//EditMode로 전환...
                @Override
                public void onClick(View v) {
                    currentQuestions.get(getLayoutPosition()).setEditMode(true);
                    StoQuestionsAdapter.this.notifyItemChanged(getLayoutPosition());
                    //etExplanation.setVisibility(View.VISIBLE);
                    etExplanation.setEnabled(true);
                    etQuestion.setEnabled(true);

                    stoAnswersAdapter.isEditMode=true;
                    stoAnswersAdapter.notifyDataSetChanged();
                }
            });

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO: 서버에 수정한 내용을 올림...

                    Question t=currentQuestions.get(getLayoutPosition());
                    etQuestion.setEnabled(false);
                    etExplanation.setEnabled(false);

                    t.setEditMode(false);
                    StoQuestionsAdapter.this.notifyItemChanged(getLayoutPosition());

                    if(t.getExplanation()==null||t.getExplanation().length()==0){
                        //etExplanation.setVisibility(View.GONE);
                    }else{
                        //etExplanation.setVisibility(View.VISIBLE);
                        etExplanation.setText(t.getExplanation());
                    }

                    stoAnswersAdapter.isEditMode=false;
                    stoAnswersAdapter.notifyDataSetChanged();

                }
            });

            //해당 문제를 삭제할때..
            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //editMode일때 아닐때 따로따로 해야함...

                    if(etQuestion.getText().toString()==null||etQuestion.getText().toString().length()==0){
                        //TODO:지운다고 서버에 보내야 함. 성사되면 지워짐....
                        currentQuestions.remove(getLayoutPosition());
                        StoQuestionsAdapter.this.notifyItemRemoved(getLayoutPosition());
                        StoQuestionsAdapter.this.notifyItemRangeChanged(getLayoutPosition(),G.currentStudySet.getSgSet().getQuestions().size()-getLayoutPosition());
                    }else{
                        //TODO: AlertDialoig로 진짜 지울건지 물어봐야 함.

                    }

                }
            });



            tbRorW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Question t= G.currentStudySet.getSgSet().getQuestions().get(getLayoutPosition());
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
                    Question t=G.currentStudySet.getSgSet().getQuestions().get(getLayoutPosition());
                    t.setQuestion(s.toString());
                }
            });

            addAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Question t=currentQuestions.get(getLayoutPosition());
                    t.getAnswers().add(new Answer());
                    if(t.getQuestionType()==Question.TYPE_ORDER)
                        t.getAnswers().get(t.getAnswers().size()-1).setCorrect(true);

                    //StoAnswersAdapter.notifyItemInserted(t.getAnswers().size()-1);
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
                    Question t=G.currentStudySet.getSgSet().getQuestions().get(getLayoutPosition());
                    t.setExplanation(s.toString());
                }
            });


        }
    }
}
