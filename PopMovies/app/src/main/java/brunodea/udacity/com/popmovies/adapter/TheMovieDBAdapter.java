package brunodea.udacity.com.popmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import brunodea.udacity.com.popmovies.MovieDetailsActivity;
import brunodea.udacity.com.popmovies.R;
import brunodea.udacity.com.popmovies.TheMovieDBAPI;
import brunodea.udacity.com.popmovies.model.TheMovieDBResponseModel;
import brunodea.udacity.com.popmovies.model.TheMovieDBResultModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

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
    private @TheMovieDBAPI.SortByDef String mSortBy;

    private TheMovieDBResponseModel mResponseModel;
    // current query page.
    private int mCurrentPage;

    public TheMovieDBAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mCurrentPage = 0;
        mResponseModel = null;
        mSortBy = null;
    }

    public void queryPosterHashes(final @TheMovieDBAPI.SortByDef String sortBy, final Handler query_handler) {
        if (mResponseModel == null || mCurrentPage <= mResponseModel.getTotalPages()) {
            Log.i(TAG, "Querying for Poster Hashes");
            query_handler.sendEmptyMessage(QUERY_MESSAGE_STARTED);
            TheMovieDBAPI.discover(mCurrentPage + 1, sortBy, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(TAG, "Finished querying for poster hashes with success!");
                    mCurrentPage += 1;
                    mSortBy = sortBy;
                    Gson gson = new Gson();
                    TheMovieDBResponseModel model = gson.fromJson(response.toString(), TheMovieDBResponseModel.class);
                    if (mResponseModel == null) {
                        mResponseModel = model;
                    } else {
                        mResponseModel.addAll(model.getResults());
                    }
                    query_handler.sendEmptyMessage(QUERY_MESSAGE_FINISHED_WITH_SUCCESS);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i(TAG, "Finished querying for poster hashes with failure: " + errorResponse.toString());
                    query_handler.sendEmptyMessage(QUERY_MESSAGE_FINISHED_WITH_ERROR);
                }
            });
        }
    }

    public void reset() {
        mCurrentPage = 0;
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
                    Intent intent = new Intent(mInflater.getContext(), MovieDetailsActivity.class);
                    intent.putExtra(MovieDetailsActivity.RESULT_MODEL_EXTRA, rm);
                    mInflater.getContext().startActivity(intent);
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
}
