package me.cpearce.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.cpearce.newsfeed.model.Article;
import me.cpearce.newsfeed.model.Source;

/**
 * Loads a list of articles by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ArticleLoader.class.getName();

    private static final String QUERY_HEADER = "https://newsapi.org/v1/articles?source=";
    private static final String QUERY_FOOTER = "&sortBy=top&apiKey=23b2fa848a2a45aa85546b463a7afc0a";

    /** Query URL */
    private String mNewsUrl;
    /** Query URL */
    private String mMLUrl;
    /** Query URL */
    private String mSrcUrl;

    /**
     * Constructs a new {@link ArticleLoader}.
     *
     * @param context of the activity
     * @param news_url to load data from news
     * @param ml_url to load data from google
     * @param src_url to load the data for news sources
     */
    public ArticleLoader(Context context, String news_url, String ml_url, String src_url) {
        super(context);
        mNewsUrl = news_url;
        mMLUrl = ml_url;
        mSrcUrl = src_url;

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

        List<Article> articles = new ArrayList<>();

        //If no sources default to basic
        if(mSrcUrl.equals(""))
        {
            articles = QueryUtils.fetchArticleData(mNewsUrl);
        }

        //get the sources and run all
        else
        {
            ArrayList<Source> sources = new ArrayList<>(QueryUtils.fetchSourceData(mSrcUrl));


            for (Source source: sources)
            {
                articles.addAll(QueryUtils.fetchArticleData(QUERY_HEADER + source.getmId() +QUERY_FOOTER));
            }

            //articles = QueryUtils.fetchArticleData(mNewsUrl);
        }

        // Perform the network request, parse the response, and extract a list of articles.
        //List<Article> articles = QueryUtils.fetchArticleData(mNewsUrl);
//        for (int i = 0; i < articles.size(); i++) {
//            Article article = articles.get(i);
//            String entities = QueryUtils.fetchEntityData(mMLUrl, article.getmDescription());
//
//            article.setmEntites(entities);
//        }
        return articles;
    }
}
