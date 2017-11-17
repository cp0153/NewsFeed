package me.cpearce.newsfeed.model;

/**
 * Created by Christopher on 11/8/2017.
 * Class for a source
 */

public class Source {
    public final String id;
    public final String name;
    public final String description;
    public final String url;
    public final String category;
    public final String language;
    public final String country;

    /**
     * Constructs a new {@link Source} object.
     * object used for soucres endpoint at https://newsapi.org/#apiSources
     *
     * @param id               The unique identifier for the news source. This is needed when querying
     *                         the /articles endpoint to retrieve article metadata.
     * @param name             The display-friendly name of the news source.
     * @param description      A brief description of the news source and what area they specialize in.
     * @param url              The base URL or homepage of the source.
     * @param category         The topic category that the source focuses on.
     *                         Possible options: business, entertainment, gaming, general, music,
     *                         politics, science-and-nature, sport, technology
     * @param language         The 2-letter ISO-639-1 code for the language that the source is
     *                         written in. Possible options: en, de, fr
     * @param country          The 2-letter ISO 3166-1 code of the country that the source mainly
     *                         focuses on.
     */
    public Source(String id, String name, String description, String url, String category,
                  String language, String country) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
        this.language = language;
        this.country = country;
    }
}