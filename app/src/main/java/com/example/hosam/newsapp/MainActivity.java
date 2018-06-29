package com.example.hosam.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsFeed>>{

    static private final int NEWSFEED_LOADER_ID = 1;
    static private final String GUARDIAN_SAMPLE_URL = "https://content.guardianapis.com/search";
    static private final String MY_API_KEY = "43a0b7b9-2ee9-4661-8ad9-2f7cfe258694";
    static public boolean PREF_CHANGED = false;
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
    protected void onResume() {
        super.onResume();
        if(PREF_CHANGED){
            getLoaderManager().restartLoader(NEWSFEED_LOADER_ID, null, this);
            PREF_CHANGED = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.settings_main_menu_item){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String category = sharedPrefs.getString(
                        getString(R.string.settings_section_key),
                        getString(R.string.settings_section_default));

        String topic = sharedPrefs.getString(
                getString(R.string.settings_topic_key),
                getString(R.string.settings_topic_default));

        Uri baseUri = Uri.parse(GUARDIAN_SAMPLE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("show-fields", "byline");
        uriBuilder.appendQueryParameter("api-key", MY_API_KEY);
        uriBuilder.appendQueryParameter(getString(R.string.settings_section_key), category);
        uriBuilder.appendQueryParameter(getString(R.string.settings_topic_key), topic);

        return new NewsFeedsLoader(this, uriBuilder.toString());

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
