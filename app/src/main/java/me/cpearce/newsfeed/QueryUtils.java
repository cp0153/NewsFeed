package me.cpearce.newsfeed;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import me.cpearce.newsfeed.model.Article;
import me.cpearce.newsfeed.model.Entity;
import me.cpearce.newsfeed.model.Source;

/**
 * Helper methods related to requesting and receiving news data from https://newsapi.org/
 */

public class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    // private static final DatabaseReference NEWS_API_KEY = database.getReference("apiKey").child("newsapi");
    // private static final DatabaseReference NAT_API_KEY = database.getReference("apiKey").child("google");
    private static final String NEWS_API_KEY = "23b2fa848a2a45aa85546b463a7afc0a";
    private static final String NAT_API_KEY = "AIzaSyAh9uz0qNveHuiNYNBhjanf5gq86Su5rlo";


    public static List<Article> fetchArticleData(String requestUrl, List<Source> sources) {
         requestUrl = requestUrl + "?sources=" +
                getCommaSeparatedSourceList(sources) +
                "&apiKey=" +
                NEWS_API_KEY;

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeGetHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the GET HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Articles}s

        // Return the list of {@link Articles}s
        return extractArticles(jsonResponse);
    }

    public static List<Article> fetchArticleData(String requestUrl) {
        URL url = createUrl(requestUrl + "&apiKey=" +
                NEWS_API_KEY);

        String jsonResponse = null;
        try {
            jsonResponse = makeGetHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the GET HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Articles}s

        // Return the list of {@link Articles}s
        return extractArticles(jsonResponse);
    }

    public static List<Article> fetchArticleData(String requestUrl, String categories) {
        URL url = createUrl(requestUrl + "&category=" + categories + "&apiKey=" +
                NEWS_API_KEY);

        String jsonResponse = null;
        try {
            jsonResponse = makeGetHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the GET HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Articles}s

        // Return the list of {@link Articles}s
        return extractArticles(jsonResponse);
    }

    public static List<Article> fetchEverythingData(String requestUrl, String searchQuery) {
        URL url = createUrl(requestUrl + "q=" + searchQuery + "&apiKey=" +
                NEWS_API_KEY);

        String jsonResponse = null;
        try {
            jsonResponse = makeGetHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the GET HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Articles}s

        // Return the list of {@link Articles}s
        return extractArticles(jsonResponse);
    }

    public static String getCommaSeparatedSourceList(List<Source> sources) {
        StringBuilder commaList = new StringBuilder("");
        for (int i = 0; i < sources.size(); i++) {
            commaList.append(sources.get(i).id);
            commaList.append(",");
        }
        return commaList.toString();
    }

    public static List<Source> fetchSourceData(String requestUrl) {
        URL url = createUrl(requestUrl + ((requestUrl.contains("?")) ? "&" : "?") + "apiKey=" + NEWS_API_KEY);
        String jsonResponse = null;
        try {
            jsonResponse = makeGetHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the GET HTTP request.", e);
        }
        return extractSources(jsonResponse);
    }

    public static List<Source> fetchSourceData(String requestUrl, String category) {
        requestUrl = requestUrl + "?category=" + category + "&apiKey=" +
                NEWS_API_KEY;
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeGetHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the GET HTTP request.", e);
        }
        return extractSources(jsonResponse);
    }

    public static List<Entity> fetchEntityData(String requestUrl, String description) {
        URL url = createUrl(requestUrl + "?key=" + NAT_API_KEY);

        String jsonResponse = null;
        try {
            jsonResponse = makePostHttpRequest(url, description);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the GET HTTP request.", e);
        }
        return extractEntities(jsonResponse);
    }

    private static List<Entity> extractEntities(String jsonResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<Entity> entities = new ArrayList<>();

        // Try to parse the SAMPLE_NEWS_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the SAMPLE_NEWS_JSON_RESPONSE string
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            JSONArray entityArray = baseJsonResponse.getJSONArray("entities");
            for (int i = 0; i < entityArray.length(); i++) {
                JSONObject currentEntity = entityArray.getJSONObject(i);
                String entity_name = currentEntity.getString("name");
                String entity_type = currentEntity.getString("type");
                int entity_salience = currentEntity.getInt("salience");
                JSONObject metadata = currentEntity.getJSONObject("metadata");
                Map<String, String> wikiLinks = new HashMap<>();
                if (metadata.has("wikipedia_url")) {

                    String wikipedia_url = metadata.getString("wikipedia_url");
                    wikiLinks.put("wikipedia_url", wikipedia_url);
                }
                Entity entity = new Entity(entity_name, entity_type, wikiLinks, entity_salience);
                entities.add(entity);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }
        return entities;
    }

    private static List<Source> extractSources(String sourcesJSON) {
        if (TextUtils.isEmpty(sourcesJSON)) {
            return null;
        }
        List<Source> sources = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(sourcesJSON);
            JSONArray sourcesArray = baseJsonResponse.getJSONArray("sources");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference SourceRef = database.getReference("sources");

            for (int i = 0; i < sourcesArray.length(); i++) {
                JSONObject currentSource = sourcesArray.getJSONObject(i);
                String id = currentSource.getString("id");
                String name = currentSource.getString("name");
                String description = currentSource.getString("description");
                String url = currentSource.getString("url");
                String category = currentSource.getString("category");
                String language = currentSource.getString("language");
                String country = currentSource.getString("country");

                Source source = new Source(id, name, description, url, category, language, country);
                sources.add(source);
                SourceRef.push().setValue(source);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }
        return sources;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeGetHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeGetHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String getQuery(List<SimpleEntry> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (SimpleEntry pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey().toString(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().toString(), "UTF-8"));
        }

        return result.toString();
    }

    /**
     * Make an HTTP request to the Google Cloud Natural Language API
     */
    private static String makePostHttpRequest(URL url, String description) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            HashMap document_properties = new HashMap();
            document_properties.put("type", "PLAIN_TEXT");
            document_properties.put("content", description);

            HashMap document = new HashMap();
            document.put("document", document_properties);
            JSONObject doc = new JSONObject(document);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(doc.toString());
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + conn.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the ML JSON results.", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeGetHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Article> extractArticles(String articleJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }
        List<Article> articles = new ArrayList<>();

        // Try to parse the SAMPLE_NEWS_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the SAMPLE_NEWS_JSON_RESPONSE string
            JSONObject baseJsonResponse = new JSONObject(articleJSON);
            String entities = "";
            //JSONObject MLJsonResponse = new JSONObject(entityJSON);

            // Extract the JSONArray associated with the key called "articles",
            // which represents a list of news articles.
            JSONArray articleArray = baseJsonResponse.getJSONArray("articles");
            //JSONArray entityArray = MLJsonResponse.getJSONArray("entities");
