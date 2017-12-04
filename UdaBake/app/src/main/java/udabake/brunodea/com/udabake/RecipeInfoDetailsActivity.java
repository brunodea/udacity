package udabake.brunodea.com.udabake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.model.RecipeStepModel;
import udabake.brunodea.com.udabake.ui.RecipeIngredientFragment;
import udabake.brunodea.com.udabake.ui.RecipeStepDetailsFragment;

public class RecipeInfoDetailsActivity extends AppCompatActivity
        implements RecipeStepDetailsFragment.OnActionListener {
    public static String RECIPE_MODEL_EXTRA = "recipe_model_extra";
    public static String RECIPE_STEP_POS = "recipe_step_pos";
    public static String RECIPE_IS_INGREDIENTS_EXTRA = "recipe_is_ingredients_extra";

    private static String VIDEO_POSITION = "video_position";

    private RecipeModel mRecipeModel;
    private RecipeStepDetailsFragment mStepDetailsFrag;

    private int mCurrStep;
    private int mNumSteps;
    private long mVideoPosition;

    enum RecipeInfoToShow {
        StepDetails,
        Ingredients
    }

    private RecipeInfoToShow mRecipeInfoToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info_details);

        // if the intent is null or doesn't have the extra, there was a programmer error, so the
        // app should indeed fail!
        Intent intent = getIntent();
        mRecipeModel = intent.getParcelableExtra(RECIPE_MODEL_EXTRA);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mRecipeModel.getName());
        }

        if (intent.getBooleanExtra(RECIPE_IS_INGREDIENTS_EXTRA, false)) {
            mRecipeInfoToShow = RecipeInfoToShow.Ingredients;
        } else {
            mRecipeInfoToShow = RecipeInfoToShow.StepDetails;
            mNumSteps = mRecipeModel.getSteps().size();
            if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_STEP_POS)) {
                mCurrStep = savedInstanceState.getInt(RECIPE_STEP_POS);
            } else {
                mCurrStep = intent.getIntExtra(RECIPE_STEP_POS, 0);
            }

            if (savedInstanceState != null && savedInstanceState.containsKey(VIDEO_POSITION)) {
                mVideoPosition = savedInstanceState.getLong(VIDEO_POSITION);
            } else {
                mVideoPosition = 0;
            }
        }

        replaceDetailsFragmentToCurrStep();
    }

    @Override
    public void onButtonPrevStepClicked() {
        if (mCurrStep > 0) {
            mCurrStep -= 1;
            replaceDetailsFragmentToCurrStep();
        }
    }

    @Override
    public void onButtonNextStepClicked() {
        if (mCurrStep < mNumSteps - 1) {
            mCurrStep += 1;
            replaceDetailsFragmentToCurrStep();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        switch (mRecipeInfoToShow) {
            case StepDetails: {
                outState.putInt(RECIPE_STEP_POS, mCurrStep);
                outState.putLong(VIDEO_POSITION, mStepDetailsFrag.videoPosition());
            }
            break;
            default: break;
        }
        super.onSaveInstanceState(outState);
    }

    private void replaceDetailsFragmentToCurrStep() {
        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction transaction = mgr.beginTransaction();
        switch (mRecipeInfoToShow) {
            case StepDetails: {
                RecipeStepModel step = mRecipeModel.getSteps().get(mCurrStep);
                RecipeStepDetailsFragment.StepPosition pos =
                        mCurrStep == 0 ?
                                RecipeStepDetailsFragment.StepPosition.First :
                                mCurrStep == mNumSteps - 1 ?
                                        RecipeStepDetailsFragment.StepPosition.Last :
                                        RecipeStepDetailsFragment.StepPosition.Other;

                mStepDetailsFrag = RecipeStepDetailsFragment.newInstance(step, pos, mVideoPosition);
                transaction.replace(R.id.frame_fragment_container, mStepDetailsFrag);
            }
            break;
            case Ingredients: {
                RecipeIngredientFragment frag = RecipeIngredientFragment.newInstance(mRecipeModel);
                transaction.replace(R.id.frame_fragment_container, frag);
            }
            break;
        }

        transaction.commit();
    }
}
