package udabake.brunodea.com.udabake.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.model.RecipeStepModel;

public class RecipeStepsListFragment extends Fragment {
    private static final String ARG_MODEL = "recipe_model";

    private RecipeModel mRecipeModel;
    private OnActionClickListener mOnActionListener;

    @BindView(R.id.bt_goto_ingredients) Button mBTGotoIngredients;
    @BindView(R.id.rv_recipe_steps) RecyclerView mRVSteps;

    public RecipeStepsListFragment() {
    }

    public static RecipeStepsListFragment newInstance(RecipeModel model) {
        RecipeStepsListFragment fragment = new RecipeStepsListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MODEL, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);

        if (getArguments() != null) {
            mRecipeModel = getArguments().getParcelable(ARG_MODEL);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_steps_list, container, false);
        ButterKnife.bind(this, view);
        final RecipeStepsAdapter adapter = new RecipeStepsAdapter(getContext(),
                mRecipeModel.getSteps(), new RecipeStepsAdapter.OnRecipeStepClickListener() {
            @Override
            public void onStepClick(RecipeStepModel model, int position) {
                mOnActionListener.onClickStep(position);
            }
        });

        mRVSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        mRVSteps.setHasFixedSize(true);
        mRVSteps.setAdapter(adapter);
        mBTGotoIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnActionListener.onClickGoToIngredients();
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRVSteps.getContext(),
                LinearLayoutManager.VERTICAL);
        mRVSteps.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActionClickListener) {
            mOnActionListener = (OnActionClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnActionListener= null;
    }

    public interface OnActionClickListener {
        void onClickGoToIngredients();
        void onClickStep(int position);
    }
}
