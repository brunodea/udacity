package brunodea.udacity.com.popmovies;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.StringDef;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Utility for dealing with requests to The Movie DB API.
 */
public class TheMovieDBAPI {
    public static final String SORTBY_POPULARITY = "popular";
    public static final String SORTBY_RATING = "top_rated";
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ SORTBY_POPULARITY, SORTBY_RATING })
    public @interface SortByDef {}

    public static final String IMAGE_W185 = "w185";
    public static final String IMAGE_W780 = "w780";
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ IMAGE_W185, IMAGE_W780 })
    public @interface ImageW {}

    private static AsyncHttpClient sClient = new AsyncHttpClient();

    public static void discover(int page, @SortByDef String sortBy, AsyncHttpResponseHandler handler) {
        Uri.Builder builder = baseRequestURLBuilder();
        builder
                .appendPath("movie")
                .appendPath(sortBy)
                .appendQueryParameter("page", String.valueOf(page));
        String final_url = builder.build().toString();
        http_get(final_url, handler);
    }

    public static void downloadImageToView(Context context, ImageView imageView, @ImageW String imageW, String imagePath) {
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/"+imageW+"/"+imagePath)
                .error(R.mipmap.broken_image)
                .into(imageView);
    }

    private static void http_get(String urlWithParams, AsyncHttpResponseHandler handler) {
        sClient.get(urlWithParams, handler);
    }

    private static Uri.Builder baseRequestURLBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendQueryParameter("api_key", BuildConfig.API_KEY);
        return builder;
    }
}
