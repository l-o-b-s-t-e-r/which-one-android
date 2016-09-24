package com.android.project.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import com.android.project.R;
import com.android.project.cofig.WhichOneApp;
import com.android.project.model.User;
import com.android.project.userpage.UserPageActivity;
import com.android.project.util.SuggestionProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchPresenter.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String mUsername;
    private SearchRecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
        mUsername = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.user_name), "");

        SearchPresenter.ActionListener actionListener = new SearchPresenterImpl(this, ((WhichOneApp) getApplication()).getMainComponent());
        mRecyclerViewAdapter = new SearchRecyclerViewAdapter(this, searchQuery, actionListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mRecyclerViewAdapter);

        actionListener.loadUsers(searchQuery);

        SearchRecentSuggestions suggestions =
                new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
        suggestions.saveRecentQuery(searchQuery, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showUsers(List<User> users) {
        mRecyclerViewAdapter.updateData(users);
    }

    @Override
    public void showUserPage(User user) {
        Intent intent = new Intent(this, UserPageActivity.class);
        intent.putExtra(getString(R.string.user_name_opened_page), user.getName());
        startActivity(intent);
    }
}