//            StringBuilder entities = new StringBuilder();
//            for (int i = 0; i < entityArray.length(); i++) {
//                JSONObject currentEntity = entityArray.getJSONObject(i);
//                String entity_name = currentEntity.getString("name");
//                entities.append(entity_name);
//                entities.append(", ");
//            }
//            entities.substring(0, entities.length() -2);
//            String built_entities = entities.toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("articles");
            // for each article in the articleArray, create an {@link Article} object
            for (int i = 0; i < articleArray.length(); i++) {

                JSONObject currentArticle = articleArray.getJSONObject(i);
                String author = currentArticle.getString("author");
                String title = currentArticle.getString("title");
                String description = currentArticle.getString("description");
                String url = currentArticle.getString("url");
                String urlToImage = currentArticle.getString("urlToImage");
                String publishedAt = currentArticle.getString("publishedAt");
                JSONObject currentSource = currentArticle.getJSONObject("source");
                String sourceId = currentSource.getString("id");
                String sourceName = currentSource.getString("name");


                Article article = new Article(sourceId, sourceName, author, title, description, url, urlToImage, publishedAt);
                articles.add(article);
                myRef.push().setValue(article);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }
        return articles;
    }

    public List<String> fetchCategoryData()
    {
        return new ArrayList<String>();
    }
}
