package udabake.brunodea.com.udabake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.ui.RecipeCardListFragment;

public class MainActivity extends AppCompatActivity implements RecipeCardListFragment.OnRecipeItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_fragment_container, RecipeCardListFragment.newInstance());
        transaction.commit();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClickRecipeItem(RecipeModel recipe) {
        if (recipe.getSteps().isEmpty()) {
            Toast.makeText(this, R.string.error_no_steps, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, RecipeDetailsActivity.class);
            intent.putExtra(RecipeDetailsActivity.RECIPE_MODEL_EXTRA, recipe);
            startActivity(intent);
        }
    }
}
