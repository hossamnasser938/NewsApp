package com.example.hosam.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

public class NewsFeedsLoader extends AsyncTaskLoader<List<NewsFeed>> {

    private String url;

    public NewsFeedsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsFeed> loadInBackground() {

        if(url == null || TextUtils.isEmpty(url)){
            return null;
        }

        return NewsQueryHelper.fetchNewsFeeds(url);

    }
}
