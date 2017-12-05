package me.cpearce.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


import me.cpearce.newsfeed.model.Article;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks {

    private static final String LOG_TAG = MainActivity.class.getName();

    private ArticleAdapter mAdapter;

    private static final String ARTICLE_REQUEST_ROOT_URL = "https://newsapi.org//v2/top-headlines?language=en"; // ?source=google-news&sortBy=top&apiKey=23b2fa848a2a45aa85546b463a7afc0a";

    private static final String NAT_LANGUAGE_REQUEST_ROOT_URL = "https://language.googleapis.com/v1beta2/documents:analyzeEntities"; // ?key=AIzaSyAh9uz0qNveHuiNYNBhjanf5gq86Su5rlo";

    private static final String SOURCE_REQUEST_URL = "https://newsapi.org/v2/sources?language=en";

    private static final String EVERYTHING_REQUEST_URL = "https://newsapi.org//v2/everything?language=en";

    private static final String HISTORY_REQUEST_URL = "https://newsfeed-38210.firebaseio.com/articles.json?orderBy=%22$key%22&limitToLast=50";

    // these two variables are used to reset the loader
    private static String currentArticleUrl = ARTICLE_REQUEST_ROOT_URL;

    private static String currentSourcesUrl = SOURCE_REQUEST_URL;

    /**
     * Constant value for the article loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */

    private static final int ARTICLE_LOADER_ID = 1;
    private static final int SOURCE_LOADER_ID = 2;
    private static final int CATEGORY_LOADER_ID = 3;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    private DrawerLayout mDrawerLayout;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);


        String[] categories ={"business","entertainment","gaming","general","health-and-medical",
                "music","politics","science-and-nature","sport","technology","History"};

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

                //add item to FireBase
                UserHistoryUpload upload = new UserHistoryUpload();
                upload.execute(currentArticle);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(currentArticle.url);

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
            //loaderManager.initLoader(CATEGORY_LOADER_ID,null,this);

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
        final int MENU_HEAD = Menu.FIRST;

        //add items to the menu
        final Menu navMenu = navigationView.getMenu();
        //cAdapter = new CategoryAdapter(this,new ArrayList<String>());

       for(int i = 0; i < categories.length; i++)
       {
           navMenu.add(0,MENU_HEAD, 0, categories[i]);
       }

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawers();

                //Get articles user viewed perviously from FireBase
                if(menuItem.getTitle().equals("History"))
                {
                    currentArticleUrl = HISTORY_REQUEST_URL;

                }

                //Send Request to api based on item selected
                else {
                    //Here we reset the loader to fetch data based on which item was clicked
                    currentArticleUrl = EVERYTHING_REQUEST_URL;
                    currentSourcesUrl = SOURCE_REQUEST_URL + "&category=" + menuItem.getTitle();
                }

                mAdapter.clear();

                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.VISIBLE);
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(ARTICLE_LOADER_ID, null, MainActivity.this);
                loaderManager.restartLoader(SOURCE_LOADER_ID, null, MainActivity.this);


                return true;
            }
        });
    }

    @Override
    public Loader onCreateLoader(int id, Bundle bundle) {

        return new ArticleLoader(this, currentArticleUrl, currentSourcesUrl);
    }

    public void setOnSearchClickListener (View.OnClickListener listener) {

    }

    @Override
    public void onLoadFinished(Loader loader, Object articles) {
        int loaderId = loader.getId();
        View loadingIndicator = findViewById(R.id.loading_indicator);

        if(loaderId == ARTICLE_LOADER_ID) {
            // Hide loading indicator because the data has been loaded
            loadingIndicator.setVisibility(View.GONE);

            // Set empty state text to display "No articles found."
            mEmptyStateTextView.setText(R.string.no_articles);

            // Clear the adapter of previous article data
            mAdapter.clear();

            // If there is a valid list of {@link article}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if ((List<Article>)articles != null && !((List<Article>)articles).isEmpty()) {
                mAdapter.addAll((List<Article>)articles);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
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

    public void btnRegistration_Click(View v){
        Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(i);
    }

    public void btnLogin_Click(View v){
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

}