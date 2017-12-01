package udabake.brunodea.com.udabake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import udabake.brunodea.com.udabake.model.RecipeStepModel;
import udabake.brunodea.com.udabake.ui.RecipeDetailsFragment;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RecipeDetailsFragment.RECIPE_STEP_MODEL_ARG)) {
            RecipeStepModel step = intent.getParcelableExtra(RecipeDetailsFragment.RECIPE_STEP_MODEL_ARG);
            RecipeDetailsFragment frag = RecipeDetailsFragment.newInstance(step);

            FragmentManager frag_manager = getSupportFragmentManager();
            frag_manager.beginTransaction()
                    .add(R.id.frame_fragment_container, frag)
                    .commit();
        }
    }
}
