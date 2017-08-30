package brunodea.udacity.com.popmovies;

import android.net.Uri;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Utility for dealing with requests to The Movie DB API.
 */
public class TheMovieDBRest {
    public enum SortBy {
        Popularity {
            @Override
            public String key() { return "popular"; }
        },
        Rating {
            @Override
            public String key() { return "top_rated"; }
        };

        public abstract String key();
   }

    // API Key for accessing The Movie DB APIs.
    private static String API_KEY = "a1fa1f5a047f3430c90c647fe1fcdf8d";

    private static AsyncHttpClient sClient = new AsyncHttpClient();

    public static void discover(int page, SortBy sortBy, AsyncHttpResponseHandler handler) {
        // TODO: make infinite scroll
        Uri.Builder builder = baseRequestURLBuilder();
        builder
                .appendPath("movie")
                .appendPath(sortBy.key())
                .appendQueryParameter("page", String.valueOf(page));
        String final_url = builder.build().toString();
        // TODO: fix TAG
        Log.i("TheMovieDBRest", final_url);
        http_get(final_url, handler);
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
