package com.example.instanthelp;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListAdapter extends ArrayAdapter {



    private Activity mContext;
    List<Questions> questionsList;

    public ListAdapter(Activity mContext, List<Questions> questionsList)  {
        super(mContext, R.layout.questions_asked, questionsList);
        this.mContext = mContext;
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.questions_asked, null, true);

        TextView tvQuestion = listItemView.findViewById(R.id.tvQuestions);
        TextView tvAnswer = listItemView.findViewById(R.id.tvAnswer);

        Questions questions = questionsList.get(position);

        tvQuestion.setText(questions.getQuestion());
        //tvAnswer.setText(questions.getAnswer());
        return listItemView;
        // super.getView(position, convertView, parent);
    }

}
