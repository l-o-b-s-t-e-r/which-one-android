package com.android.project.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.model.Option;

import java.text.DecimalFormat;

import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by Lobster on 01.08.16.
 */
public class QuizViewBuilder {

    private static final String PERCENT_TEMPLATE = "0.00%";
    private static DecimalFormat mFormat = new DecimalFormat(PERCENT_TEMPLATE);

    public static Subscriber<Void> createFinalOption(RadioGroup radioGroup, Context context, Option option, final int allVotesCount, boolean userVote) {
        final int currentVotesCount = option.getVotes().size();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.quiz_option, null);

        final ProgressBar progressBar = ButterKnife.findById(view, R.id.progressBar);
        final TextView voteCount = ButterKnife.findById(view, R.id.vote_count);
        final TextView percent = ButterKnife.findById(view, R.id.percent);
        TextView title = ButterKnife.findById(view, R.id.title);

        title.setText(option.getOptionName());
        progressBar.setMax(allVotesCount);
        if (userVote) {
            progressBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
        }

        radioGroup.addView(view);

        return new Subscriber<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Void aVoid) {
                progressBar.setIndeterminate(false);

                progressBar.setProgress(currentVotesCount);
                voteCount.setText(String.valueOf(currentVotesCount));
                percent.setText(mFormat.format((float) currentVotesCount / allVotesCount));
            }
        };
    }

    public static View createFinalOption(Context context, Option option, final int allVotesCount, boolean votedByUser) {
        final int currentVotesCount = option.getVotes().size();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.quiz_option, null);

        final ProgressBar progressBar = ButterKnife.findById(view, R.id.progressBar);
        final TextView voteCount = ButterKnife.findById(view, R.id.vote_count);
        final TextView percent = ButterKnife.findById(view, R.id.percent);
        TextView title = ButterKnife.findById(view, R.id.title);

        progressBar.setIndeterminate(false);
        progressBar.setProgress(currentVotesCount);
        progressBar.setMax(allVotesCount);

        title.setText(option.getOptionName());
        voteCount.setText(String.valueOf(currentVotesCount));
        percent.setText(mFormat.format((float) currentVotesCount / allVotesCount));

        if (votedByUser) {
            progressBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
        }

        return view;
    }

    public static View createBaseOption(Context context, Option option, int id) {
        RadioButton radioButton = new RadioButton(context);
        radioButton.setText(option.getOptionName());
        radioButton.setId(id);

        return radioButton;
    }

    public static View createNewOption(Context context, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.quiz_new_option, null);

        EditText title = ButterKnife.findById(view, R.id.option_title);
        ImageView delete = ButterKnife.findById(view, R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeView(view);
            }
        });

        return view;
    }
}
