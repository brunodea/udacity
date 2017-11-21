package brunodea.udacity.com.popmovies.moviedb;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringDef;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import brunodea.udacity.com.popmovies.BuildConfig;
import brunodea.udacity.com.popmovies.R;
import brunodea.udacity.com.popmovies.model.MovieInfoResponseModel;
import brunodea.udacity.com.popmovies.model.MovieReviewResponseModel;
import brunodea.udacity.com.popmovies.model.MovieVideoResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Utility for dealing with requests to The Movie DB API.
 */
public class TheMovieDBAPI {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

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

    public static void getMovies(final int page, @SortByDef String sortBy, final Callback<MovieInfoResponseModel> callback) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        TheMovieDBAPIInterface movie_db = retrofit.create(TheMovieDBAPIInterface.class);
        Call<MovieInfoResponseModel> call = movie_db.getSortedMovies(sortBy, page, BuildConfig.API_KEY);
        call.enqueue(callback);
    }

    public static void getMovieVideos(final String movie_id, final Callback<MovieVideoResponseModel> callback) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        TheMovieDBAPIInterface movie_db = retrofit.create(TheMovieDBAPIInterface.class);
        Call<MovieVideoResponseModel> call = movie_db.getMovieVideos(movie_id, BuildConfig.API_KEY);
        call.enqueue(callback);
    }

    public static void getMovieReviews(final String movie_id, final Callback<MovieReviewResponseModel> callback) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        TheMovieDBAPIInterface movie_db = retrofit.create(TheMovieDBAPIInterface.class);
        Call<MovieReviewResponseModel> call = movie_db.getMovieReviews(movie_id, BuildConfig.API_KEY);
        call.enqueue(callback);
    }

    public static void downloadImageToView(Context context, ImageView imageView, @ImageW String imageW, String imagePath) {
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/"+imageW+"/"+imagePath)
                .placeholder(new ColorDrawable(context.getResources().getColor(android.R.color.white)))
                .error(R.mipmap.broken_image)
                .into(imageView);
    }

    public static Drawable getDrawable(Context context, @ImageW String imageW, String imagePath) {
        try {
            return new BitmapDrawable(context.getResources(), Picasso.with(context)
                   .load("http://image.tmdb.org/t/p/"+imageW+"/"+imagePath)
                   .placeholder(new ColorDrawable(context.getResources().getColor(android.R.color.white)))
                   .error(R.mipmap.broken_image)
                   .get());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
