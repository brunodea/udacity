package brunodea.udacity.com.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import brunodea.udacity.com.popmovies.adapter.MoviewReviewAdapter;
import brunodea.udacity.com.popmovies.model.MovieInfoModel;
import brunodea.udacity.com.popmovies.model.MovieReviewResponseModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewsActivity extends AppCompatActivity {
    private final static String TAG = "MovieReviewsActivity";
    public final static String MOVIE_INFO_EXTRA = "movie_info";
    private final static String MOVIE_REVIEWS_PARCELABLE_STATE_KEY = "movie_reviews";

    @BindView(R.id.pb_loading_movie_reviews) ProgressBar mPBLoadingReviews;
    @BindView(R.id.tv_error_movie_reviews) TextView mTVError;
    @BindView(R.id.rv_movie_reviews) RecyclerView mRVMovieReviews;

    private MovieInfoModel mMovieInfoModel;
    private MoviewReviewAdapter mMovieReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);
        ButterKnife.bind(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_reviews_title);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MOVIE_INFO_EXTRA)) {
                mMovieInfoModel = intent.getParcelableExtra(MOVIE_INFO_EXTRA);
                mMovieReviewAdapter = new MoviewReviewAdapter(this);

                if (savedInstanceState != null &&
                        savedInstanceState.containsKey(MOVIE_REVIEWS_PARCELABLE_STATE_KEY)) {
                    mMovieReviewAdapter.setResponseModel(
                            (MovieReviewResponseModel)
                                    savedInstanceState.getParcelable(MOVIE_REVIEWS_PARCELABLE_STATE_KEY)
                    );
                } else {
                    if (Util.isOnline(this)) {
                        mMovieReviewAdapter.queryReviews(String.valueOf(mMovieInfoModel.getId()),
                                new MoviewReviewAdapter.QueryCallback() {
                                    @Override
                                    public void onQueryStarted() {
                                        mPBLoadingReviews.setVisibility(View.VISIBLE);
                                        mTVError.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onQueryFailure() {
                                        mPBLoadingReviews.setVisibility(View.GONE);
                                        mTVError.setVisibility(View.VISIBLE);
                                        mTVError.setText(R.string.error_loading_reviews);
                                        mRVMovieReviews.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onQuerySuccess() {
                                        mPBLoadingReviews.setVisibility(View.GONE);
                                        if (mMovieReviewAdapter.getItemCount() == 0) {
                                            mTVError.setText(R.string.no_reviews);
                                            mTVError.setVisibility(View.VISIBLE);
                                            mRVMovieReviews.setVisibility(View.GONE);
                                        } else {
                                            mRVMovieReviews.setVisibility(View.VISIBLE);
                                        }
                                        mMovieReviewAdapter.notifyDataSetChanged();
                                    }
                                }
                        );
                    } else {
                        mPBLoadingReviews.setVisibility(View.GONE);
                        mTVError.setVisibility(View.VISIBLE);
                        mTVError.setText(R.string.error_no_internet);
                        mRVMovieReviews.setVisibility(View.GONE);
                    }
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL, false);
                mRVMovieReviews.setLayoutManager(layoutManager);
                mRVMovieReviews.setHasFixedSize(true);
                mRVMovieReviews.setAdapter(mMovieReviewAdapter);
                //mRVMovieReviews.setBackground(TheMovieDBAPI.getDrawable(this, TheMovieDBAPI.IMAGE_W780,
                //        mMovieInfoModel.getPosterPath()));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_REVIEWS_PARCELABLE_STATE_KEY, mMovieReviewAdapter.getResponseModel());
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
