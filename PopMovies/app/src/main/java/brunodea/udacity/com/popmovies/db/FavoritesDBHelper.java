package brunodea.udacity.com.popmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Movies";
    public static final String MOVIES_TABLE_NAME = "favorites_movie_info";
    private static final int DB_VERSION = 1;

    public static final String _ID = "_id";
    public static final String MOVIE_ID = "movie_id";
    public static final String MOVIE_INFO_JSON = "movie_info_json";

    private static final String CREATE_DB_TABLE =
            "CREATE TABLE " + MOVIES_TABLE_NAME +
                    " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MOVIE_ID + " INTEGER NOT NULL," +
                    MOVIE_INFO_JSON + " TEXT NOT NULL);";


    public FavoritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MOVIES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
