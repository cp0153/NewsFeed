//package me.cpearce.newsfeed;
//
//import android.content.AsyncTaskLoader;
//import android.content.Context;
//
//import java.util.List;
//
//import me.cpearce.newsfeed.model.Entity;
//
///**
// * Created by Christopher on 11/11/2017.
// */
//
//public class EntityLoader extends AsyncTaskLoader<List<Entity>> {
//    /** Tag for log messages */
//    private static final String LOG_TAG = ArticleLoader.class.getName();
//
//    /** Query URL */
//    private String mMLUrl;
//    private String mArticleDescription;
//
//    public EntityLoader(Context context, String ml_url, String articleDescription) {
//        super(context);
//        mMLUrl = ml_url;
//        mArticleDescription = articleDescription;
//    }
//
//    @Override
//    protected void onStartLoading() {
//        forceLoad();
//    }
//
//    @Override
//    public List<Entity> loadInBackground() {
//        if (mMLUrl == null) {
//            return null;
//        }
//
//        // Perform the network request, parse the response, and extract a list of articles.
//        String entities = QueryUtils.fetchEntityData(mMLUrl, mArticleDescription);
//        return entities;
//    }
//}
