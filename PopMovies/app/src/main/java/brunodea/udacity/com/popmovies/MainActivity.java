package brunodea.udacity.com.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.Toast;

import brunodea.udacity.com.popmovies.adapter.EndlessRecyclerViewScrollListener;
import brunodea.udacity.com.popmovies.adapter.MovieInfoAdapter;
import brunodea.udacity.com.popmovies.db.FavoritesDB;
import brunodea.udacity.com.popmovies.model.MovieInfoModel;
import brunodea.udacity.com.popmovies.model.MovieInfoResponseModel;
import brunodea.udacity.com.popmovies.moviedb.TheMovieDBAPI;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityLOG";
    private static final String MOVIE_LIST_PARCELABLE_STATE_KEY = "movies_list";

    private static final String CURR_SORT_PREF = "curr_sort_pref";

    private static final int COLUMNS_LANDSCAPE = 4;
    private static final int COLUMNS_PORTRAIT = 2;

    @BindView(R.id.tv_sort_by_title) TextView mSortByTitle;
    @BindView(R.id.pb_loading_movies) ProgressBar mPBLoadingMovies;
    @BindView(R.id.tv_error) TextView mTVError;
    @BindView(R.id.rv_pop_movies) RecyclerView mRVPopMovies;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private MovieInfoAdapter mMovieInfoAdapter;
    private Handler mQueryPosterHandler;
    private EndlessRecyclerViewScrollListener mEndlessScrollListener;

    private @TheMovieDBAPI.SortByDef String mCurrSortBy;
    private FavoritesDB mFavoriteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFavoriteDB = new FavoritesDB(this);

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        mCurrSortBy = prefs.getString(CURR_SORT_PREF, TheMovieDBAPI.SORTBY_POPULARITY);
        adjustSortByTitle();

        mMovieInfoAdapter = new MovieInfoAdapter(this, new MovieInfoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(MovieInfoModel model) {
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.RESULT_MODEL_EXTRA, model);
                startActivity(intent);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reset_movie_list();
            }
        });
        mQueryPosterHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                mSwipeRefreshLayout.setRefreshing(false);
                switch (inputMessage.what) {
                    case MovieInfoAdapter.QUERY_MESSAGE_STARTED:
                        if (mMovieInfoAdapter.getItemCount() == 0) {
                            mPBLoadingMovies.setVisibility(View.VISIBLE);
                        }
                        break;
                    case MovieInfoAdapter.QUERY_MESSAGE_FINISHED_WITH_SUCCESS:
                        // msg.arg1 = page, msg.arg2 = item_count
                        mPBLoadingMovies.setVisibility(View.GONE);
                        mMovieInfoAdapter.notifyItemRangeChanged(
                                (inputMessage.arg1-1)*inputMessage.arg2,
                                inputMessage.arg2
                        );
                        mRVPopMovies.setVisibility(View.VISIBLE);
                        mMovieInfoAdapter.notifyDataSetChanged();
                        mTVError.setVisibility(View.GONE);
                        break;
                    case MovieInfoAdapter.QUERY_MESSAGE_FINISHED_WITH_ERROR:
                        if (mMovieInfoAdapter.getItemCount() == 0) {
                            mTVError.setText(getResources().getText(R.string.error_downloading_info));
                            mTVError.setVisibility(View.VISIBLE);
                            mPBLoadingMovies.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(MainActivity.this, R.string.error_downloading_info, Toast.LENGTH_LONG)
                                    .show();
                        }
                        break;
                    default:
                        //unreachable!
                        Log.e(TAG, "Reached invalid place in code.");
                        break;
                }
            }
        };

        int num_cols = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                COLUMNS_LANDSCAPE : COLUMNS_PORTRAIT;
        GridLayoutManager grid = new GridLayoutManager(this, num_cols);
        mEndlessScrollListener = new EndlessRecyclerViewScrollListener(grid) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!mCurrSortBy.equals(TheMovieDBAPI.SORTBY_FAVORITES)) {
                    load_more_movies(page + 1);
                }
            }
        };

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(MOVIE_LIST_PARCELABLE_STATE_KEY)) {
            mMovieInfoAdapter.setResponseModel(
                    (MovieInfoResponseModel) savedInstanceState.getParcelable(MOVIE_LIST_PARCELABLE_STATE_KEY)
            );
        } else {
            reset_movie_list();
        }

        mRVPopMovies.addOnScrollListener(mEndlessScrollListener);
        mRVPopMovies.setLayoutManager(grid);
        mRVPopMovies.setHasFixedSize(true);
        mRVPopMovies.setAdapter(mMovieInfoAdapter);
    }

    private void adjustSortByTitle() {
        if (mCurrSortBy.equals(TheMovieDBAPI.SORTBY_FAVORITES)) {
            mSortByTitle.setText(R.string.sort_by_favorites);
        } else if (mCurrSortBy.equals(TheMovieDBAPI.SORTBY_POPULARITY)) {
            mSortByTitle.setText(R.string.sort_by_popular);
        } else if (mCurrSortBy.equals(TheMovieDBAPI.SORTBY_RATING)) {
            mSortByTitle.setText(R.string.sort_by_rating);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_LIST_PARCELABLE_STATE_KEY, mMovieInfoAdapter.getResponseModel());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrSortBy.equals(TheMovieDBAPI.SORTBY_FAVORITES)) {
            load_favorites();
        }
    }

    private void load_more_movies(int page) {
        if (Util.isOnline(this)) {
            mMovieInfoAdapter.queryPosterHashes(page, mCurrSortBy, mQueryPosterHandler);
            mTVError.setVisibility(View.GONE);
        } else {
            mTVError.setText(R.string.error_no_internet);
            mTVError.setVisibility(View.VISIBLE);
        }
    }
    private void reset_movie_list() {
        if (mCurrSortBy.equals(TheMovieDBAPI.SORTBY_FAVORITES)) {
            load_favorites();
        } else {
            mMovieInfoAdapter.reset();
            mEndlessScrollListener.resetState();
            load_more_movies(1);
        }
    }
    private void load_favorites() {
        mMovieInfoAdapter.reset();
        mSwipeRefreshLayout.setEnabled(false);
        mEndlessScrollListener.resetState();
        MovieInfoResponseModel responseModel = new MovieInfoResponseModel(mFavoriteDB.getAllFavorites());
        mMovieInfoAdapter.setResponseModel(responseModel);
        mMovieInfoAdapter.notifyDataSetChanged();
        if (responseModel.getResults().isEmpty()) {
            mTVError.setText(R.string.no_favorites);
            mTVError.setVisibility(View.VISIBLE);
        } else {
            mTVError.setVisibility(View.GONE);
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
                                mSwipeRefreshLayout.setEnabled(true);
                                break;
                            }
                            case R.id.action_sortby_rating: {
                                mCurrSortBy = TheMovieDBAPI.SORTBY_RATING;
                                mSwipeRefreshLayout.setEnabled(true);
                                break;
                            }
                            case R.id.action_sortby_favorites: {
                                mCurrSortBy = TheMovieDBAPI.SORTBY_FAVORITES;
                                mSwipeRefreshLayout.setEnabled(false);
                                break;
                            }
                            default: {
                                return false;
                            }
                        }

                        if (!mCurrSortBy.equals(last_sort_by)) {
                            SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefs_editor = prefs.edit();
                            prefs_editor.putString(CURR_SORT_PREF, mCurrSortBy);
                            prefs_editor.apply();

                            reset_movie_list();
                            adjustSortByTitle();
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
                if (mCurrSortBy.equals(TheMovieDBAPI.SORTBY_FAVORITES)) {
                    reset_movie_list();
                } else {
                    mRVPopMovies.smoothScrollToPosition(0);
                    load_more_movies(1);
                }
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
