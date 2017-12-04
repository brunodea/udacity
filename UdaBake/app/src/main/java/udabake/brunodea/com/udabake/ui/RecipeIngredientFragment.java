package udabake.brunodea.com.udabake.ui;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.model.RecipeModel;

public class RecipeIngredientFragment extends Fragment {
    private static final String RECIPE_MODEL_ARG = "recipe_model_arg";

    @BindView(R.id.rv_recipe_ingredients) RecyclerView mRVIngredients;

    private RecipeModel mRecipeModel;

    public RecipeIngredientFragment() {
    }

    public static RecipeIngredientFragment newInstance(RecipeModel recipeModel) {
        RecipeIngredientFragment fragment = new RecipeIngredientFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_MODEL_ARG, recipeModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mRecipeModel = args.getParcelable(RECIPE_MODEL_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_ingredient_list, container, false);
        ButterKnife.bind(this, view);
        RecipeIngredientsAdapter adapter =
                new RecipeIngredientsAdapter(getContext(), mRecipeModel.getIngredients());
        mRVIngredients.setHasFixedSize(true);
        mRVIngredients.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRVIngredients.setAdapter(adapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
