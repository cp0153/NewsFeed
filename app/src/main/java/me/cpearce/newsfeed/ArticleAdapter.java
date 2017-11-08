package me.cpearce.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An {@link ArticleAdapter} knows how to create a list item layout for each earthquake
 * in the data source (a list of {@link Article} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {

    /**
     * The part of the location string from the USGS service that we use to determine
     * whether or not there is a location offset present ("5km N of Cairo, Egypt").
     */
    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * Constructs a new {@link ArticleAdapter}.
     *
     * @param context of the app
     * @param articles is the list of articles, which is the data source of the adapter
     */
    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    /**
     * Returns a list item view that displays information about the article at the given position
     * in the list of articles.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }

        // Find the article at the given position in the list of articles
        Article currentArticle = getItem(position);

        String title = currentArticle.getmTitle();

        // Find the TextView with view ID location
        TextView articleView = (TextView) listItemView.findViewById(R.id.article_title);
        // Display the location of the current article in that TextView
        articleView.setText(title);

        if (!currentArticle.getmAuthor().equals("null")) {
            String author = "by " + currentArticle.getmAuthor();
            TextView authorView = (TextView) listItemView.findViewById(R.id.article_author);
            authorView.setText(author);
        }

       // Find the TextView with view ID description
        String description = currentArticle.getmDescription();
        TextView descriptionView = (TextView) listItemView.findViewById(R.id.description);
        // Display the description of the current article in that TextView
        descriptionView.setText(description);

        String entities = "categories:\n" + currentArticle.getmEntities();
        TextView entitiesView = (TextView) listItemView.findViewById(R.id.entities);
        entitiesView.setText(entities);

        // Create a new Date object from the time in milliseconds of the article
        String date = currentArticle.getmPublishedAt();

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = date.substring(0, 9);
        // Display the date of the current article in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = date.substring(11, date.length() - 1);
        // Display the time of the current article in that TextView
        timeView.setText(formattedTime);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
