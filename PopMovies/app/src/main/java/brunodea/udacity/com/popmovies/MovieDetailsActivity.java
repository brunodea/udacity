package brunodea.udacity.com.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import brunodea.udacity.com.popmovies.model.TheMovieDBResultModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String RESULT_MODEL_EXTRA = "result_model";

    @BindView(R.id.iv_movie_poster_details) ImageView mIVPoster;
    @BindView(R.id.rb_movie_rating) RatingBar mRBRating;
    @BindView(R.id.tv_movie_overview) TextView mTVOverview;
    @BindView(R.id.tv_original_title) TextView mTVOriginalTitle;
    @BindView(R.id.tv_release_date) TextView mTVReleaseDate;
    @BindView(R.id.details_toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(RESULT_MODEL_EXTRA)) {
                TheMovieDBResultModel model = intent.getParcelableExtra(RESULT_MODEL_EXTRA);
                TheMovieDBAPI.downloadImageToView(this,
                    mIVPoster,
                    TheMovieDBAPI.IMAGE_W780,
                    model.getBackdropPath()
                );
                mToolbarLayout.setTitle(model.getTitle());
                mRBRating.setRating((float) model.getVoteAverage()/2.f);
                mTVOverview.setText(model.getOverview());
                mTVOriginalTitle.setText(
                    fromHtml(getString(R.string.original_title, model.getOriginalTitle()))
                );
                mTVReleaseDate.setText(
                    fromHtml(getString(R.string.release_date, model.getReleaseDate()))
                );
            }
        }
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
}
