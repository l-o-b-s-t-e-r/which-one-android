package com.android.project.util;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Lobster on 03.08.16.
 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.android.project.util.SuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
