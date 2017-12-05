package me.cpearce.newsfeed;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.cpearce.newsfeed.model.Article;
import me.cpearce.newsfeed.ImageLoadTask;
import me.cpearce.newsfeed.model.Entity;

/**
 * An {@link ArticleAdapter} knows how to create a list item layout for each article
 * in the data source (a list of {@link Article} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {

    /**
     * Constructs a new {@link ArticleAdapter}.
     *
     * @param context  of the app
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

        // Handle the article image

        if (!currentArticle.urlToImage.equals("null")) {
            String articleUrl = currentArticle.urlToImage;
            ImageView articleImgView = (ImageView) listItemView.findViewById(R.id.articleView);
            new ImageLoadTask(articleUrl, articleImgView).execute();
        }

        String title = currentArticle.title;

        // Find the TextView with view ID location
        TextView articleView = (TextView) listItemView.findViewById(R.id.article_title);
        // Display the location of the current article in that TextView
        articleView.setText(title);
        articleView.setTypeface(null, Typeface.BOLD);
        articleView.setTextColor(Color.BLACK);

        if (!currentArticle.author.equals("null")) {
            String author = "by " + currentArticle.author;
            TextView authorView = (TextView) listItemView.findViewById(R.id.article_author);
            authorView.setText(author);
        }

        // Find the TextView with view ID description
        String description = currentArticle.description;
        TextView descriptionView = (TextView) listItemView.findViewById(R.id.description);
        // Display the description of the current article in that TextView
        descriptionView.setText(description);

        List<Entity> entities = currentArticle.entitiesList;

        // get list of names with a url value
        //List<Map<String, String>> entityUrlNames = new ArrayList<>();
        StringBuilder tempText = new StringBuilder();
        TextView entityView =(TextView) listItemView.findViewById(R.id.entities);
        for (int i = 0; i < entities.size(); i++) {
            if (!entities.get(i).metadata.isEmpty()) {
                entityView.setClickable(true);
                entityView.setFocusable(false);
                entityView.setMovementMethod(LinkMovementMethod.getInstance());
                tempText.append("<a href='").append(entities.get(i).metadata.get("wikipedia_url")).append("'> ").append(entities.get(i).name).append(" ");
            }
        }
        entityView.setText(Html.fromHtml(tempText.toString()));
        return listItemView;
    }
}
