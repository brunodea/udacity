package brunodea.udacity.com.popmovies.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import brunodea.udacity.com.popmovies.R;
import brunodea.udacity.com.popmovies.model.TheMovieDBResponseModel;
import brunodea.udacity.com.popmovies.model.TheMovieDBResultModel;
import brunodea.udacity.com.popmovies.moviedb.TheMovieDBAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adapter class to be used by some RecyclerView.
 * It takes data from TheMovieDB and populate a ViewHolder with the gathered information.
 */
public class TheMovieDBAdapter extends RecyclerView.Adapter<TheMovieDBAdapter.ViewHolder> {
    public static final int QUERY_MESSAGE_STARTED = 1;
    public static final int QUERY_MESSAGE_FINISHED_WITH_SUCCESS = 2;
    public static final int QUERY_MESSAGE_FINISHED_WITH_ERROR = 3;

    private static final String TAG = "TheMovieDBAdapter";

    private LayoutInflater mInflater;

    private TheMovieDBResponseModel mResponseModel;
    private OnItemClickListener mOnItemClickListener;

    public TheMovieDBAdapter(Context context, OnItemClickListener itemClickListener) {
        mInflater = LayoutInflater.from(context);
        mResponseModel = null;
        mOnItemClickListener = itemClickListener;
    }

    public void queryPosterHashes(final int page, final @TheMovieDBAPI.SortByDef String sortBy, final Handler query_handler) {
        if (mResponseModel == null || page <= mResponseModel.getTotalPages()) {
            Log.i(TAG, "Querying for Poster Hashes");
            query_handler.sendEmptyMessage(QUERY_MESSAGE_STARTED);

            TheMovieDBAPI.getMovies(page, sortBy, new Callback<TheMovieDBResponseModel>() {
                @Override
                public void onResponse(Call<TheMovieDBResponseModel> call, Response<TheMovieDBResponseModel> response) {
                    if (response.isSuccessful()) {
                        TheMovieDBResponseModel model = response.body();
                        if (mResponseModel == null) {
                            mResponseModel = model;
                        } else {
                            mResponseModel.addAll(model.getResults());
                        }
                        Message msg = query_handler.obtainMessage();
                        msg.what = QUERY_MESSAGE_FINISHED_WITH_SUCCESS;
                        msg.arg1 = page;
                        msg.arg2 = model.getResults().size();
                        query_handler.sendMessage(msg);
                    } else {
                        query_handler.sendEmptyMessage(QUERY_MESSAGE_FINISHED_WITH_ERROR);
                    }
                }

                @Override
                public void onFailure(Call<TheMovieDBResponseModel> call, Throwable t) {
                    Log.e(TAG, t.toString());
                    query_handler.sendEmptyMessage(QUERY_MESSAGE_FINISHED_WITH_ERROR);
                }
            });
        }
    }

    public void reset() {
        mResponseModel = null;
        notifyDataSetChanged();
    }

    public TheMovieDBResponseModel getResponseModel() {
        return mResponseModel;
    }
    public void setResponseModel(TheMovieDBResponseModel model) {
        mResponseModel = model;
    }

    @Override
    public TheMovieDBAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.main_grid_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mResponseModel != null) {
            final TheMovieDBResultModel rm = mResponseModel.getResults().get(position);
            TheMovieDBAPI.downloadImageToView(
                mInflater.getContext(),
                holder.mIVMoviePoster,
                TheMovieDBAPI.IMAGE_W185,
                rm.getPosterPath()
            );
            holder.mTVMovieTitle.setText(rm.getOriginalTitle());
            holder.mTVPosition.setText(String.valueOf(position + 1));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.OnItemClick(rm);
                }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mResponseModel == null ? 0 : mResponseModel.getResults().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movie_poster) ImageView mIVMoviePoster;
        @BindView(R.id.tv_movie_title) TextView mTVMovieTitle;
        @BindView(R.id.tv_position) TextView mTVPosition;

        ViewHolder(View item_view) {
            super(item_view);
            ButterKnife.bind(this, item_view);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(final TheMovieDBResultModel model);
    }
}
