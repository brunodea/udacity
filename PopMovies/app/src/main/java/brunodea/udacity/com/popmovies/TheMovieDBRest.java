package brunodea.udacity.com.popmovies;

import android.net.Uri;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Utility for dealing with requests to The Movie DB API.
 */
public class TheMovieDBRest {
    // API Key for accessing The Movie DB APIs.
    private static String API_KEY = "a1fa1f5a047f3430c90c647fe1fcdf8d";

    private static AsyncHttpClient sClient = new AsyncHttpClient();

    public static void discover(int page, AsyncHttpResponseHandler handler) {
        // TODO: make infinite scroll
        Uri.Builder builder = baseRequestURLBuilder();
        builder.appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("language", "en-US")
                .appendQueryParameter("sort_by", "popularity.desc")
                .appendQueryParameter("include_adult", "false")
                .appendQueryParameter("include_video", "false")
                .appendQueryParameter("page", String.valueOf(page));
        http_get(builder.build().toString(), handler);
    }

    private static void http_get(String urlWithParams, AsyncHttpResponseHandler handler) {
        sClient.get(urlWithParams, handler);
    }

    private static Uri.Builder baseRequestURLBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendQueryParameter("api_key", API_KEY);
        return builder;
    }
}
