package me.cpearce.newsfeed;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import me.cpearce.newsfeed.model.Article;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    private ArticleAdapter mAdapter;

    /**
     * URL for the https://newsapi.org
     */
    private static final String ARTICLE_REQUEST_ROOT_URL = "https://newsapi.org/v1/articles?source=google-news&sortBy=top&apiKey=23b2fa848a2a45aa85546b463a7afc0a";

    private static final String NAT_LANGUAGE_REQUEST_ROOT_URL = "https://language.googleapis.com/v1/documents:analyzeEntities?key=AIzaSyAh9uz0qNveHuiNYNBhjanf5gq86Su5rlo";

    private static final String SOURCE_REQUEST_URL = "https://newsapi.org/v1/sources?apiKey=23b2fa848a2a45aa85546b463a7afc0a";

    private static final String POLITICS_URL = "https://newsapi.org/v1/sources?category=politics&apiKey=23b2fa848a2a45aa85546b463a7afc0a";

    private static final String TECH_URL = "https://newsapi.org/v1/sources?category=technology&apiKey=23b2fa848a2a45aa85546b463a7afc0a";

    private static final String BUSINESS_URL = "https://newsapi.org/v1/sources?category=business&apiKey=23b2fa848a2a45aa85546b463a7afc0a";

    private static final String ENTERTAINMENT_URL = "https://newsapi.org/v1/sources?category=entertainment&apiKey=23b2fa848a2a45aa85546b463a7afc0a";

    private static String currentSourceUrl = "";

    //for testing purospes
    private static final String TEST_URL = "https://newsapi.org/v1/articles?source=ign&sortBy=top&apiKey=23b2fa848a2a45aa85546b463a7afc0a";
    /**
     * Constant value for the article loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */

    private static final int ARTICLE_LOADER_ID = 1;
    private static final int SOURCE_LOADER_ID = 2;
//    private static final int ENTITY_LOADER_ID = 2;


    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);


        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        articleListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of articles as input
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected article.
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                Article currentArticle = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(currentArticle.getmUrl());

                // Create a new intent to view the article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
            loaderManager.initLoader(SOURCE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        //Initializing NavigationView
        final NavigationView navigationView = findViewById(R.id.navigation);


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked())
                {
                    menuItem.setChecked(false);
                }
                else
                {
                    menuItem.setChecked(true);
                }

                //Here we reset the loader to fetech data based on which item was clicked
                switch (menuItem.getItemId()) {
                    case R.id.Trending:
                        //TODO
                    case R.id.Politics:
                        currentSourceUrl = POLITICS_URL;
                        break;
                    case R.id.Tech:
                        currentSourceUrl = TECH_URL;
                        break;
                    case R.id.Business:
                        currentSourceUrl = BUSINESS_URL;
                        break;
                    case R.id.Entertainment:
                        currentSourceUrl = ENTERTAINMENT_URL;
                    default:
                        //change the url used the loader
                        currentSourceUrl = "";
                }

                mAdapter.clear();
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(ARTICLE_LOADER_ID, null, MainActivity.this);
                loaderManager.restartLoader(SOURCE_LOADER_ID, null,MainActivity.this);
                return true;
            }
        });
    }

    // TODO: refactor to use 3 loaders
    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {


        // Create a new loader for the given URL
        return new ArticleLoader(this, ARTICLE_REQUEST_ROOT_URL, NAT_LANGUAGE_REQUEST_ROOT_URL, currentSourceUrl);
    }

        //@Override
//    public Loader<List<Source>> onCreateLoader(int i, Bundle bundle) {
//        return new SourceLoader(this, SOURCE_REQUEST_URL);
//    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No articles found."
        mEmptyStateTextView.setText(R.string.no_articles);

        // Clear the adapter of previous article data
        mAdapter.clear();

        // If there is a valid list of {@link article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_navigation_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.navigation) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}