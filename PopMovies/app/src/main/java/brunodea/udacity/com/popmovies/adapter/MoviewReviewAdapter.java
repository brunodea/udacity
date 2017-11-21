package brunodea.udacity.com.popmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import brunodea.udacity.com.popmovies.R;
import brunodea.udacity.com.popmovies.model.MovieReviewModel;
import brunodea.udacity.com.popmovies.model.MovieReviewResponseModel;
import brunodea.udacity.com.popmovies.moviedb.TheMovieDBAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviewReviewAdapter extends RecyclerView.Adapter<MoviewReviewAdapter.ViewHolder> {
    private final static String TAG = "MovieReviewAdapter";

    private LayoutInflater mInflater;
    private MovieReviewResponseModel mResponseModel;

    public MoviewReviewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mResponseModel = null;
    }

    public void setResponseModel(final MovieReviewResponseModel model) {
        mResponseModel = model;
    }
    public MovieReviewResponseModel getResponseModel() {
        return mResponseModel;
    }

    public void queryReviews(final String movie_id, final QueryCallback callback) {
        if (mResponseModel == null) {
            Log.i(TAG, "Querying for reviews; movie ID: " + movie_id);
            callback.onQueryStarted();

            TheMovieDBAPI.getMovieReviews(movie_id, new Callback<MovieReviewResponseModel>() {
                @Override
                public void onResponse(Call<MovieReviewResponseModel> call, Response<MovieReviewResponseModel> response) {
                    if (response.isSuccessful()) {
                        mResponseModel = response.body();
                        Log.i(TAG, "Query success: " + mResponseModel.getResults().size() + " results.");
                        callback.onQuerySuccess();
                    } else {
                        Log.i(TAG, "Query failed!");
                        callback.onQueryFailure();
                    }
                }

                @Override
                public void onFailure(Call<MovieReviewResponseModel> call, Throwable t) {
                    Log.e(TAG, t.toString());
                    callback.onQueryFailure();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mResponseModel != null) {
            final MovieReviewModel model = mResponseModel.getResults().get(position);
            holder.mTVReviewerName.setText(model.getAuthor());
            holder.mTVReview.setText(model.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mResponseModel == null ? 0 : mResponseModel.getResults().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_reviewer_name) TextView mTVReviewerName;
        @BindView(R.id.tv_review) TextView mTVReview;

        ViewHolder(View item_view) {
            super(item_view);
            ButterKnife.bind(this, item_view);
        }
    }

    public interface QueryCallback {
        void onQueryStarted();
        void onQueryFailure();
        void onQuerySuccess();
    }
}
