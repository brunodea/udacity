package brunodea.udacity.com.popmovies;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import brunodea.udacity.com.popmovies.adapter.TheMovieDBAdapter;
import brunodea.udacity.com.popmovies.model.TheMovieDBResponseModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String MOVIE_LIST_PARCELABLE_STATE_KEY = "movies_list";
    
    private static final int COLUMNS_LANDSCAPE = 4;
    private static final int COLUMNS_PORTRAIT = 2;

    @BindView(R.id.pb_loading_movies) ProgressBar mPBLoadingMovies;
    @BindView(R.id.tv_error) TextView mTVError;
    @BindView(R.id.rv_pop_movies) RecyclerView mRVPopMovies;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private TheMovieDBAdapter mTheMovieDBAdapter;
    private Handler mQueryPosterHandler;

    private @TheMovieDBAPI.SortByDef String mCurrSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCurrSortBy = TheMovieDBAPI.SORTBY_POPULARITY;
        mTheMovieDBAdapter = new TheMovieDBAdapter(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update_main_movies();
            }
        });
        mQueryPosterHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                mSwipeRefreshLayout.setRefreshing(false);
                switch (inputMessage.what) {
                    case TheMovieDBAdapter.QUERY_MESSAGE_STARTED:
                        mPBLoadingMovies.setVisibility(View.VISIBLE);
                        break;
                    case TheMovieDBAdapter.QUERY_MESSAGE_FINISHED_WITH_SUCCESS:
                        mPBLoadingMovies.setVisibility(View.GONE);
                        mTheMovieDBAdapter.notifyDataSetChanged();
                        mTVError.setVisibility(View.GONE);
                        break;
                    case TheMovieDBAdapter.QUERY_MESSAGE_FINISHED_WITH_ERROR:
                        mTVError.setText(getResources().getText(R.string.error_downloading_info));
                        mTVError.setVisibility(View.VISIBLE);
                        mPBLoadingMovies.setVisibility(View.GONE);
                        break;
                    default:
                        //unreachable!
                        Log.e(TAG, "Reached invalid place in code.");
                        break;
                }
            }
        };
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_LIST_PARCELABLE_STATE_KEY)) {
            mTheMovieDBAdapter.setResponseModel(
                    (TheMovieDBResponseModel) savedInstanceState.getParcelable(MOVIE_LIST_PARCELABLE_STATE_KEY)
            );
        } else {
            update_main_movies();
        }

        int num_cols = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                COLUMNS_LANDSCAPE : COLUMNS_PORTRAIT;
        mRVPopMovies.setLayoutManager(new GridLayoutManager(this, num_cols));
        mRVPopMovies.setHasFixedSize(true);
        mRVPopMovies.setAdapter(mTheMovieDBAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_LIST_PARCELABLE_STATE_KEY, mTheMovieDBAdapter.getResponseModel());
        super.onSaveInstanceState(outState);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void update_main_movies() {
        mTheMovieDBAdapter.reset();
        if (isOnline()) {
            mTheMovieDBAdapter.queryPosterHashes(mCurrSortBy, mQueryPosterHandler);
            mTVError.setVisibility(View.GONE);
        } else {
            mTVError.setText(R.string.error_no_internet);
            mTVError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sortby: {
                PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.action_sortby));
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String last_sort_by = mCurrSortBy;
                        switch (item.getItemId()) {
                            case R.id.action_sortby_popularity: {
                                mCurrSortBy = TheMovieDBAPI.SORTBY_POPULARITY;
                                break;
                            }
                            case R.id.action_sortby_rating: {
                                mCurrSortBy = TheMovieDBAPI.SORTBY_RATING;
                                break;
                            }
                            default: {
                                return false;
                            }
                        }
                        if (!mCurrSortBy.equals(last_sort_by)) {
                            update_main_movies();
                        }
                        return true;
                    }
                });
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.sort_by_menu, popupMenu.getMenu());
                popupMenu.show();
                return true;
            }
            case R.id.action_refresh: {
                //mSwipeRefreshLayout.setRefreshing(true);
                update_main_movies();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
