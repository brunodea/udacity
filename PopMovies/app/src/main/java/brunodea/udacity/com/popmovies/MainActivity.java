package brunodea.udacity.com.popmovies;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import brunodea.udacity.com.popmovies.adapter.TheMovieDBAdapter;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mPBLoadingMovies;

    private RecyclerView mRVPopMovies;
    private TheMovieDBAdapter mTheMovieDBAdapter;
    private RecyclerView.LayoutManager mRVLayoutManager;
    private Handler mQueryPosterHandler;

    // TODO: instead of using a fixed value, maybe calculate it somehow?
    private static int NUMBER_OF_COLUMNS_IN_GRID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupGUI();
    }

    private void setupGUI() {
        mPBLoadingMovies = (ProgressBar) findViewById(R.id.pb_loading_movies);
        // TODO: make the recyclerview horizontaly scrollable when the phone is rotated.
        mRVPopMovies = (RecyclerView) findViewById(R.id.rv_pop_movies);

        mRVLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS_IN_GRID);
        mTheMovieDBAdapter = new TheMovieDBAdapter(this);

        mQueryPosterHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what) {
                    case TheMovieDBAdapter.QUERY_MESSAGE_STARTED:
                        mPBLoadingMovies.setVisibility(View.VISIBLE);
                        break;
                    case TheMovieDBAdapter.QUERY_MESSAGE_FINISHED_WITH_SUCCESS:
                        mPBLoadingMovies.setVisibility(View.GONE);
                        mTheMovieDBAdapter.notifyDataSetChanged();
                        break;
                    case TheMovieDBAdapter.QUERY_MESSAGE_FINISHED_WITH_ERROR:
                        mPBLoadingMovies.setVisibility(View.GONE);
                        break;
                    default:
                        // TODO
                        Log.w("", "shouldn't be here!");
                        break;
                }
            }
        };

        mTheMovieDBAdapter.queryPosterHashes(mQueryPosterHandler);

        // TODO: make sure the recycleview has fixed size.
        mRVPopMovies.setHasFixedSize(true);
        mRVPopMovies.setLayoutManager(mRVLayoutManager);
        mRVPopMovies.setAdapter(mTheMovieDBAdapter);
    }
}
