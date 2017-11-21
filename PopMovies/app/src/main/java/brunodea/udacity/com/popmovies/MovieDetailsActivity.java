package brunodea.udacity.com.popmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import brunodea.udacity.com.popmovies.adapter.MovieVideoAdapter;
import brunodea.udacity.com.popmovies.db.FavoritesDB;
import brunodea.udacity.com.popmovies.model.MovieInfoModel;
import brunodea.udacity.com.popmovies.model.MovieVideoModel;
import brunodea.udacity.com.popmovies.model.MovieVideoResponseModel;
import brunodea.udacity.com.popmovies.moviedb.TheMovieDBAPI;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String RESULT_MODEL_EXTRA = "result_model";
    private static final String MOVIE_INFO_PARCELABLE_STATE_KEY = "movie_info";
    private static final String MOVIE_VIDEO_PARCELABLE_STATE_KEY = "movie_videos";

    @BindView(R.id.iv_movie_poster_details) ImageView mIVPoster;
    @BindView(R.id.rb_movie_rating) RatingBar mRBRating;
    @BindView(R.id.tv_movie_overview) TextView mTVOverview;
    @BindView(R.id.tv_original_title) TextView mTVOriginalTitle;
    @BindView(R.id.tv_release_date) TextView mTVReleaseDate;
    @BindView(R.id.details_toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.tv_goto_reviews) TextView mTVGotoReviews;
    @BindView(R.id.ib_favorite) ImageButton mIBFavorite;

    @BindView(R.id.tv_movie_videos_global) TextView mTVVideoGlobal;
    @BindView(R.id.pb_loading_movie_videos) ProgressBar mPBLoadingVideos;
    @BindView(R.id.rv_movie_videos) RecyclerView mRVMovieVideos;

    private MovieInfoModel mMovieInfoModel;
    private MovieVideoAdapter mMovieVideoAdapter;

    private FavoritesDB mFavoritesDB;
    private boolean mIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mFavoritesDB = new FavoritesDB(this);
        mIsFavorite = false;

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovieVideoAdapter = new MovieVideoAdapter(this, new MovieVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieVideoModel model) {
                // from https://stackoverflow.com/a/12439378
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + model.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + model.getKey()));
                try {
                    MovieDetailsActivity.this.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    MovieDetailsActivity.this.startActivity(webIntent);
                }
            }
        });

        reloadFromBundle(savedInstanceState);

        // if it wasn't reloaded from the bundle, it should come from an intent.
        Intent intent = getIntent();
        if (mMovieInfoModel == null && intent != null && intent.hasExtra(RESULT_MODEL_EXTRA)) {
            mMovieInfoModel = intent.getParcelableExtra(RESULT_MODEL_EXTRA);
        }

        if (mMovieInfoModel != null) {
            loadMovieInfo();
        }

        mTVGotoReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetailsActivity.this, MovieReviewsActivity.class);
                intent.putExtra(MovieReviewsActivity.MOVIE_INFO_EXTRA, mMovieInfoModel);
                startActivity(intent);
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRVMovieVideos.setLayoutManager(layoutManager);
        mRVMovieVideos.setHasFixedSize(true);
        mRVMovieVideos.setAdapter(mMovieVideoAdapter);
    }

    public void reloadFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIE_INFO_PARCELABLE_STATE_KEY)) {
                mMovieInfoModel = savedInstanceState.getParcelable(MOVIE_INFO_PARCELABLE_STATE_KEY);
            }
            if (savedInstanceState.containsKey(MOVIE_VIDEO_PARCELABLE_STATE_KEY)) {
                mMovieVideoAdapter.setResponseModel(
                        (MovieVideoResponseModel) savedInstanceState.getParcelable(MOVIE_VIDEO_PARCELABLE_STATE_KEY)
                );
            } else {
                if (Util.isOnline(this)) {
                    mMovieVideoAdapter.queryVideos(String.valueOf(mMovieInfoModel.getId()),
                            new MovieVideoAdapter.QueryCallback() {
                                @Override
                                public void onQueryStarted() {
                                    mPBLoadingVideos.setVisibility(View.VISIBLE);
                                    mTVVideoGlobal.setVisibility(View.VISIBLE);
                                    mRVMovieVideos.setVisibility(View.GONE);
                                }

                                @Override
                                public void onQueryFailure() {
                                    mPBLoadingVideos.setVisibility(View.GONE);
                                    mTVVideoGlobal.setVisibility(View.VISIBLE);
                                    mTVVideoGlobal.setText(R.string.error_loading_videos);
                                    mRVMovieVideos.setVisibility(View.GONE);
                                }

                                @Override
                                public void onQuerySuccess() {
                                    mPBLoadingVideos.setVisibility(View.GONE);
                                    mTVVideoGlobal.setVisibility(View.GONE);
                                    mRVMovieVideos.setVisibility(View.VISIBLE);
                                    mMovieVideoAdapter.notifyDataSetChanged();
                                }
                            }
                    );
                } else {
                    mPBLoadingVideos.setVisibility(View.GONE);
                    mTVVideoGlobal.setVisibility(View.VISIBLE);
                    mTVVideoGlobal.setText(R.string.error_no_internet);
                    mRVMovieVideos.setVisibility(View.GONE);
                }
            }
        }
    }

    private void loadMovieInfo() {
        TheMovieDBAPI.downloadImageToView(this,
                mIVPoster,
                TheMovieDBAPI.IMAGE_W780,
                mMovieInfoModel.getBackdropPath()
        );

        mToolbarLayout.setTitle(mMovieInfoModel.getTitle());
        mRBRating.setRating((float) mMovieInfoModel.getVoteAverage()/2.f);
        mTVOverview.setText(mMovieInfoModel.getOverview());
        mTVOriginalTitle.setText(
                fromHtml(getString(R.string.original_title, mMovieInfoModel.getOriginalTitle()))
        );
        mTVReleaseDate.setText(
                fromHtml(getString(R.string.release_date, mMovieInfoModel.getReleaseDate()))
        );

        mIsFavorite = mFavoritesDB.isFavorite(mMovieInfoModel.getId());
        mIBFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsFavorite = !mIsFavorite;
                adjustFavoriteStarDrawable();
                if (mIsFavorite) {
                    mFavoritesDB.insertMovieInfo(mMovieInfoModel);
                } else {
                    mFavoritesDB.deleteMovieInfo(mMovieInfoModel.getId());
                }
            }
        });
    }

    private void adjustFavoriteStarDrawable() {
        if (mIsFavorite) {
            mIBFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
        } else {
            mIBFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_VIDEO_PARCELABLE_STATE_KEY, mMovieVideoAdapter.getResponseModel());
        outState.putParcelable(MOVIE_INFO_PARCELABLE_STATE_KEY, mMovieInfoModel);

        super.onSaveInstanceState(outState);
    }

    // from: https://stackoverflow.com/a/37905107
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
           result = Html.fromHtml(html);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
