package udabake.brunodea.com.udabake.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.model.RecipeModel;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RecipeModel} and makes a call to the
 * specified {@link udabake.brunodea.com.udabake.ui.RecipeCardListFragment.OnRecipeItemClickListener}.
 */
public class RecipeItemAdapter extends RecyclerView.Adapter<RecipeItemAdapter.ViewHolder> {

    private final List<RecipeModel> mRecipeModels;
    private final RecipeCardListFragment.OnRecipeItemClickListener mOnRecipeItemClickListener;

    public RecipeItemAdapter(List<RecipeModel> recipes, RecipeCardListFragment.OnRecipeItemClickListener listener) {
        mRecipeModels = recipes;
        mOnRecipeItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipecarditem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // TODO: set all other holder stuff base on mRecipeModels.get(position).
        RecipeModel model = mRecipeModels.get(position);
        holder.mRecipeModel = model;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRecipeItemClickListener != null) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mOnRecipeItemClickListener.onClick(holder.mRecipeModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        RecipeModel mRecipeModel;

        @BindView(R.id.iv_recipe_item_image) ImageView mRecipeImage;
        @BindView(R.id.tv_recipe_item_name) TextView mRecipeName;
        @BindView(R.id.tv_recipe_item_servings) TextView mItemServings;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mRecipeName.getText() + "'";
        }
    }
}
