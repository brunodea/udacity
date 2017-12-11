package udabake.brunodea.com.udabake.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.UdabakeUtil;
import udabake.brunodea.com.udabake.model.RecipeModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnRecipeItemClickListener}
 * interface.
 */
public class RecipeCardListFragment extends Fragment {
    private static final String RECIPES_PARCELABLE_KEY = "recipes_parcelable";
    public static final String RECIPE_PREF_JSON = "recipe_model_json";

    private int mColumnCount = 1;
    private OnRecipeItemClickListener mOnRecipeItemClickListener;
    private RecipeItemAdapter mRecipeItemAdapter;

    @BindView(R.id.pb_loading_recipes) ProgressBar mPBLoadingRecipes;
    @BindView(R.id.rv_recipes) RecyclerView mRVRecipes;
    @BindView(R.id.tv_message) TextView mTVMessage;

    public RecipeCardListFragment() {
    }

    public static RecipeCardListFragment newInstance() {
        return new RecipeCardListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_card_item_list, container, false);
        ButterKnife.bind(this, view);

        mRecipeItemAdapter = new RecipeItemAdapter(mOnRecipeItemClickListener);

        if (UdabakeUtil.isTablet(getContext())) {
            mColumnCount = UdabakeUtil.isLandscape(getContext()) ? 5 : 4;
        }
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            mRVRecipes.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRVRecipes.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mRVRecipes.setHasFixedSize(true);
        mRVRecipes.setAdapter(mRecipeItemAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES_PARCELABLE_KEY)) {
            ArrayList<RecipeModel> models = savedInstanceState.getParcelableArrayList(RECIPES_PARCELABLE_KEY);
            mRecipeItemAdapter.setRecipeModels(models);
        } else if (getActivity() != null && isAdded()) {
            loadRecipesFromWeb();
        }
        return view;
    }

    private void loadRecipesFromWeb() {
        if (UdabakeUtil.isOnline(getContext())) {
            mRecipeItemAdapter.queryRecipes(new RecipeItemAdapter.QueryCallback() {
                @Override
                public void onQueryStarted() {
                    mPBLoadingRecipes.setVisibility(View.VISIBLE);
                    mRVRecipes.setVisibility(View.GONE);
                    mTVMessage.setVisibility(View.GONE);
                }

                @Override
                public void onQueryFailure() {
                    mPBLoadingRecipes.setVisibility(View.GONE);
                    mRVRecipes.setVisibility(View.GONE);
                    mTVMessage.setVisibility(View.VISIBLE);
                    mTVMessage.setText(R.string.query_fail);
                }

                @Override
                public void onQuerySuccess() {
                    mPBLoadingRecipes.setVisibility(View.GONE);
                    mRVRecipes.setVisibility(View.VISIBLE);
                    mTVMessage.setVisibility(View.GONE);
                }
            });
        } else {
            mPBLoadingRecipes.setVisibility(View.GONE);
            mRVRecipes.setVisibility(View.GONE);
            mTVMessage.setVisibility(View.VISIBLE);
            mTVMessage.setText(R.string.no_internet);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeItemClickListener) {
            mOnRecipeItemClickListener = (OnRecipeItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnRecipeItemClickListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPES_PARCELABLE_KEY, mRecipeItemAdapter.getRecipeModels());
        super.onSaveInstanceState(outState);
    }

    public interface OnRecipeItemClickListener {
        void onClickRecipeItem(RecipeModel recipe);
    }
}
