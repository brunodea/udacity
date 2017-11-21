package brunodea.udacity.com.popmovies.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

public class FavoritesProvider extends ContentProvider {
    private static final String PROVIDER_NAME = "brunodea.udacity.com.popmovies.db.FavoritesProvider";
    private static final String URL = "content://" + PROVIDER_NAME + "/favorites";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private static HashMap<String, String> FAVORITES_MOVIE_INFO_PROJECTION_MAP;

    public static final int FAVORITES = 1;
    public static final int FAVORITES_ID = 2;

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(PROVIDER_NAME, "favorites", FAVORITES);
        sUriMatcher.addURI(PROVIDER_NAME, "favorites/#", FAVORITES_ID);
    }

    private SQLiteDatabase mDB;

    @Override
    public boolean onCreate() {
        FavoritesDBHelper helper = new FavoritesDBHelper(getContext());
        mDB = helper.getWritableDatabase();
        // writable database creates DB if it doesn't exist already.
        return mDB != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(FavoritesDBHelper.MOVIES_TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
            case FAVORITES:
                qb.setProjectionMap(FAVORITES_MOVIE_INFO_PROJECTION_MAP);
                break;
            case FAVORITES_ID:
                qb.appendWhere(FavoritesDBHelper._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                // unreachable!
                break;
        }

        Cursor c = qb.query(mDB, projection, selection, selectionArgs, null, null,
                null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            // Get all records
            case FAVORITES:
                return "vnd.android.cursor.dir/vnd.popmovies.favorites";
            // get a single record
            case FAVORITES_ID:
                return "vnd.android.cursor.item/vnd.popmovies.favorites";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowID = mDB.insert(FavoritesDBHelper.MOVIES_TABLE_NAME, "", contentValues);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        switch (sUriMatcher.match(uri)) {
            case FAVORITES:
                count = mDB.delete(FavoritesDBHelper.MOVIES_TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITES_ID:
                String id = uri.getPathSegments().get(1);
                count = mDB.delete(FavoritesDBHelper.MOVIES_TABLE_NAME, FavoritesDBHelper._ID + "=" + id + (
                        !TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""
                        ), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    // Not used by this app!
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
