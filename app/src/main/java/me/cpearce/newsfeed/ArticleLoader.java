package me.cpearce.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import me.cpearce.newsfeed.model.Article;

/**
 * Loads a list of articles by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ArticleLoader.class.getName();

    /** Query URL */
    private String mNewsUrl;
    /** Query URL */
    private String mMLUrl;

    /**
     * Constructs a new {@link ArticleLoader}.
     *
     * @param context of the activity
     * @param news_url to load data from news
     * @param ml_url to load data from google
     */
    public ArticleLoader(Context context, String news_url, String ml_url) {
        super(context);
        mNewsUrl = news_url;
        mMLUrl = ml_url;
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

        // Perform the network request, parse the response, and extract a list of articles.
        List<Article> articles = QueryUtils.fetchArticleData(mNewsUrl);
//        for (int i = 0; i < articles.size(); i++) {
//            Article article = articles.get(i);
//            String entities = QueryUtils.fetchEntityData(mMLUrl, article.getmDescription());
//
//            article.setmEntites(entities);
//        }
        return articles;
    }
}
