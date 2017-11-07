package me.cpearce.assignment_6_news_app_cp0153;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ArticleLoader.class.getName();

    /** Query URL */
    private String mNews_Url;
    /** Query URL */
    private String mML_Url;

    /**
     * Constructs a new {@link ArticleLoader}.
     *
     * @param context of the activity
     * @param news_url to load data from news
     * @param ml_url to load data from google
     */
    public ArticleLoader(Context context, String news_url, String ml_url) {
        super(context);
        mNews_Url = news_url;
        mML_Url = ml_url;
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
        if (mNews_Url == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of articles.
        List<Article> articles = QueryUtils.fetchArticleData(mNews_Url);
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            String entities = QueryUtils.fetchEntityData(mML_Url, article.getmDescription());

            article.setmEntites(entities);
        }
        return articles;
    }
}
