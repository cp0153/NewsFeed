package me.cpearce.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import me.cpearce.newsfeed.model.Source;

/**
 * Created by Christopher on 11/11/2017.
 */

public class SourceLoader extends AsyncTaskLoader<List<Source>> {
    private static final String LOG_TAG = SourceLoader.class.getName();

    private String mSourceUrl;

    public SourceLoader(Context context, String source_url) {
        super(context);
        mSourceUrl = source_url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Source> loadInBackground() {
        if (mSourceUrl == null) {
            return null;
        }
        return QueryUtils.fetchSourceData(mSourceUrl);
    }
}
