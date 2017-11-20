package brunodea.udacity.com.popmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import brunodea.udacity.com.popmovies.R;
import brunodea.udacity.com.popmovies.model.MovieVideoModel;
import brunodea.udacity.com.popmovies.model.MovieVideoResponseModel;
import brunodea.udacity.com.popmovies.moviedb.TheMovieDBAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieVideoAdapter  extends RecyclerView.Adapter<MovieVideoAdapter.ViewHolder> {
    private static final String TAG = "MovieVideoAdapter";

    private LayoutInflater mInflater;
    private MovieVideoResponseModel mResponseModel;
    private OnItemClickListener mOnItemClickListener;

    public MovieVideoAdapter(Context context, OnItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        mResponseModel = null;
        mOnItemClickListener = listener;
    }

    public void setResponseModel(final MovieVideoResponseModel model) {
        mResponseModel = model;
    }
    public MovieVideoResponseModel getResponseModel() {
        return mResponseModel;
    }

    public void queryVideos(final String movie_id, final QueryCallback callback) {
        if (mResponseModel == null) {
            Log.i(TAG, "Querying for videos; movie ID: " + movie_id);
            callback.onQueryStarted();

            TheMovieDBAPI.getMovieVideos(movie_id, new Callback<MovieVideoResponseModel>() {
                @Override
                public void onResponse(Call<MovieVideoResponseModel> call, Response<MovieVideoResponseModel> response) {
                    if (response.isSuccessful()) {
                        mResponseModel = response.body();
                        callback.onQuerySuccess();
                    } else {
                        callback.onQueryFailure();
                    }
                }

                @Override
                public void onFailure(Call<MovieVideoResponseModel> call, Throwable t) {
                    Log.e(TAG, t.toString());
                    callback.onQueryFailure();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mResponseModel != null) {
            final MovieVideoModel model = mResponseModel.getResults().get(position);
            holder.mTVVideoTitle.setText(model.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(model);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mResponseModel == null ? 0 : mResponseModel.getResults().size();
    }

    public interface OnItemClickListener {
        void onItemClick(final MovieVideoModel model);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_video_title) TextView mTVVideoTitle;

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
