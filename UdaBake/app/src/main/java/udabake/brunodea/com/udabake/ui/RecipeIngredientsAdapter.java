package udabake.brunodea.com.udabake.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.model.RecipeIngredientModel;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {
    private List<RecipeIngredientModel> mRecipeIngredients;

    private LayoutInflater mInflater;
    private Context mContext;

    public RecipeIngredientsAdapter(Context context, List<RecipeIngredientModel> ingredients) {
        mRecipeIngredients = ingredients;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.fragment_recipe_ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mRecipeIngredients != null && mInflater != null) {
            RecipeIngredientModel ingredientModel = mRecipeIngredients.get(position);
            holder.mTVIngredientName.setText(ingredientModel.getIngredient());
            holder.mTVIngredientQuantity.setText(
                    mContext.getString(R.string.ingredient_quantity,
                        ingredientModel.getQuantity(),
                        ingredientModel.getMeasure()
                    )
            );
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient) TextView mTVIngredientName;
        @BindView(R.id.tv_ingredient_quantity) TextView mTVIngredientQuantity;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTVIngredientName.getText() + "'";
        }
    }
}
