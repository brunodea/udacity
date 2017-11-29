package udabake.brunodea.com.udabake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import udabake.brunodea.com.udabake.model.RecipeModel;
import udabake.brunodea.com.udabake.ui.RecipeCardListFragment;

public class MainActivity extends AppCompatActivity implements RecipeCardListFragment.OnRecipeItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClickRecipeItem(RecipeModel recipe) {
        // TODO
        Toast.makeText(this, recipe.getName(), Toast.LENGTH_LONG)
                .show();
    }
}
