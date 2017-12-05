package me.cpearce.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.cpearce.newsfeed.model.Article;
import me.cpearce.newsfeed.model.Entity;
import me.cpearce.newsfeed.model.Source;

/**
 * Loads a list of articles by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /** Query URL */
    private String mNewsUrl;
    /** Query URL */
    private String mSourceUrl;

    /**
     * Constructs a new {@link ArticleLoader}.
     *
     * @param context of the activity
     * @param newsUrl to load data from news
     */
    public ArticleLoader(Context context, String newsUrl, String sourceUrl) {
        super(context);
        mNewsUrl = newsUrl;
        mSourceUrl = sourceUrl;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Article> loadInBackground() {
        if (mNewsUrl == null) {
            return null;
        }

        List<Source> sources = QueryUtils.fetchSourceData(mSourceUrl);

        List<Article> articles = new ArrayList<>();

        articles.addAll(QueryUtils.fetchArticleData(mNewsUrl,((sources.size() < 5) ? sources : sources.subList(0,2))));

        return articles;
    }
}
