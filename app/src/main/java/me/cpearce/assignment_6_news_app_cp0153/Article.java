package me.cpearce.assignment_6_news_app_cp0153;

/**
 * Created by Christopher on 10/19/2017.
 */

public class Article {
    private String mAuthor;
    private String mTitle;
    private String mDescription;
    private String mUrl;
    private String mUrlToImage;
    private String mPublishedAt;
    private String mEntities; // comma separated list of entities detected by the natural language api

    /**
     * Constructs a new {@link Article} object.
     *
     * @param author      article author
     * @param title       article title
     * @param description article description
     * @param url         is the article url
     * @param urlToImage  is the image url
     * @param publishedAt is the date and time the article was published
     */
    public Article(String author, String title, String description, String url, String urlToImage,
                   String publishedAt, String entities) {
        mAuthor = author;
        mTitle = title;
        mDescription = description;
        mUrl = url;
        mUrlToImage = urlToImage;
        mPublishedAt = publishedAt;
        mEntities = entities;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmUrlToImage() {
        return mUrlToImage;
    }

    public String getmPublishedAt() {
        return mPublishedAt;
    }

    public String getmEntities() {
        return mEntities;
    }

    public String setmEntites(String entities) {return mEntities = entities; }
}
