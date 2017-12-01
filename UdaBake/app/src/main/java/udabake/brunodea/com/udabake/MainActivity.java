package udabake.brunodea.com.udabake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.ui.RecipeCardListFragment;
import udabake.brunodea.com.udabake.ui.RecipeDetailsFragment;

public class MainActivity extends AppCompatActivity implements RecipeCardListFragment.OnRecipeItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClickRecipeItem(RecipeModel recipe) {
        if (recipe.getSteps().isEmpty()) {
            Toast.makeText(this, R.string.error_no_steps, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, RecipeDetailsActivity.class);
            intent.putExtra(RecipeDetailsFragment.RECIPE_STEP_MODEL_ARG, recipe.getSteps().get(0));
            startActivity(intent);
        }
    }
}
