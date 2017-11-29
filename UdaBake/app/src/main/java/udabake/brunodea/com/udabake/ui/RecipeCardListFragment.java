package udabake.brunodea.com.udabake.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
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

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnRecipeItemClickListener mOnRecipeItemClickListener;
    private RecipeItemAdapter mRecipeItemAdapter;

    @BindView(R.id.pb_loading_recipes) ProgressBar mPBLoadingRecipes;
    @BindView(R.id.rv_recipes) RecyclerView mRVRecipes;
    @BindView(R.id.tv_message) TextView mTVMessage;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeCardListFragment() {
    }

    @SuppressWarnings("unused")
    public static RecipeCardListFragment newInstance(int columnCount) {
        RecipeCardListFragment fragment = new RecipeCardListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipecarditem_list, container, false);
        //ButterKnife.bind(view);

        mRVRecipes = view.findViewById(R.id.rv_recipes);
        mPBLoadingRecipes = view.findViewById(R.id.pb_loading_recipes);
        mTVMessage = view.findViewById(R.id.tv_message);

        mRecipeItemAdapter = new RecipeItemAdapter(mOnRecipeItemClickListener);

        Context context = view.getContext();
        if (mColumnCount <= 1) {
            mRVRecipes.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRVRecipes.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mRVRecipes.setHasFixedSize(true);
        mRVRecipes.setAdapter(mRecipeItemAdapter);

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
        return view;
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

    public interface OnRecipeItemClickListener {
        void onClickRecipeItem(RecipeModel recipe);
    }
}
