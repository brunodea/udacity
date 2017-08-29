package brunodea.udacity.com.popmovies.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import brunodea.udacity.com.popmovies.R;
import brunodea.udacity.com.popmovies.TheMovieDBRest;
import brunodea.udacity.com.popmovies.model.TheMovieDBResponseModel;
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
    private ArrayList<String> mListOfPosterHashes;
    // current query page.
    private int mCurrentPage;
    private int mMaxPages;

    public TheMovieDBAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mCurrentPage = 1;
        mMaxPages = 0;
        mListOfPosterHashes = new ArrayList<>();
    }

    public void queryPosterHashes(final Handler query_handler) {
        if (mMaxPages == 0 || mCurrentPage <= mMaxPages) {
            Log.i(TAG, "Querying for Poster Hashes");
            query_handler.sendEmptyMessage(QUERY_MESSAGE_STARTED);
            TheMovieDBRest.discover(mCurrentPage, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(TAG, "Finished querying for poster hashes with success!");
                    mCurrentPage += 1;
                    TheMovieDBResponseModel model = TheMovieDBResponseModel.parseJSON(response.toString());
                    mMaxPages = model.getTotalPages();
                    for (TheMovieDBResponseModel.ResultModel m : model.getResults()) {
                        mListOfPosterHashes.add(m.getPosterPath());
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

    @Override
    public TheMovieDBAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.main_grid_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String image_path = mListOfPosterHashes.get(position);
        Picasso.with(mInflater.getContext())
                .load("http://image.tmdb.org/t/p/w185/"+image_path)
                .into(holder.mIVMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mListOfPosterHashes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIVMoviePoster;

        public ViewHolder(View item_view) {
            super(item_view);
            mIVMoviePoster = (ImageView) item_view.findViewById(R.id.iv_movie_poster);
        }
    }
}
