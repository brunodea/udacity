package udabake.brunodea.com.udabake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.ui.RecipeIngredientFragment;
import udabake.brunodea.com.udabake.ui.RecipeStepDetailsFragment;
import udabake.brunodea.com.udabake.ui.RecipeStepsListFragment;

public class RecipeDetailsActivity extends AppCompatActivity
        implements RecipeStepsListFragment.OnActionClickListener,
        RecipeStepDetailsFragment.OnActionListener {
    public static final String RECIPE_MODEL_EXTRA = "recipe_model_extra";

    private boolean mIsMultiPane;
    private int mCurrStep;
    private RecipeModel mRecipeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        mIsMultiPane = false;
        mCurrStep = 0;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RECIPE_MODEL_EXTRA)) {
            mRecipeModel = intent.getParcelableExtra(RECIPE_MODEL_EXTRA);
            getSupportActionBar().setTitle(mRecipeModel.getName());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_recipe_details_main,
                    RecipeStepsListFragment.newInstance(mRecipeModel));

            if (findViewById(R.id.frame_recipe_details_rhs) != null) {
                mIsMultiPane = true;
                multiPaneReplaceRHSFragment(false);
            }

            transaction.commit();
        }
    }

    @Override
    public void onButtonPrevStepClicked() {
        if (mCurrStep > 0) {
            mCurrStep -= 1;
            multiPaneReplaceRHSFragment(false);
        }
    }

    @Override
    public void onButtonNextStepClicked() {
        if (mCurrStep < mRecipeModel.getSteps().size() - 1) {
            mCurrStep += 1;
            multiPaneReplaceRHSFragment(false);
        }
    }

    private void multiPaneReplaceRHSFragment(boolean is_ingredient_list) {
        ActionBar actionBar = getSupportActionBar();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (is_ingredient_list) {
            actionBar.setTitle(getString(R.string.ingredients_title, mRecipeModel.getName()));
            transaction.replace(R.id.frame_recipe_details_rhs,
                    RecipeIngredientFragment.newInstance(mRecipeModel));
        } else {
            RecipeStepDetailsFragment.StepPosition pos =
                    mCurrStep == 0 ?
                            RecipeStepDetailsFragment.StepPosition.First :
                            mCurrStep == mRecipeModel.getSteps().size() - 1 ?
                                    RecipeStepDetailsFragment.StepPosition.Last :
                                    RecipeStepDetailsFragment.StepPosition.Other;
            actionBar.setTitle(getString(R.string.step_title, mCurrStep, mRecipeModel.getName()));
            transaction.replace(R.id.frame_recipe_details_rhs,
                    RecipeStepDetailsFragment.newInstance(
                            mRecipeModel.getSteps().get(mCurrStep),
                            pos,
                            0,
                            (int) getResources().getDimension(R.dimen.video_portrait_height_tablet)
                    )
            );
        }
        transaction.commit();
    }

    @Override
    public void onClickGoToIngredients() {
        if (mIsMultiPane) {
            multiPaneReplaceRHSFragment(true);
        } else {
            Intent intent = new Intent(this, RecipeInfoDetailsActivity.class);
            intent.putExtra(RecipeInfoDetailsActivity.RECIPE_MODEL_EXTRA, mRecipeModel);
            intent.putExtra(RecipeInfoDetailsActivity.RECIPE_IS_INGREDIENTS_EXTRA, true);
            startActivity(intent);
        }
    }

    @Override
    public void onClickStep(int position) {
        if (mIsMultiPane) {
            mCurrStep = position;
            multiPaneReplaceRHSFragment(false);
        } else {
            Intent intent = new Intent(this, RecipeInfoDetailsActivity.class);
            intent.putExtra(RecipeInfoDetailsActivity.RECIPE_STEP_POS, position);
            intent.putExtra(RecipeInfoDetailsActivity.RECIPE_MODEL_EXTRA, mRecipeModel);
            startActivity(intent);
        }
    }
}
