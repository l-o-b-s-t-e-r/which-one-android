package com.android.project.util;

import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.project.R;
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

    private Context mContext;

    public QuizViewBuilder(Application application) {
        mContext = application.getApplicationContext();
    }

    public void createRadioOptions(RadioGroup radioGroup, Record record) {
        RadioButton radioButton;

        for (Option option : record.getOptions()) {
            radioButton = new RadioButton(mContext);
            radioButton.setText(option.getOptionName());
            radioButton.setId(radioGroup.getChildCount());

            radioButton.setTextColor(Color.rgb(51, 51, 51));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radioButton.setButtonTintList(ColorStateList.valueOf(Color.rgb(51, 51, 51)));
            }

            radioGroup.addView(radioButton);
        }
    }

    public List<ViewHolder> createVotedOptions(RadioGroup radioGroup, Record record) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<ViewHolder> viewHolderOptions = new ArrayList<>();
        Integer allVotesCount = record.getVoteCount();
        for (Option option : record.getOptions()) {
            View view = inflater.inflate(R.layout.voted_option_layout, null);

            ViewHolder viewHolder = new ViewHolder(view, option);
            viewHolder.setContent(record, allVotesCount);
            viewHolderOptions.add(viewHolder);

            radioGroup.addView(view, radioGroup.getChildCount());
        }

        return viewHolderOptions;
    }

    public List<ViewHolder> createProgressOption(final RadioGroup radioGroup, final Record record) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<ViewHolder> viewHolderOptions = new ArrayList<>();
        for (Option option : record.getOptions()) {
            View view = inflater.inflate(R.layout.voted_option_layout, null);
            ViewHolder viewHolder = new ViewHolder(view, option);
            viewHolderOptions.add(viewHolder);
            radioGroup.addView(view, radioGroup.getChildCount());
        }

        return viewHolderOptions;
    }

    public View createNewOption(ViewGroup parent, Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.new_option_layout, null);

        ImageView delete = ButterKnife.findById(view, R.id.delete);

        delete.setOnClickListener(v -> {
            parent.removeView(view);
        });

        return view;
    }

    public class ViewHolder {

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.vote_count)
        TextView voteCount;
        @BindView(R.id.percent)
        TextView percent;
        @BindView(R.id.option_name)
        TextView optionName;

        private Option mOption;

        public ViewHolder() {

        }

        public ViewHolder(View view, Option option) {
            ButterKnife.bind(this, view);

            progressBar.getProgressDrawable().setColorFilter(Color.rgb(148, 148, 148), PorterDuff.Mode.SRC_IN);
            progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(148, 148, 148), PorterDuff.Mode.SRC_IN);

            mOption = option;
            optionName.setText(option.getOptionName());
        }

        public void setContent(Record record, int allVotesCount) {
            Integer optionVotesCount = record.getOption(mOption.getOptionName()).getVoteCount();

            progressBar.setIndeterminate(false);
            progressBar.setProgress(optionVotesCount);
            progressBar.setMax(allVotesCount);

            voteCount.setText(String.valueOf(optionVotesCount));
            percent.setText(FORMAT.format((float) optionVotesCount / allVotesCount));

            if (mOption.getOptionName().equals(record.getSelectedOption())) {
                progressBar.getProgressDrawable().setColorFilter(Color.rgb(225, 0, 86), PorterDuff.Mode.SRC_IN);
            }
        }

    }
}
