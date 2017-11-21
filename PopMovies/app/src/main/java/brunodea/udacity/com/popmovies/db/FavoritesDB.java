package brunodea.udacity.com.popmovies.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;

import brunodea.udacity.com.popmovies.model.MovieInfoModel;

public class FavoritesDB {
    private ContentResolver mContentResolver;

    public FavoritesDB(Context context) {
        mContentResolver = context.getContentResolver();
    }

    private MovieInfoModel findByMovieID(final int movie_id) {
        String[] projection = {FavoritesDBHelper._ID, FavoritesDBHelper.MOVIE_INFO_JSON};
        String selection = FavoritesDBHelper.MOVIE_ID + " = \"" + movie_id + "\"";
        Cursor c = mContentResolver.query(FavoritesProvider.CONTENT_URI, projection, selection, null, null);
        MovieInfoModel model = null;
        if (c != null) {
            if (c.moveToFirst()) {
                model = new Gson().fromJson(c.getString(2), MovieInfoModel.class);
            }
            c.close();
        }

        return model;
    }

    public boolean isFavorite(final int movie_id) {
        return findByMovieID(movie_id) != null;
    }

    public void insertMovieInfo(final MovieInfoModel model) {
        ContentValues cv = new ContentValues();
        cv.put(FavoritesDBHelper.MOVIE_ID, model.getId());
        cv.put(FavoritesDBHelper.MOVIE_INFO_JSON, model.toString());

        mContentResolver.insert(FavoritesProvider.CONTENT_URI, cv);
    }

    public void deleteMovieInfo(final int movie_id) {
        String selection = FavoritesDBHelper.MOVIE_ID + " = \"" + movie_id + "\"";
        mContentResolver.delete(FavoritesProvider.CONTENT_URI, selection, null);
    }
}
