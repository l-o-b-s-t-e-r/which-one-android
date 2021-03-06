package com.android.project.view.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.User;
import com.android.project.util.SuggestionProvider;
import com.android.project.view.search.di.SearchModule;
import com.android.project.view.userpage.UserPageActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchPresenter.View {

    @BindView(R.id.search_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    SearchPresenter.ActionListener presenter;
    @Inject
    SearchRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_dialog);
        ButterKnife.bind(this);

        WhichOneApp.getUserComponent()
                .plus(new SearchModule(this))
                .inject(this);

        toolbar.setTitle(R.string.search_dialog_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
        recyclerViewAdapter.setSearchQuery(searchQuery);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(recyclerViewAdapter);

        SearchRecentSuggestions suggestions =
                new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
        suggestions.saveRecentQuery(searchQuery, null);

        presenter.loadUsers(searchQuery);
    }

    @Override
    public void showUsers(List<User> users) {
        recyclerViewAdapter.updateData(users);
    }

    @Override
    public void showUserPage(User user) {
        Intent intent = new Intent(this, UserPageActivity.class);
        intent.putExtra(getString(R.string.user_name_opened_page), user.getUsername());
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        throwable.printStackTrace();
    }
}
