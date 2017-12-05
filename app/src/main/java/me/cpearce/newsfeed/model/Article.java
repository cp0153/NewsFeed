package me.cpearce.newsfeed.model;

import java.util.List;

/**
 * Created by Christopher on 10/19/2017.
 * Article model
 */

public class Article {
    public final String sourceId;
    public final String sourceName;
    public final String author;
    public final String title;
    public final String description;
    public final String url;
    public final String urlToImage;
    public final String publishedAt;
    public final List<Entity> entitiesList;

    /**
     * Constructs a new {@link Article} object.
     * reference here: https://newsapi.org/docs/endpoints/top-headlines and https://newsapi.org/docs/endpoints/everything
     * @param sourceId   article source The identifier id
     * @param sourceName article source name
     * @param author      article author
     * @param title       article title
     * @param description article description
     * @param url         is the article url
     * @param urlToImage  is the image url
     * @param publishedAt is the date and time the article was published
     * @param entitiesList list of article entities
     */
    public Article(String sourceId, String sourceName, String author, String title, String description, String url, String urlToImage,
                   String publishedAt, List<Entity> entitiesList) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.entitiesList = entitiesList;
    }
}
