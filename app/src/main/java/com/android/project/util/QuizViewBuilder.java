package com.android.project.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.model.Option;

import java.text.DecimalFormat;

import butterknife.ButterKnife;

/**
 * Created by Lobster on 01.08.16.
 */
public class QuizViewBuilder {

    private static final String PERCENT_TEMPLATE = "0.00%";
    private static DecimalFormat mFormat = new DecimalFormat(PERCENT_TEMPLATE);

    public static View createFinalOption(Context context, Option option, int allVotesCount) {
        int currentVotesCount = option.getVotes().size();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.quiz_option, null);

        ProgressBar progressBar = ButterKnife.findById(view, R.id.progressBar);
        TextView title = ButterKnife.findById(view, R.id.title);
        TextView voteCount = ButterKnife.findById(view, R.id.vote_count);
        TextView percent = ButterKnife.findById(view, R.id.percent);

        progressBar.setMax(allVotesCount);
        progressBar.setProgress(currentVotesCount);

        title.setText(option.getOptionName());
        voteCount.setText(String.valueOf(currentVotesCount));
        percent.setText(mFormat.format((float) currentVotesCount / allVotesCount));

        return view;
    }

    public static View createBaseOption(Context context, Option option, int id) {
        RadioButton radioButton = new RadioButton(context);
        radioButton.setText(option.getOptionName());
        radioButton.setId(id);

        return radioButton;
    }

    public static View createNewOption(Context context, final ViewGroup parent, String optionTitle) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.quiz_new_option, null);

        EditText title = ButterKnife.findById(view, R.id.option_title);
        ImageView delete = ButterKnife.findById(view, R.id.delete);

        title.setText(optionTitle);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeView(view);
            }
        });

        return view;
    }
}
