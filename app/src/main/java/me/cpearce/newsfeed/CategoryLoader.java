package me.cpearce.newsfeed;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.cpearce.newsfeed.model.Source;


/**
 * Created by Robert on 11/29/2017.
 */

public class CategoryLoader extends AsyncTaskLoader<List<String>>{

    private String sourceUrl;

    public CategoryLoader(Context context, String sourceUrl)
    {
        super(context);
        this.sourceUrl = sourceUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<String> loadInBackground()
    {
        List<Source> sources = QueryUtils.fetchSourceData(sourceUrl);

        List<String> categories = new ArrayList<>();

        for(Source source: sources)
        {
            if(!categories.contains(source.category))
            {
                categories.add(source.category);
            }
        }

        return categories;
    }


}
