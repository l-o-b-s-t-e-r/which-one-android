package com.android.project.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.Option;
import com.android.project.model.Record;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lobster on 01.08.16.
 */

public class QuizViewBuilder {

    private static final String TAG = QuizViewBuilder.class.getSimpleName();
    private static final String PERCENT_TEMPLATE = "0.00%";
    private static final DecimalFormat FORMAT = new DecimalFormat(PERCENT_TEMPLATE);

    private static QuizViewBuilder mQuizViewBuilder;

    private QuizViewBuilder() {

    }

    public static QuizViewBuilder getInstance() {
        if (mQuizViewBuilder == null) {
            mQuizViewBuilder = new QuizViewBuilder();
        }

        return mQuizViewBuilder;
    }

    public void createRadioOptions(RadioGroup radioGroup, Record record) {
        RadioButton radioButton;

        for (Option option : record.getOptions()) {
            radioButton = new RadioButton(WhichOneApp.getContext());
            radioButton.setText(option.getOptionName());
            radioButton.setId(radioGroup.getChildCount());

            radioGroup.addView(radioButton);
        }
    }

    public void createVotedOptions(RadioGroup radioGroup, Record record, String username) {
        LayoutInflater inflater = (LayoutInflater) WhichOneApp.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Option option : record.getOptions()) {
            View view = inflater.inflate(R.layout.quiz_option, null);

            ViewHolder viewHolder = new ViewHolder(view, option, username);
            viewHolder.setContent(record);

            radioGroup.addView(view, radioGroup.getChildCount());
        }
    }

    public List<ViewHolder> createProgressOption(final RadioGroup radioGroup, final Record record, final String username) {
        LayoutInflater inflater = (LayoutInflater) WhichOneApp.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final List<ViewHolder> viewHolderOptions = new ArrayList<>();
        for (Option option : record.getOptions()) {
            View view = inflater.inflate(R.layout.quiz_option, null);
            ViewHolder viewHolder = new ViewHolder(view, option, username);
            viewHolderOptions.add(viewHolder);
            radioGroup.addView(view, radioGroup.getChildCount());
        }

        return viewHolderOptions;
    }

    public View createNewOption(final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) WhichOneApp.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.quiz_new_option, null);

        ImageView delete = ButterKnife.findById(view, R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeView(view);
            }
        });

        return view;
    }

    public class ViewHolder {

        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.vote_count)
        TextView voteCount;
        @BindView(R.id.percent)
        TextView percent;
        @BindView(R.id.option_name)
        TextView optionName;

        private String mUsername;
        private Option mOption;

        public ViewHolder() {

        }

        public ViewHolder(View view, Option option, String username) {
            ButterKnife.bind(this, view);

            mUsername = username;
            mOption = option;
            optionName.setText(option.getOptionName());
        }

        public void setContent(Record record) {
            Integer optionVotesCount = mOption.getVotes().size();
            Integer allVotesCount = record.getAllVotes().size();

            progressBar.setIndeterminate(false);
            progressBar.setProgress(optionVotesCount);
            progressBar.setMax(allVotesCount);

            voteCount.setText(String.valueOf(optionVotesCount));
            percent.setText(FORMAT.format((float) optionVotesCount / allVotesCount));

            if (mOption.getVotes().contains(mUsername)) {
                progressBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
            }
        }

    }
}
