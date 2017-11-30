package me.cpearce.newsfeed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
/**
 * Created by Robert on 11/29/2017.
 */

public class CategoryAdapter extends  ArrayAdapter<String>{

    /**
     * Constructs a new {@link CategoryAdapter}.
     *
     * @param context of the app
     * @param categories is the list of categories, which is the data source of the adapter
     */
    public CategoryAdapter(Context context, List<String> categories) {
        super(context, 0, categories);
    }


}
