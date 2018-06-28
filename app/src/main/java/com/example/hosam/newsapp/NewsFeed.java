package com.example.hosam.newsapp;

public class NewsFeed {

    private String articleTitle;
    private String sectionName;
    private String authorName;
    private String datePublished;
    private String articleUrl;

    public NewsFeed(String articleTitle, String sectionName, String authorName, String datePublished, String articleUrl){
        this.articleTitle = articleTitle;
        this.sectionName = sectionName;
        this.authorName = authorName;
        this.datePublished = datePublished;
        this.articleUrl = articleUrl;
    }


    public String getArticleTitle() {
        return articleTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getArticleUrl() {
        return articleUrl;
    }
}
