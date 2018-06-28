package com.example.hosam.newsapp;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsQueryHelper {

    public NewsQueryHelper(){
    }

    static public List<NewsFeed> fetchNewsFeeds(String urlStr){

        if(urlStr == null || TextUtils.isEmpty(urlStr)){
            return null;
        }

        URL url = createURL(urlStr);

        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<NewsFeed> newsFeeds = extractNewsFeedsFromJson(jsonResponse);

        return newsFeeds;

    }

    static private URL createURL(String urlStr){

        if(urlStr == null || TextUtils.isEmpty(urlStr)){
            return null;
        }

        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }

    static private String makeHTTPRequest(URL url) throws IOException{

        if(url == null){
            return null;
        }

        String jsonResponse = null;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000 /*milliseconds*/);
            httpURLConnection.setConnectTimeout(15000 /*milliseconds*/);
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    static private String readFromStream(InputStream inputStream) throws IOException{

        StringBuilder builder = null;

        if(inputStream != null){
            builder = new StringBuilder();
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }

        return builder.toString();

    }

    static private List<NewsFeed> extractNewsFeedsFromJson(String jsonResponse){

        if(jsonResponse == null || TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        List<NewsFeed> newsFeeds = new ArrayList<>();

        try {
            JSONObject responseJsonObject = new JSONObject(jsonResponse);
            JSONArray results = responseJsonObject.getJSONArray("results");
            JSONObject newsFeedJsonObject;
            String articleTitle;
            String sectionName;
            //String authorName;
            String datePublished;
            String articleUrl;
            for(int i = 0, k = results.length(); i < k; i++){
                newsFeedJsonObject = results.getJSONObject(i);
                articleTitle = newsFeedJsonObject.optString("webTitle");
                sectionName = newsFeedJsonObject.optString("sectionName");
                //authorName = newsFeedJsonObject.optString();
                datePublished = newsFeedJsonObject.optString("webPublicationDate");
                articleUrl = newsFeedJsonObject.optString("webUrl");
                newsFeeds.add(new NewsFeed(articleTitle, sectionName, null, datePublished, articleUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsFeeds;

    }

}
