package brunodea.udacity.com.popmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import brunodea.udacity.com.popmovies.adapter.MovieVideoAdapter;
import brunodea.udacity.com.popmovies.model.MovieInfoModel;
import brunodea.udacity.com.popmovies.model.MovieVideoModel;
import brunodea.udacity.com.popmovies.model.MovieVideoResponseModel;
import brunodea.udacity.com.popmovies.moviedb.TheMovieDBAPI;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String RESULT_MODEL_EXTRA = "result_model";
    private static final String MOVIE_VIDEO_PARCELABLE_STATE_KEY = "movie_videos";

    @BindView(R.id.iv_movie_poster_details) ImageView mIVPoster;
    @BindView(R.id.rb_movie_rating) RatingBar mRBRating;
    @BindView(R.id.tv_movie_overview) TextView mTVOverview;
    @BindView(R.id.tv_original_title) TextView mTVOriginalTitle;
    @BindView(R.id.tv_release_date) TextView mTVReleaseDate;
    @BindView(R.id.details_toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.tv_movie_videos_global) TextView mTVVideoGlobal;
    @BindView(R.id.pb_loading_movie_videos) ProgressBar mPBLoadingVideos;
    @BindView(R.id.rv_movie_videos) RecyclerView mRVMovieVideos;

    private MovieInfoModel mMovieInfoModel;
    private MovieVideoModel mMovieVideoModel;

    private MovieVideoAdapter mMovieVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(RESULT_MODEL_EXTRA)) {
                mMovieInfoModel = intent.getParcelableExtra(RESULT_MODEL_EXTRA);
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

                mMovieVideoAdapter = new MovieVideoAdapter(this, new MovieVideoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(MovieVideoModel model) {
                        // from https://stackoverflow.com/a/12439378
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + model.getId()));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + model.getId()));
                        try {
                            MovieDetailsActivity.this.startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            MovieDetailsActivity.this.startActivity(webIntent);
                        }
                    }
                });

                if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_VIDEO_PARCELABLE_STATE_KEY)) {
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

                LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL, false);
                mRVMovieVideos.setLayoutManager(layoutManager);
                mRVMovieVideos.setHasFixedSize(true);
                mRVMovieVideos.setAdapter(mMovieVideoAdapter);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_VIDEO_PARCELABLE_STATE_KEY, mMovieVideoAdapter.getResponseModel());
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
