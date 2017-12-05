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
import udabake.brunodea.com.udabake.model.RecipeStepModel;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {
    private static final String TAG = "RecipeStepsAdapter";

    private List<RecipeStepModel> mRecipeStepModels;
    private LayoutInflater mInflater;
    private OnRecipeStepClickListener mOnRecipeStepClickListener;

    private Context mContext;

    public RecipeStepsAdapter(Context context, List<RecipeStepModel> steps, OnRecipeStepClickListener listener) {
        mRecipeStepModels = steps;
        mOnRecipeStepClickListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.recipe_step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mRecipeStepModels != null && mInflater != null) {
            final RecipeStepModel model = mRecipeStepModels.get(position);

            holder.mTVStepShort.setText(mContext.getString(R.string.step_short, position,
                    model.getShortDescription()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecipeStepClickListener != null) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mOnRecipeStepClickListener.onStepClick(model, holder.getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeStepModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_step_short) TextView mTVStepShort;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnRecipeStepClickListener {
        void onStepClick(RecipeStepModel model, int position);
    }
}
