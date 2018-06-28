package com.example.hosam.newsapp;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsFeedsAdapter extends ArrayAdapter<NewsFeed> {

    public NewsFeedsAdapter(Context context, List<NewsFeed> newsFeeds) {
        super(context, 0, newsFeeds);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_feed_item, parent, false);
        }

        NewsFeed currentNewsFeed = getItem(position);

        TextView articleTitleView = listItemView.findViewById(R.id.article_title_text);
        TextView sectionNameView = listItemView.findViewById(R.id.section_name_text);
        TextView authorNameView = listItemView.findViewById(R.id.author_name_text);
        TextView datePublishedView = listItemView.findViewById(R.id.date_published_text);

        String articleTitle = currentNewsFeed.getArticleTitle();
        String sectionName = currentNewsFeed.getSectionName();
        String authorName = currentNewsFeed.getAuthorName();
        String datePublished = currentNewsFeed.getDatePublished();

        articleTitleView.setPaintFlags(articleTitleView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        articleTitleView.setText(articleTitle);
        sectionNameView.setText(sectionName);

        if(authorName == null || TextUtils.isEmpty(authorName)){
            authorNameView.setText(R.string.no_author);
        }else{
            authorNameView.setText(authorName);
        }

        if(datePublished == null || TextUtils.isEmpty(datePublished)){
            datePublishedView.setText(R.string.no_date);
        }else{
            datePublishedView.setText(datePublished);
        }

        return listItemView;

    }

}
