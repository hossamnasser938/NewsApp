package com.example.hosam.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsFeed>>{

    static private final int NEWSFEED_LOADER_ID = 1;
    static private final String GUARDIAN_SAMPLE_URL = "https://content.guardianapis.com/search?q=%22salah%22&section=football&api-key=43a0b7b9-2ee9-4661-8ad9-2f7cfe258694";
    ProgressBar loadingProgressBar;
    TextView emptyListTextView;
    ListView newsListView;
    NewsFeedsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        emptyListTextView = findViewById(R.id.empty_list_view);
        newsListView = findViewById(R.id.news_list);

        adapter = new NewsFeedsAdapter(getApplicationContext(), new ArrayList<NewsFeed>());

        newsListView.setAdapter(adapter);
        newsListView.setEmptyView(emptyListTextView);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsFeed currentNewsFeed = adapter.getItem(position);
                Uri uri = Uri.parse(currentNewsFeed.getArticleUrl());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                if(browserIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(browserIntent);
                }
            }
        });

        if(isNetworkAvailable()){
            LoaderManager manager = getLoaderManager();
            manager.initLoader(NEWSFEED_LOADER_ID, null, this);
        }
        else{
            loadingProgressBar.setVisibility(View.GONE);
            emptyListTextView.setText(R.string.no_internet);
        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int id, Bundle args) {

        return new NewsFeedsLoader(this, GUARDIAN_SAMPLE_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> data) {

        adapter.clear();
        if(data != null && !data.isEmpty()){
            adapter.addAll(data);
        }
        loadingProgressBar.setVisibility(View.GONE);
        emptyListTextView.setText(R.string.no_news);

    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {

        adapter.clear();

    }
}
