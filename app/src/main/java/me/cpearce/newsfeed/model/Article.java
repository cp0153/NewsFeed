package me.cpearce.newsfeed.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Christopher on 10/19/2017.
 * model for Articles
 */

public class Article {
    private Map mSource;
    private String mAuthor;
    private String mTitle;
    private String mDescription;
    private String mUrl;
    private String mUrlToImage;
    private String mPublishedAt;

    /**
     * Constructs a new {@link Article} object.
     *
     * @param source      article source The identifier id and a display name name for the source
     *                    this article came from.
     * @param author      article author
     * @param title       article title
     * @param description article description
     * @param url         is the article url
     * @param urlToImage  is the image url
     * @param publishedAt is the date and time the article was published
     */
    public Article(Map source, String author, String title, String description, String url, String urlToImage,
                   String publishedAt) {
        mSource = source;
        mAuthor = author;
        mTitle = title;
        mDescription = description;
        mUrl = url;
        mUrlToImage = urlToImage;
        mPublishedAt = publishedAt;
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

    public Map getmSource() {
        return mSource;
    }

//    public String getmEntities() {
//        return mEntities;
//    }

//    public String setmEntites(String entities) {
//        return mEntities = entities;
//    }


}
