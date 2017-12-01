package udabake.brunodea.com.udabake.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.net.RecipesAPI;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RecipeModel} and makes a call to the
 * specified {@link udabake.brunodea.com.udabake.ui.RecipeCardListFragment.OnRecipeItemClickListener}.
 */
public class RecipeItemAdapter extends RecyclerView.Adapter<RecipeItemAdapter.ViewHolder> {
    private static final String TAG = "RecipeItemAdapter";

    private ArrayList<RecipeModel> mRecipeModels;
    private final RecipeCardListFragment.OnRecipeItemClickListener mOnRecipeItemClickListener;

    private LayoutInflater mInflater;

    public RecipeItemAdapter(RecipeCardListFragment.OnRecipeItemClickListener listener) {
        mOnRecipeItemClickListener = listener;
        mRecipeModels = null;
    }

    public void setRecipeModels(ArrayList<RecipeModel> recipes) {
        mRecipeModels = recipes;
        notifyDataSetChanged();
    }

    public ArrayList<RecipeModel> getRecipeModels() {
        return mRecipeModels;
    }

    public void queryRecipes(final QueryCallback callback) {
        if (mRecipeModels == null) {
            Log.i(TAG, "Started querying for recipes.");
            callback.onQueryStarted();

            RecipesAPI.getRecipes(new Callback<ArrayList<RecipeModel>>() {
                @Override
                public void onResponse(Call<ArrayList<RecipeModel>> call, Response<ArrayList<RecipeModel>> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, "Successful querying for recipes.");
                        mRecipeModels = response.body();
                        notifyDataSetChanged();
                        callback.onQuerySuccess();
                    } else {
                        Log.e(TAG, "Unsuccessful querying for recipes.");
                        callback.onQueryFailure();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<RecipeModel>> call, Throwable t) {
                    Log.e(TAG, "Failed to query for recipes: " + t.toString());
                    callback.onQueryFailure();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.fragment_recipe_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mRecipeModels != null && mInflater != null) {
            // TODO: set all other holder stuff base on mRecipeModels.get(position).
            final RecipeModel model = mRecipeModels.get(position);

            holder.mRecipeModel = model;
            RecipesAPI.downloadImageToView(
                    mInflater.getContext(),
                    holder.mRecipeImage,
                    model.getImage()
            );
            holder.mRecipeName.setText(model.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecipeItemClickListener != null) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mOnRecipeItemClickListener.onClickRecipeItem(holder.mRecipeModel);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeModels == null ? 0 : mRecipeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecipeModel mRecipeModel;

        @BindView(R.id.iv_recipe_item_image) ImageView mRecipeImage;
        @BindView(R.id.tv_recipe_item_name) TextView mRecipeName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mRecipeName.getText() + "'";
        }
    }

    public interface QueryCallback {
        void onQueryStarted();
        void onQueryFailure();
        void onQuerySuccess();
    }
}
